package positioning.com.wifipositioning;

/**
 * Created by Mihir on 5/10/2015.
 */
public class TestDB {
    private int id;
    String BSSID;
    int level;

    public TestDB(){}

    public TestDB(String BSSID, int level){
        super();
        this.BSSID=BSSID;
        this.level=level;
    }

    public String toString(){
        return "[id="+id+", BSSID="+BSSID+", level="+level+"]";
    }
}
