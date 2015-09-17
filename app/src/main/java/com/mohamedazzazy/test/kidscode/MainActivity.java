package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mohamedazzazy.test.kidscode.java.DB;
import com.mohamedazzazy.test.kidscode.java.Rearrange;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //done on back pressed check
    //done age group in stat
    // DONE new kid in main
    //done rearrange after new kid
//done checking and completing the new feature of ALL

    /*  done add calling feature
    done adding all option in age chooser
    done change attlisttaker in DB to suit it
    done stat show the specified age only
    done state offer call to all kids shown
    done reading fulllist read only the wnated age
    done fulllist read after changing the agegroup
        by making onSharedPrefrencsChanged
     */

    Intent next;
    int i = 1, a = 1, e = 1,f=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DB.IS_OPENED_BEFORE) {
            finish();
            next = new Intent(getApplicationContext(), ActionsActivity.class);
            startActivity(next);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dec();
    }


    public void dec() {
        DB.mainActivity = this;
        findViewById(R.id.bStat).setOnClickListener(this);
        findViewById(R.id.bStart).setOnClickListener(this);
        DB.MAX_ID = getSharedPreferences(DB.PREFS_FILE, MODE_PRIVATE).getInt("max_id", 32);
        Log.e("TEST_VALUES", "MAX_ID Out : " + DB.MAX_ID);        //test
        DB.AGE_CHAR = PreferenceManager.getDefaultSharedPreferences(this).getString("age_group", "A").charAt(0);

    }

    @Override
    public void onClick(View v) {
        if (Rearrange.IN_ACTION) return;
        switch (v.getId()) {
            case R.id.bStat:
                if (DB.fullList == null) {
                    if(!DB.readFullDBComplete(DB.EXACT))break;
                }
                next = new Intent(getApplicationContext(), ShowCoinActivity.class);
                next.putExtra("From_Actions_Activity", false);
                startActivity(next);
                break;
            case R.id.bStart:
                boolean USE_QR = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("use_qr", false);
                if (DB.readAttDataBase()) {
                    DB.fullList = null;
                    if (USE_QR) {
                        // todo : Start QR Activity
                    } else {
                        finish();
                        next = new Intent(getApplicationContext(), NormalAttActivity.class);
                        startActivity(next);
                    }
                } else {
                    Toast.makeText(this, "No active kids in this age group", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                next = new Intent(this, SettingsActivity.class);
                startActivity(next);
                break;
            case R.id.action_import:
                e = a = 1;
                if (i++ == f) {
                    i = 1;
                    String DEFAULT_PATH = "/storage/emulated/0/Download/KidsCode/MainDB.txt";
                    DB.EXTERNAL_FILE_PATH = PreferenceManager.getDefaultSharedPreferences(this).getString("external_file_path", DEFAULT_PATH);
                    DB.importDataBase();
                } else {
                    Toast.makeText(this, "need " + (f+1 - i) + " more pressing to do that", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_export:
                i = a = 1;
                if (e++ == f) {
                    e = 1;
                    String DEFAULT_PATH = "/storage/emulated/0/Download/KidsCode/MainDB.txt";
                    DB.EXTERNAL_FILE_PATH = PreferenceManager.getDefaultSharedPreferences(this).getString("external_file_path", DEFAULT_PATH);
                    DB.exportDataBase();
                } else {
                    Toast.makeText(this, "need " + (f+1 - e) + " more pressing to do that", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_rearrange:
                i = e = 1;
                if (a++ == f) {
                    a = 1;
                    next = new Intent(getApplicationContext(), Rearrange.class);
                    startService(next);
                } else {
                    Toast.makeText(this, "need " + (f+1 - a) + " more pressing to do that", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_addKid_main:
                next = new Intent(getApplicationContext(), NewKidActivity.class);
                next.putExtra("From_Actions_Activity", false);
                startActivity(next);
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    /*
    Intent i = new Intent(getApplicationContext(), NewActivity.class);
    i.putExtra("new_variable_name","value");
    startActivity(i);

    Then in the new Activity, retrieve those values:

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
        String value = extras.getString("new_variable_name");
    }
         */

     /*DATE
     Use SimpleDateFormat#parse() to parse a String in a certain pattern into a Date.

     String oldstring = "2011-01-18 00:00:00.0";
     Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);
     Use SimpleDateFormat#format() to format a Date into a String in a certain pattern.

     String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
     System.out.println(newstring); // 2011-01-18
    */
}
