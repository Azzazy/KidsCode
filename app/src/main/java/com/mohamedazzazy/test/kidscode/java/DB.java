package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.mohamedazzazy.test.kidscode.ActionsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 27/06/2015 withen KidsCode
 */
public class DB {

    static final String LOG_TAG = DB.class.getSimpleName();
    static final String DIR_NAME = "KidsCode";

    static public ArrayList<Kid> attList, fullList;
    static String data;

    /*      ////////////////DATA FORMAT \\\\\\\\\\\\\\\\\
     "#<O,M,L>@CC<his code>@NN<Name Of Kid>@MM<His Mobile>@ATSS%SC<coin taken>%SD<date ddmmyyyy>SS%SC<coin taken>%SD<date ddmmyyyy>"
     */
    static public ArrayAdapter<String> getAdapterOfAtt(Activity a, int SHOWMODE, boolean FIRST_NULL) {
        int i = 0;
        String k[];
        if (FIRST_NULL) {
            k = new String[DB.attList.size() + 1];
            k[i++] = "Chose a kid";
        } else {
            k = new String[DB.attList.size()];
        }
        for (Kid x : DB.attList) {
            k[i++] = x.toString(SHOWMODE);
        }

        return new ArrayAdapter<String>(a,
                android.R.layout.simple_list_item_1, k);
    }

    static public int findInAtt(Kid k) {
        for (int i = 0; i < DB.attList.size(); i++) {
            if (k.code.equals(DB.attList.get(i).code))
                return i;
        }
        return -1;
    }

    static public void getAttDataBase(char ageChar) {
        String name = null, mobile = null, code = null;
        readTheFile();
        // data = "#O@CC542132635@NNAli Mohamed Mohamed@MM01091178126\n#O@CC9451635@NNIsmail Ahmed Mustafa@MM011365415\n#O@CC3264964@NNAmgd Hamouda Ibrahim@MM012541116\n#M@CC43243544532@NNsoha ahmed mohamed@MM0123645489";
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
                                mobile = kidTerm.substring(2, kidTerm.length() - 1);
                        }
                    }
                    attList.add(new Kid(name, code, mobile));
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    static public void readFullDatabase() {
        String name = null, mobile = null, code = null;
        int coin = 0;
        char ageChar;
        Date date = null;
        ArrayList<Session> sessions = null;
        readTheFile();

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
    }

    static public void readTheFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), DIR_NAME);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        File thefile = new File(file, "MainDB.txt");
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

}
