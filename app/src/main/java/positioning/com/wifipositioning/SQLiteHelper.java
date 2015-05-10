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
    static final String DATABASE_NAME="TestDB";

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE="CREATE TABLE test ( id INTEGER PRIMARY KEY AUTOINCREMENT, BSSID TEXT, level INTEGER )";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS test");
        this.onCreate(db);
    }

    static final String TABLE_TEST="test";
    static final String KEY_ID="id";
    static final String KEY_BSSID="BSSID";
    static final String KEY_LEVEL="level";

    public void addRouter(TestDB test){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_BSSID, test.BSSID);
        values.put(KEY_LEVEL, test.level);
        db.insert(TABLE_TEST, null, values);
        db.close();
    }

    public void delete(){
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("DROP TABLE test");
        this.onCreate(db);
    }
}
