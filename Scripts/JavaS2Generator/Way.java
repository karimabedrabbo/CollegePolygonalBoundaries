import java.util.ArrayList;

public class Way {
    String type;
    String name;
    Boolean nameisid;
    ArrayList<ArrayList<Float>> nodelist;
    ArrayList<String> cover10s = new ArrayList<>();
    ArrayList<String> cover16s = new ArrayList<>();
    ArrayList<String> cover10and11 = new ArrayList<>();
    ArrayList<String> cover11and12 = new ArrayList<>();
    ArrayList<String> cover12and13 = new ArrayList<>();
    ArrayList<String> cover13and14 = new ArrayList<>();
    ArrayList<String> cover14and15 = new ArrayList<>();
    ArrayList<String> cover15and16 = new ArrayList<>();
    ArrayList<String> cover10to16 = new ArrayList<>();
    ArrayList<String> cover30s = new ArrayList<>();

    public Way(String type, String name, Boolean nameIsId,  ArrayList<ArrayList<Float>> pairlist) {
        this.type = type;
        this.name = name;
        this.nameisid = nameIsId;
        this.nodelist = pairlist;
    }
}
