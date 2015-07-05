package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.mohamedazzazy.test.kidscode.ActionsActivity;
import com.mohamedazzazy.test.kidscode.R;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 27/06/2015 within KidsCode
 */
public class DB extends Service {
    public static boolean IS_OPENNED_BEFORE;
    volatile boolean CONT_THREAD = true;
    int END_TIME;
    String END_TIME_FIXED;
    static boolean READ_KIDS_ONLY = true;
    static boolean READ_ALL = false;

    static final String LOG_TAG = DB.class.getSimpleName();
    static final String DIR_NAME = "KidsCode";

    static public ArrayList<Kid> attList;
    static public ArrayList<FullKid> fullList;
    static String data;


    static public void appendData() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), DIR_NAME);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        File thefile = new File(file, "MainDB.txt");

        String string = "\n" +
                "\tO542132635@Ali Mohamed Mohamed@01091178126\n" +
                "\to9451635@Ismail Ahmed Mustafa@011365415\n" +
                "\tO3264964@Amgd Hamouda Ibrahim@012541116\n" +
                "\tM43243544532@soha ahmed mohamed@0123645489\n" +
                "\t542132635#5@120315#2@180315#6@220315\n" +
                "\t9451635#3@070415\t43243544532#7@221115\n" +
                "\tL345546364@Assad Ibrahim Ali@0106321456\n" +
                "\t345546364#4@260115\n" +
                "\t34554622364#4@260115\n" +
                "\t542132635#5@120315#2@180315#6@220315\n" +
                "\t9451635#3@070415\t43243544532#7@221115\n" +
                "\t345546364#4@260115\n" +
                "\t34554622364#4@260115\t9451635#9@0909415";
        String test = "Hello world!";
        OutputStream out ;
        try {
            out = new BufferedOutputStream(new FileOutputStream(thefile, false));
            out.write(string.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    static public void readTheFile(boolean READ_CONDITION) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), DIR_NAME);
        /*if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }*/
        file = new File(file, "MainDB.txt");
        StringBuilder rawText = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            if (READ_CONDITION) {
                br.readLine();
                line = br.readLine();
                rawText.append(line);

            } else {
                while ((line = br.readLine()) != null) {
                    rawText.append(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = rawText.toString();
    }


    /*      ////////////////DATA FORMAT \\\\\\\\\\\\\\\\\TODO this is not correct
     "#<O,M,L>@CC<his id>@NN<Name Of Kid>@MM<His Mobile>@ATSS%SC<coin taken>%SD<date ddmmyyyy>SS%SC<coin taken>%SD<date ddmmyyyy>"
     */
    /*
       data = "\n\tO542132635@Ali Mohamed Mohamed@01091178126\to9451635@Ismail Ahmed Mustafa@011365415\tO3264964@Amgd Hamouda Ibrahim@012541116
                 \tM43243544532@soha ahmed mohamed@0123645489\n
                 \t542132635#5@120315#2@180315#6@220315\t9451635#3@070415\t43243544532#7@221115\n
                 \tL345546364@Assad Ibrahim Ali@0106321456\t345546364#4@260115";
       */

    static public boolean getAttDataBase(char ageChar) {
        readTheFile(READ_KIDS_ONLY);
        if (data != null) {
            attList = new ArrayList<>();
            data = data.substring(1);
            for (String kidStr : data.split("\t")) {
                if (kidStr.charAt(0) == ageChar) {
                    String kidTermStr[] = kidStr.split("@");
                    attList.add(new Kid(kidTermStr[0].substring(1), kidTermStr[1], kidTermStr[2]));
                }
            }
        }
        return (attList.size()>0);
    }

    static public void readFullDatabase() {
        readTheFile(READ_ALL);
        /*data = "\n\tO542132635@Ali Mohamed Mohamed@01091178126\n\to9451635@Ismail Ahmed Mustafa@011365415\n" +
                "\tO3264964@Amgd Hamouda Ibrahim@012541116" +
                "\n" +
                "\tM43243544532@soha ahmed mohamed@0123645489\n" +
                "\t542132635#5@120315#2@180315#6@220315\t9451635#3@070415\t43243544532#7@221115\n" +
                "\tL345546364@Assad Ibrahim Ali@0106321456\n" +
                "\t345546364#4@260115\t34554622364#4@260115\t9451635#9@260115";*/
        if (data != null) {
            fullList = new ArrayList<>();
            data = data.substring(1);
            for (String dataParts : data.split("\n")) {
                dataParts = dataParts.substring(1);
                for (String dataPartStr : dataParts.split("\t")) {   // Add the kids and the sessions
                    if (isKid(dataPartStr.charAt(0))) {
                        String kidTermStr[] = dataPartStr.split("@");
                        fullList.add(new FullKid(kidTermStr[0].substring(1), kidTermStr[1], kidTermStr[2], kidTermStr[0].charAt(0)));
                    } else {
                        int idIndex = findByIdInFull(dataPartStr.substring(0, dataPartStr.indexOf('#')));
                        if (idIndex == -1) continue;
                        FullKid fk = fullList.get(idIndex);
                        dataPartStr = dataPartStr.substring(dataPartStr.indexOf('#') + 1);
                        for (String onetSesStr : dataPartStr.split("#")) {
                            String sesTermStr[] = onetSesStr.split("@");
                            fk.sessions.add(new Session(sesTermStr[0], sesTermStr[1]));
                        }
                        fullList.set(idIndex, fk);
                    }
                }
            }
        }
    }

    private static int findByIdInFull(String id) {
        for (int i = 0; i < fullList.size(); i++) {
            if (fullList.get(i).id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static public int findByIdInAtt(String id) {
        for (int i = 0; i < DB.attList.size(); i++) {
            if (DB.attList.get(i).id.equals(id))
                return i;
        }
        return -1;
    }

    private static boolean isKid(char c) {
        switch (c) {
            case 'O':
            case 'M':
            case 'L':
            case 'o':
            case 'm':
            case 'l':
                return true;
            default:
                return false;
        }
    }


    static public ArrayAdapter<String> getAdapterOfAtt(Activity a, int SHOWMODE,
                                                       boolean FIRST_NULL) {
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
        return new ArrayAdapter<>(a,
                android.R.layout.simple_list_item_1, k);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    void getDate() {
        END_TIME_FIXED = PreferenceManager.getDefaultSharedPreferences(this).getString("end_time", "11:30");
        try {
            Date date = new Date();
            String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
            date = new SimpleDateFormat("yyyy-MM-ddHH:mm").parse(newstring + END_TIME_FIXED);
            END_TIME = (int) ((date.getTime() - System.currentTimeMillis()) / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getDate();
        final int mId = 5;
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("OnGoing Session")
                        .setOngoing(true)
                        .setAutoCancel(false);

        Intent notificationIntent = new Intent(this, ActionsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        mBuilder.setContentIntent(pendingIntent);

        startForeground(mId, mBuilder.build());
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= END_TIME && CONT_THREAD; i++) {
                            String timeStr = Integer.toString((END_TIME - i) / 3600);
                            timeStr += ":" + Integer.toString(((END_TIME - i) % 3600) / 60);
                            timeStr += ":" + Integer.toString((END_TIME - i) % 60);
                            mBuilder.setContentText(timeStr)
                                    .setProgress(END_TIME, i, false);
                            mNotificationManager.notify(mId, mBuilder.build());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d(LOG_TAG, "sleep failure");
                            }
                        }
                        mBuilder.setContentText("Session Ended")
                                .setProgress(0, 0, false);
                        mNotificationManager.notify(mId, mBuilder.build());
                        mNotificationManager.cancel(mId);
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
//        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        CONT_THREAD = false;
        super.onDestroy();
    }


}
