package positioning.com.wifipositioning;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mihir on 5/10/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION=1;
    static final String DATABASE_NAME="DB";

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ROUTER_TABLE="CREATE TABLE Router_Details ( id INTEGER PRIMARY KEY AUTOINCREMENT, BSSID TEXT, SSID TEXT, level INTEGER, distance FLOAT )";
        db.execSQL(CREATE_ROUTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Router_Details");
        this.onCreate(db);
    }

    static final String TABLE_TEST="Router_Details";
    static final String KEY_ID="id";
    static final String KEY_BSSID="BSSID";
    static final String KEY_SSID="SSID";
    static final String KEY_LEVEL="level";
    static final String KEY_DISTANCE="distance";

    public void addRouter(DB DB){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_BSSID, DB.BSSID);
        values.put(KEY_SSID,DB.SSID);
        values.put(KEY_LEVEL, DB.level);
        values.put(KEY_DISTANCE,DB.distance);
        db.insert(TABLE_TEST, null, values);
        db.close();
    }

    public void delete(){
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("DROP TABLE test");
        this.onCreate(db);
    }
}
