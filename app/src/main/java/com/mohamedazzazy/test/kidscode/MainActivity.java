package com.mohamedazzazy.test.kidscode;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mohamedazzazy.test.kidscode.java.DB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // TODO: need to modify the full record after finishing the session.

    Intent next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DB.IS_OPENNED_BEFORE) {
            finish();
            next = new Intent(getApplicationContext(), ActionsActivity.class);
            startActivity(next);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dec();
//        DB.appendData();
    }


    public void dec() {
        findViewById(R.id.bStat).setOnClickListener(this);
        findViewById(R.id.bStart).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bStat:
                next = new Intent(getApplicationContext(), StatActivity.class);
                startActivity(next);
                break;
            case R.id.bStart:
                char ageChar= PreferenceManager.getDefaultSharedPreferences(this).getString("age_group", "O").charAt(0);
                boolean USE_QR = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("use_qr", false);
                if (DB.getAttDataBase(ageChar)) {
                    if (USE_QR) {
                       // TODO : Start QR Activity
                    }else{
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
        if (id == R.id.action_settings) {
            next = new Intent(this, SettingsActivity.class);
            startActivity(next);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
