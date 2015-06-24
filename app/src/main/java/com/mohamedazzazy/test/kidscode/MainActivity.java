package com.mohamedazzazy.test.kidscode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mohamedazzazy.test.kidscode.java.Kid;
import com.mohamedazzazy.test.kidscode.java.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String data;
    TextView display;
    Button bstat, bstart, bget;
    Intent next;
    ArrayList<Kid> fullList, attList;
    char ageChar; /// The age of kids you'r looking for [O,L,M]
    static final String LOG_TAG = MainActivity.class.getSimpleName();
    static final String DIR_NAME = "KidsCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dec();
        readFullDatabase();
    }

    public void dec() {
        bstat = (Button) findViewById(R.id.bStat);
        bstart = (Button) findViewById(R.id.bStart);
        bget = (Button) findViewById(R.id.bGet);
        bget.setOnClickListener(this);
        bstart.setOnClickListener(this);
        bstat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bStat:
                next = new Intent(getApplicationContext(), StatActivity.class);
                break;
            case R.id.bStart:
                next = new Intent(getApplicationContext(), AttActivity.class);
                break;
            case R.id.bGet:
                readFullDatabase();
                break;
        }
        startActivity(next);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAttDataBase() {
        String name = null, mobile = null, code = null;
        readTheFile();

        /*
         ////////////////////// TEST real number of dataBase :
         // (400 kid) takes around ?? seconds to calculate it.
         // (800 kid) takes around ?? seconds to calculate it.
         // (1200 kid) takes around ?? seconds to calculate it.
         //////////// Time measuring
         long stime = System.currentTimeMillis();
         data = "#O@CC1141512613254@NNSherif Ahmed Ali Mohamed@MM01091178126@ATSS%SC6%SD15062015SS%SC8%SD18072014";
         data = data + data + data + data + data + data + data + data + data + data;
         data = data + data + data + data + data + data + data + data + data + data;
         data = data + data + data + data + data ;
         ///////////////////////////////////////////////////////////
         */
        if (data.length() > 0) {
            attList = new ArrayList<>();
            data = data.substring(1);
            for (String kid : data.split("#")) {
                if (kid.charAt(0) == ageChar) {
                    kid = kid.substring(2);
                    for (String kidTerm : kid.split("@")) {
                        switch (kidTerm.substring(0, 2)) {
                            case "NN":
                                name = kidTerm.substring(2);
                                break;
                            case "CC":
                                code = kidTerm.substring(2);
                                break;
                            case "MM":
                                mobile = kidTerm.substring(2);
                        }
                    }
                    attList.add(new Kid(name, code, mobile));
                }
            }
        }

         //////////// Time measuring
//         stime = System.currentTimeMillis() - stime;
    }

    @SuppressLint("SimpleDateFormat")
    public void readFullDatabase() {      /// only usefull with statistics, not with Att.
        String name = null, mobile = null, code = null;
        int coin = 0;
        Date date = null;
        ArrayList<Session> sessions = null;
        readTheFile();

        /*
         ////////////////////// TEST real number of dataBase :
         // (1000 kid with 15 sessions) takes around 5 seconds to calculate it.
         // (500 kid with 45 sessions) takes around 6 seconds to calculate it.
         // (1000 kid with 60 sessions) takes around 17 seconds to calculate it.
         // (500 kid with 15 sessions) takes around 2 seconds to calculate it.
         //////////// Time measuring
         long stime = System.currentTimeMillis();
         String Ss = "SS%SC10%SD02032014";
         data = "#O@CC1141512613254@NNSherif Ahmed Ali Mohamed@MM01091178126@ATSS%SC6%SD15062015SS%SC8%SD18072014";
         data += Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss;
         // data += Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss;
         // data += Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss;
         // data += Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss + Ss;
         data = data + data + data + data + data + data + data + data + data + data;
         data = data + data + data + data + data + data + data + data + data + data;
         data = data + data + data + data + data ;
         ///////////////////////////////////////////////////////////
         */
        /*      ////////////////DATA FORMAT \\\\\\\\\\\\\\\\\
         "#<O,M,L>@CC<his code>@NN<Name Of Kid>@MM<His Mobile>@ATSS%SC<coin taken>%SD<date ddmmyyyy>SS%SC<coin taken>%SD<date ddmmyyyy>"
         */
        if (data.length() > 1) {
            fullList = new ArrayList<>();
            data = data.substring(1);
            for (String kid : data.split("#")) {
                ageChar = kid.charAt(0);
                kid = kid.substring(2);
                for (String kidTerm : kid.split("@")) {
                    switch (kidTerm.substring(0, 2)) {
                        case "NN":
                            name = kidTerm.substring(2);
                            break;
                        case "CC":
                            code = kidTerm.substring(2);
                            break;
                        case "MM":
                            mobile = kidTerm.substring(2);
                            break;
                        case "AT":
                            sessions = new ArrayList<>();
                            kidTerm = kidTerm.substring(2);
                            for (String kidSession : kidTerm.substring(2).split("SS")) {
                                kidSession = kidSession.substring(1);
                                for (String sessionTerm : kidSession.split("%")) {
                                    switch (sessionTerm.substring(0, 2)) {
                                        case "SC":
                                            coin = Integer.parseInt(sessionTerm.substring(2));
                                            break;
                                        case "SD":
                                            try {
                                                date = new SimpleDateFormat("ddMMyyyy").parse(sessionTerm.substring(2));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                    }
                                }
                                sessions.add(new Session(coin, date));
                            }
                    }
                }
                fullList.add(new Kid(name, code, mobile, sessions, ageChar));
            }
        }
        /*
         //////////// Time measuring
         stime = System.currentTimeMillis() - stime;
         */
    }

    public void readTheFile() {
        File thefile = new File(getStorageDir(DIR_NAME), "MainDB.txt");
        StringBuilder rawText = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(thefile));
            String line;
            while ((line = br.readLine()) != null) {
                rawText.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = rawText.toString();
    }

    public File getStorageDir(String dirName) {
        // Get the directory for the user's public downloads directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), dirName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /* DATE
     Use SimpleDateFormat#parse() to parse a String in a certain pattern into a Date.

     String oldstring = "2011-01-18 00:00:00.0";
     Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);
     Use SimpleDateFormat#format() to format a Date into a String in a certain pattern.

     String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
     System.out.println(newstring); // 2011-01-18

     */
}
