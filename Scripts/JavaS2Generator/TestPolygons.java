
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

import com.google.gson.*;

public class TestPolygons {

    public static void main(String[] args) {

        String root_dir = new String("/Users/karim/Desktop/univzipped/");
        String bounding_local_parsed_path = new String(root_dir + "bounding_local_parsed_final.json");
        String bounding_university_parsed_path = new String(root_dir + "bounding_university_parsed_final.json");
        String bounding_college_parsed_path = new String(root_dir + "bounding_college_parsed_final.json");
        String bounding_university_parsed_path_bad = new String(root_dir + "bounding_university_parsed_bad.json");
        String bounding_college_parsed_path_bad = new String(root_dir + "bounding_college_parsed_bad.json");

        String cells_local_path = new String(root_dir + "cells_local.json");
        String cells_university_path = new String(root_dir + "cells_university.json");
        String cells_college_path = new String(root_dir + "cells_college.json");
        String cells_university_bad_path = new String(root_dir + "cells_university_bad.json");
        String cells_college_bad_path = new String(root_dir + "cells_college_bad.json");

        String[] retrievepaths = {bounding_local_parsed_path,bounding_university_parsed_path,bounding_college_parsed_path,bounding_university_parsed_path_bad,bounding_college_parsed_path_bad};
        String[] savepaths = {cells_local_path,cells_university_path,cells_college_path,cells_university_bad_path,cells_college_bad_path};

        for (int retrievepathsindex=0; retrievepathsindex < retrievepaths.length; retrievepathsindex+=1) {
            JsonParser parser = new JsonParser();
            try {

                JsonElement jsontree = parser.parse(
                        new FileReader(
                                retrievepaths[retrievepathsindex]
                        )
                );

                ArrayList<Relation> relations = new ArrayList<>();
                ArrayList<Way> ways = new ArrayList<>();

                JsonArray local = jsontree.getAsJsonArray();

                for (Object a : local) {

                    JsonObject jbounda = (JsonObject) a;

                    String type = jbounda.get("type").getAsString();
                    String name = jbounda.get("name").getAsString();
                    Boolean nameisid = jbounda.get("nameisid").getAsBoolean();
                    JsonArray jwaylist = jbounda.get("nodelist").getAsJsonArray();


                    if (type.equals("relation")) {

                        ArrayList<ArrayList<ArrayList<Float>>> waylist = new ArrayList<ArrayList<ArrayList<Float>>>();


                        for (int i = 0; i < jwaylist.size(); i++) {
                            JsonArray jpairlist = jwaylist.get(i).getAsJsonArray();
                            ArrayList<ArrayList<Float>> pairlist = new ArrayList<ArrayList<Float>>();
                            for (int j = 0; j < jpairlist.size(); j++) {
                                JsonArray jnodepair = jpairlist.get(j).getAsJsonArray();
                                ArrayList<Float> nodepair = new ArrayList<Float>();
                                for (int k = 0; k < jnodepair.size(); k++) {
                                    nodepair.add(jnodepair.get(k).getAsFloat());

                                }
                                pairlist.add(nodepair);
                            }
                            waylist.add(pairlist);
                        }

                        relations.add(new Relation(type, name, nameisid, waylist));
                    } else {
                        JsonArray jpairlist = jbounda.get("nodelist").getAsJsonArray();
                        ArrayList<ArrayList<Float>> pairlist = new ArrayList<ArrayList<Float>>();
                        for (int j = 0; j < jpairlist.size(); j++) {
                            JsonArray jnodepair = jpairlist.get(j).getAsJsonArray();
                            ArrayList<Float> nodepair = new ArrayList<Float>();
                            for (int k = 0; k < jnodepair.size(); k++) {
                                nodepair.add(jnodepair.get(k).getAsFloat());

                            }
                            pairlist.add(nodepair);
                        }

                        ways.add(new Way(type, name, nameisid, pairlist));
                    }
                }

                S2PolygonBuilder pb = new S2PolygonBuilder();

                S2RegionCoverer cover10s = new S2RegionCoverer();
                cover10s.setMaxLevel(10);
                cover10s.setMinLevel(10);

                S2RegionCoverer cover16s = new S2RegionCoverer();
                cover16s.setMaxLevel(16);
                cover16s.setMinLevel(16);

                S2RegionCoverer cover10to16 = new S2RegionCoverer();
                cover10to16.setMaxLevel(16);
                cover10to16.setMinLevel(10);

//            S2RegionCoverer cover30s = new S2RegionCoverer();
//            cover30s.setMaxLevel(30);
//            cover30s.setMinLevel(30);

                S2RegionCoverer cover10and11 = new S2RegionCoverer();
                cover10and11.setMaxLevel(11);
                cover10and11.setMinLevel(10);

                S2RegionCoverer cover11and12 = new S2RegionCoverer();
                cover11and12.setMaxLevel(12);
                cover11and12.setMinLevel(11);

                S2RegionCoverer cover12and13 = new S2RegionCoverer();
                cover12and13.setMaxLevel(13);
                cover12and13.setMinLevel(12);

                S2RegionCoverer cover13and14 = new S2RegionCoverer();
                cover13and14.setMaxLevel(14);
                cover13and14.setMinLevel(13);

                S2RegionCoverer cover14and15 = new S2RegionCoverer();
                cover14and15.setMaxLevel(15);
                cover14and15.setMinLevel(14);

                S2RegionCoverer cover15and16 = new S2RegionCoverer();
                cover15and16.setMaxLevel(16);
                cover15and16.setMinLevel(15);

                switch (retrievepathsindex) {
                    case 1:
                        relations.remove(363);
                        relations.remove(998);
                        ways.remove(18815);
                        ways.remove(31166);
                        ways.remove(33705);
                        ways.remove(33710);
                        break;
                    case 2:
                        ways.remove(25382);
                        break;
                    case 3:
                        relations.remove(1);
                        relations.remove(1);
                        relations.remove(1);
                        relations.remove(2);
                        relations.remove(2);
                        relations.remove(9);
                        relations.remove(12);
                        break;
                    case 4:
                        relations.remove(3);
                        relations.remove(3);
                        break;
                }

                int count = 0;
                for (Relation rel : relations) {
                    System.out.print("Relation: ");
                    System.out.println(count);
                    count += 1;
                    ArrayList<S2Polygon> polygons = new ArrayList<>();
                    for (ArrayList<ArrayList<Float>> waylist : rel.nodelist) {
                        ArrayList<S2Point> points = new ArrayList<>();
                        for (ArrayList<Float> nodelist : waylist) {
                            Float lat = nodelist.get(0);
                            Float lon = nodelist.get(1);
                            S2Point p = S2LatLng.fromDegrees(lat, lon).toPoint();
                            points.add(p);
                        }

                        for (int i = 0; i < points.size(); i += 1) {
                            if (i == points.size() - 1) {
                                pb.addEdge(points.get(i), points.get(0));
                            } else {
                                pb.addEdge(points.get(i), points.get(i + 1));
                            }
                        }
                        S2Polygon newpoly = pb.assemblePolygon();
                        polygons.add(newpoly);
                        pb.dump();
                    }
                    for (S2Polygon poly : polygons) {
                        pb.addPolygon(poly);
                    }
                    S2Polygon newpoly = pb.assemblePolygon();
                    pb.dump();

                    S2CellUnion cucover10s = cover10s.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids10s = cucover10s.cellIds();
                    for (S2CellId cellid : newcellids10s) {
                        rel.cover10s.add(cellid.toToken());
                    }

                    S2CellUnion cucover16s = cover16s.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids16s = cucover16s.cellIds();
                    for (S2CellId cellid : newcellids16s) {
                        rel.cover16s.add(cellid.toToken());
                    }

                    S2CellUnion cucover10to16 = cover10to16.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids10to16 = cucover10to16.cellIds();
                    for (S2CellId cellid : newcellids10to16) {
                        rel.cover10to16.add(cellid.toToken());
                    }
//
//                S2CellUnion cucover30s = cover30s.getCovering(newpoly);
//                ArrayList<S2CellId> newcellids30s = cucover30s.cellIds();
//                for (S2CellId cellid : newcellids30s) {
//                    rel.cover30s.add(cellid.toToken());
//                }

                    S2CellUnion cucover10and11 = cover10and11.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids10and11 = cucover10and11.cellIds();
                    for (S2CellId cellid : newcellids10and11) {
                        rel.cover10and11.add(cellid.toToken());
                    }

                    S2CellUnion cucover11and12 = cover11and12.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids11and12 = cucover11and12.cellIds();
                    for (S2CellId cellid : newcellids11and12) {
                        rel.cover11and12.add(cellid.toToken());
                    }

                    S2CellUnion cucover12and13 = cover12and13.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids12and13 = cucover12and13.cellIds();
                    for (S2CellId cellid : newcellids12and13) {
                        rel.cover12and13.add(cellid.toToken());
                    }

                    S2CellUnion cucover13and14 = cover13and14.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids13and14 = cucover13and14.cellIds();
                    for (S2CellId cellid : newcellids13and14) {
                        rel.cover13and14.add(cellid.toToken());
                    }

                    S2CellUnion cucover14and15 = cover14and15.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids14and15 = cucover14and15.cellIds();
                    for (S2CellId cellid : newcellids14and15) {
                        rel.cover14and15.add(cellid.toToken());
                    }

                    S2CellUnion cucover15and16 = cover15and16.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids15and16 = cucover15and16.cellIds();
                    for (S2CellId cellid : newcellids15and16) {
                        rel.cover15and16.add(cellid.toToken());
                    }
                }
                count = 0;
                for (Way way : ways) {
                    System.out.print("Way: ");
                    System.out.println(count);
                    count += 1;
                    ArrayList<S2Point> points = new ArrayList<>();
                    for (ArrayList<Float> nodelist : way.nodelist) {
                        Float lat = nodelist.get(0);
                        Float lon = nodelist.get(1);
                        S2Point p = S2LatLng.fromDegrees(lat, lon).toPoint();
                        points.add(p);
                    }

                    for (int i = 0; i < points.size(); i += 1) {
                        if (i == points.size() - 1) {
                            pb.addEdge(points.get(i), points.get(0));
                        } else {
                            pb.addEdge(points.get(i), points.get(i + 1));
                        }
                    }


                    S2Polygon newpoly = pb.assemblePolygon();
                    pb.dump();

                    S2CellUnion cucover10s = cover10s.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids10s = cucover10s.cellIds();
                    for (S2CellId cellid : newcellids10s) {
                        way.cover10s.add(cellid.toToken());
                    }

                    S2CellUnion cucover16s = cover16s.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids16s = cucover16s.cellIds();
                    for (S2CellId cellid : newcellids16s) {
                        way.cover16s.add(cellid.toToken());
                    }

                    S2CellUnion cucover10to16 = cover10to16.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids10to16 = cucover10to16.cellIds();
                    for (S2CellId cellid : newcellids10to16) {
                        way.cover10to16.add(cellid.toToken());
                    }

//                S2CellUnion cucover30s = cover30s.getCovering(newpoly);
//                ArrayList<S2CellId> newcellids30s = cucover30s.cellIds();
//                for (S2CellId cellid : newcellids30s) {
//                    way.cover30s.add(cellid.toToken());
//                }

                    S2CellUnion cucover10and11 = cover10and11.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids10and11 = cucover10and11.cellIds();
                    for (S2CellId cellid : newcellids10and11) {
                        way.cover10and11.add(cellid.toToken());
                    }

                    S2CellUnion cucover11and12 = cover11and12.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids11and12 = cucover11and12.cellIds();
                    for (S2CellId cellid : newcellids11and12) {
                        way.cover11and12.add(cellid.toToken());
                    }

                    S2CellUnion cucover12and13 = cover12and13.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids12and13 = cucover12and13.cellIds();
                    for (S2CellId cellid : newcellids12and13) {
                        way.cover12and13.add(cellid.toToken());
                    }

                    S2CellUnion cucover13and14 = cover13and14.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids13and14 = cucover13and14.cellIds();
                    for (S2CellId cellid : newcellids13and14) {
                        way.cover13and14.add(cellid.toToken());
                    }

                    S2CellUnion cucover14and15 = cover14and15.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids14and15 = cucover14and15.cellIds();
                    for (S2CellId cellid : newcellids14and15) {
                        way.cover14and15.add(cellid.toToken());
                    }

                    S2CellUnion cucover15and16 = cover15and16.getCovering(newpoly);
                    ArrayList<S2CellId> newcellids15and16 = cucover15and16.cellIds();
                    for (S2CellId cellid : newcellids15and16) {
                        way.cover15and16.add(cellid.toToken());
                    }
                }
                String stringjson;
                JsonArray relwayholder = new JsonArray();
                for (Relation rel : relations) {
                    JsonObject relholder = new JsonObject();
                    relholder.addProperty("name", rel.name);
                    relholder.addProperty("type", rel.type);
                    relholder.addProperty("nameisid", rel.nameisid);
                    JsonArray jarrwaylist = new JsonArray();

                    for (ArrayList<ArrayList<Float>> theway : rel.nodelist) {
                        JsonArray jarrpairlist = new JsonArray();
                        for (ArrayList<Float> pairs : theway) {
                            JsonArray jarrpair = new JsonArray();
                            Float lat = pairs.get(0);
                            Float lon = pairs.get(1);
                            jarrpair.add(lat);
                            jarrpair.add(lon);
                            jarrpairlist.add(jarrpair);
                        }
                        jarrwaylist.add(jarrpairlist);
                    }

                    JsonArray jcover10s = new JsonArray();
                    for (String cellid : rel.cover10s) {
                        jcover10s.add(cellid);
                    }

                    JsonArray jcover16s = new JsonArray();
                    for (String cellid : rel.cover16s) {
                        jcover16s.add(cellid);
                    }

//                JsonArray jcover30s = new JsonArray();
//                for (String cellid: rel.cover30s) {
//                    jcover30s.add(cellid);
//                }

                    JsonArray jcover10to16 = new JsonArray();
                    for (String cellid : rel.cover10to16) {
                        jcover10to16.add(cellid);
                    }

                    JsonArray jcover10and11 = new JsonArray();
                    for (String cellid : rel.cover10and11) {
                        jcover10and11.add(cellid);
                    }

                    JsonArray jcover11and12 = new JsonArray();
                    for (String cellid : rel.cover11and12) {
                        jcover11and12.add(cellid);
                    }

                    JsonArray jcover12and13 = new JsonArray();
                    for (String cellid : rel.cover12and13) {
                        jcover12and13.add(cellid);
                    }

                    JsonArray jcover13and14 = new JsonArray();
                    for (String cellid : rel.cover13and14) {
                        jcover13and14.add(cellid);
                    }

                    JsonArray jcover14and15 = new JsonArray();
                    for (String cellid : rel.cover14and15) {
                        jcover14and15.add(cellid);
                    }

                    JsonArray jcover15and16 = new JsonArray();
                    for (String cellid : rel.cover15and16) {
                        jcover15and16.add(cellid);
                    }

                    relholder.add("nodelist", jarrwaylist);
                    relholder.add("cover10s", jcover10s);
                    relholder.add("cover16s", jcover16s);
//                relholder.add("cover30s",jcover30s);
                    relholder.add("cover10to16", jcover10to16);
                    relholder.add("cover10and11", jcover10and11);
                    relholder.add("cover11and12", jcover11and12);
                    relholder.add("cover12and13", jcover12and13);
                    relholder.add("cover13and14", jcover13and14);
                    relholder.add("cover14and15", jcover14and15);
                    relholder.add("cover15and16", jcover15and16);
                    relwayholder.add(relholder);
                }
                for (Way way : ways) {
                    JsonObject wayholder = new JsonObject();
                    wayholder.addProperty("name", way.name);
                    wayholder.addProperty("type", way.type);
                    wayholder.addProperty("nameisid", way.nameisid);


                    JsonArray jarrpairlist = new JsonArray();
                    for (ArrayList<Float> pairs : way.nodelist) {
                        JsonArray jarrpair = new JsonArray();
                        Float lat = pairs.get(0);
                        Float lon = pairs.get(1);
                        jarrpair.add(lat);
                        jarrpair.add(lon);
                        jarrpairlist.add(jarrpair);
                    }

                    JsonArray jcover10s = new JsonArray();
                    for (String cellid : way.cover10s) {
                        jcover10s.add(cellid);
                    }

                    JsonArray jcover16s = new JsonArray();
                    for (String cellid : way.cover16s) {
                        jcover16s.add(cellid);
                    }

//                JsonArray jcover30s = new JsonArray();
//                for (String cellid: way.cover30s) {
//                    jcover30s.add(cellid);
//                }

                    JsonArray jcover10to16 = new JsonArray();
                    for (String cellid : way.cover10to16) {
                        jcover10to16.add(cellid);
                    }

                    JsonArray jcover10and11 = new JsonArray();
                    for (String cellid : way.cover10and11) {
                        jcover10and11.add(cellid);
                    }

                    JsonArray jcover11and12 = new JsonArray();
                    for (String cellid : way.cover11and12) {
                        jcover11and12.add(cellid);
                    }

                    JsonArray jcover12and13 = new JsonArray();
                    for (String cellid : way.cover12and13) {
                        jcover12and13.add(cellid);
                    }

                    JsonArray jcover13and14 = new JsonArray();
                    for (String cellid : way.cover13and14) {
                        jcover13and14.add(cellid);
                    }

                    JsonArray jcover14and15 = new JsonArray();
                    for (String cellid : way.cover14and15) {
                        jcover14and15.add(cellid);
                    }

                    JsonArray jcover15and16 = new JsonArray();
                    for (String cellid : way.cover15and16) {
                        jcover15and16.add(cellid);
                    }

                    wayholder.add("nodelist", jarrpairlist);
                    wayholder.add("cover10s", jcover10s);
                    wayholder.add("cover16s", jcover16s);
//                wayholder.add("cover30s",jcover30s);
                    wayholder.add("cover10to16", jcover10to16);
                    wayholder.add("cover10and11", jcover10and11);
                    wayholder.add("cover11and12", jcover11and12);
                    wayholder.add("cover12and13", jcover12and13);
                    wayholder.add("cover13and14", jcover13and14);
                    wayholder.add("cover14and15", jcover14and15);
                    wayholder.add("cover15and16", jcover15and16);

                    relwayholder.add(wayholder);
                }
                Gson gson = new Gson();
                stringjson = gson.toJson(relwayholder);
                try (PrintWriter out = new PrintWriter(savepaths[retrievepathsindex])) {
                    out.println(stringjson);
                }
            } catch (JsonIOException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }




    }
}