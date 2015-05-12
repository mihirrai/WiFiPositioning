package positioning.com.wifipositioning;

/**
 * Created by Mihir on 5/11/2015.
 */
public class DB {
    private int id;
    float distance;
    String BSSID,SSID;
    int level;

    public DB(){}

    public DB(String BSSID, String SSID,int level, float distance){
        super();
        this.BSSID=BSSID;
        this.SSID=SSID;
        this.level=level;
        this.distance=distance;
    }

    public String toString(){
        return "[id="+id+", BSSID="+BSSID+", level="+level+"]";
    }
}
