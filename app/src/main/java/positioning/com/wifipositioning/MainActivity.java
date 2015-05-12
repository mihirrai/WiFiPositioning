package positioning.com.wifipositioning;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button b,b2;
    public TextView tv;
    public static final int RESULT_LOAD_IMAGE = 1000;
    private static final int CAMERA_REQUEST = 1001;
    ImageView iv;
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b.setOnClickListener(this);
        b2.setOnClickListener(this);
        tv=(TextView)findViewById(R.id.textView);
        iv=(ImageView)findViewById(R.id.imageView);
        db = new SQLiteHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.add_map){
            String names[] ={"Choose From Gallery","Take Picture"};
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Choose");
            ListView lv = (ListView) convertView.findViewById(R.id.listview);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                            break;
                        case 1:
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            break;
                    }
                }
            });
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
            lv.setAdapter(adapter);
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(photo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                WifiManager wifiManager=(WifiManager)this.getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> results = wifiManager.getScanResults();
                Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult lhs, ScanResult rhs) {
                        return (rhs.level <lhs.level ? -1 : (lhs.level==rhs.level ? 0 : 1));
                    }
                };
                Collections.sort(results, comparator);
                //Log.d("Wifiresults", results + "");
                for (ScanResult result : results){
                    /*if(result.BSSID.contains("mihir")) {
                        int lvl = result.level;
                    }*/
                    Double t=calculateDistance((double) result.level, result.frequency);
                    db.addRouter(new DB(result.BSSID,result.SSID,result.level,t.floatValue()));
                    Log.d("Result","BSSID: "+result.BSSID+" SSID: "+result.SSID+" Level: "+result.level);
                }
                //startService(new Intent(getBaseContext(), Service.class));
                break;
            case R.id.button2:
                //stopService(new Intent(getBaseContext(), Service.class));
                db.delete();
                break;
        }
    }

    public double calculateDistance(double levelInDb, double freqInMHz)    {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }
}
