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
    TextView display;
    Button bstat, bstart, bget;
    Intent next;
    static final String LOG_TAG = MainActivity.class.getSimpleName();
    static final String DIR_NAME = "KidsCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dec();
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
                readDatabase();
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

    @SuppressLint("SimpleDateFormat")
    public ArrayList<Kid> readDatabase() {
        File f2 = new File(getStorageDir(DIR_NAME), "MainDB.txt");
        StringBuilder text = new StringBuilder();
        try {










            BufferedReader br = new BufferedReader(new FileReader(f2));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data;
        String name = null,mobile = null;
        int code = 0 ,coin = 0;
        ArrayList<Kid> kids = null;
        ArrayList<Session> sessions = new ArrayList<>();
        Date date = null;
        data = text.toString();

        if (data.length() > 1) {
            kids = new ArrayList<>();
            for (String kid : data.split("#")) {
                if (kid.length() > 1) {
                    for (String kidTerm : kid.split("@")) {
                        if (kidTerm.length() > 1) {
                            switch (kidTerm.substring(0, 2)) {
                                case "NN":
                                    name = kidTerm.substring(2);
                                    break;
                                case "CC":
                                    code = Integer.parseInt(kidTerm.substring(2));
                                    break;
                                case "MM":
                                    mobile = kidTerm.substring(2);
                                    break;
                                case "AT":
                                    for (String kidSession : kidTerm.substring(2).split("SS")) {
                                        if (kidSession.length() > 1) {
                                            for (String sessionTerm : kidSession.split("%")) {
                                                if (sessionTerm.length() > 1) {
                                                    switch (sessionTerm.substring(0, 2)) {
                                                        case "SC":
                                                            coin = Integer.parseInt(sessionTerm.substring(2));
                                                            break;
                                                        case "SD":
                                                            try {
                                                                date = new SimpleDateFormat("dd-MM-yyyy").parse(sessionTerm.substring(2));
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                    }
                                                }
                                            }
                                            sessions.add(new Session(coin, date));

                                        }
                                    }
                            }
                        }
                    }
                    kids.add(new Kid(name, code, mobile, sessions));
                }
            }
        }

        return kids;
    }

    public File getStorageDir(String dirName) {
        // Get the directory for the user's public pictures directory.
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

     /*      ///DATA FORMAT
        "#@NN<Name Of Kid>@CC<his code>@MM<His Mobile>@ATSS%SC<coin taken>%SD<date dd-mm-yyyy>SS%SC<coin taken>%SD<date dd-mm-yyyy>"
        */
}
