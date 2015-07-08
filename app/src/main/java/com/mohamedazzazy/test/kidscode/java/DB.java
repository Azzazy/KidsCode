package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mohamedazzazy.test.kidscode.ActionsActivity;
import com.mohamedazzazy.test.kidscode.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 27/06/2015 within KidsCode
 */
public class DB extends Service {
    public static boolean IS_OPENED_BEFORE;
    volatile boolean CONT_THREAD = true;
    static boolean READ_KIDS_ONLY = true, READ_ALL = false;
    static public ArrayList<Kid> attList;
    static public ArrayList<Kid> newKidsList;
    static public ArrayList<FullKid> fullList;
    public static String EXTERNAL_FILE_PATH;
    static String INTERNAL_FILE_NAME = "MainDB.txt", data, END_TIME_FIXED;
    static int DB_VERSION = 1, END_TIME;
    public static Activity a;

    public static int arrangeDataBase() {
        readFullDatabase();
        data = "";
        int n = fullList.size();
        addFullKidsToData();
        addFullSessionsToData();
        writeInternalFile(Context.MODE_PRIVATE);
        return n;
    }

    public static void appendData() {
        data = "";
        addNewKidsToData();
        addAttToData();
        writeInternalFile(Context.MODE_APPEND);
    }

    public static void importDataBase() {
        if (readExternalFile() && isRealDataBase()) {
            removeHeader();
            writeInternalFile(Context.MODE_PRIVATE);
            Toast.makeText(a, "Import finished", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(a, "File is missing or not a valid one", Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportDataBase() {
        readInternalFile(READ_ALL);
        addHeader();
        writeExternalFile();
        Toast.makeText(a, "Export finished", Toast.LENGTH_SHORT).show();
    }

    public static boolean getAttDataBase(char ageChar) {
        if (!readInternalFile(READ_KIDS_ONLY)) return false;
        attList = new ArrayList<>();
        for (String kidStr : data.split("\t")) {
            if (kidStr.charAt(0) == ageChar) {
                String kidTermStr[] = kidStr.split("@");
                attList.add(new Kid(kidTermStr[0].substring(1), kidTermStr[1], kidTermStr[2]));
            }
        }
        data = null;
        return (attList.size() > 0);
    }

    public static boolean readFullDatabase() {
        if (!readInternalFile(READ_ALL)) return false;
        fullList = new ArrayList<>();
        for (String dataParts : data.split("\n")) {
            for (String dataPartStr : dataParts.split("\t")) {
                if (dataPartStr.length() == 0) continue;
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
        data = null;
        return (fullList.size() > 0);
    }

    public static int findByIdInAtt(String id) {
        for (int i = 0; i < DB.attList.size(); i++) {
            if (DB.attList.get(i).id.equals(id))
                return i;
        }
        return -1;
    }

    public static ArrayAdapter<String> getAdapterOfAtt(int SHOWCASE,
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
            k[i++] = x.toString(SHOWCASE);
        }
        return new ArrayAdapter<>(a,
                android.R.layout.simple_list_item_1, k);
    }


    static boolean isKid(char c) {
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

    static int findByIdInFull(String id) {
        for (int i = 0; i < fullList.size(); i++) {
            if (fullList.get(i).id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static boolean writeInternalFile(int MODE) {
        try {
            FileOutputStream fos;
            fos = a.openFileOutput(INTERNAL_FILE_NAME, MODE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean writeExternalFile() {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(EXTERNAL_FILE_PATH));
            out.write(data);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(a, new String[]{EXTERNAL_FILE_PATH}, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean readExternalFile() {
        StringBuilder rawText;

        try {
            BufferedReader br = new BufferedReader(new FileReader(EXTERNAL_FILE_PATH));
            String line;
            rawText = new StringBuilder();
            while ((line = br.readLine()) != null) {
                rawText.append(line).append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        data = rawText.toString();
        data = data.substring(0, data.length() - 1);
        return true;
    }

    public static boolean readInternalFile(boolean READ_CONDITION) {
        data = "";
        try {
            FileInputStream fis = a.openFileInput(INTERNAL_FILE_NAME);
            int c;
            while ((c = fis.read()) != -1) {
                if (c == '\n' && READ_CONDITION) break;
                data += (char) c;
            }
            fis.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean isRealDataBase() {
        return data.length() > 0 && data.startsWith(dataBaseHeader());
    }

    static String dataBaseHeader() {
        return "KidsCode DataBase File" + " v" + DB_VERSION;
    }

    static void removeHeader() {
        data = data.substring(data.indexOf('\n') + 2);//2 for '\n' and '\t'
    }

    static void addHeader() {
        data = dataBaseHeader() + "\n\t" + data;
    }

    static boolean addNewKidsToData() {
        if (newKidsList == null || newKidsList.size() == 0)        //check for new kids if so add them to data
            return false;

        data += '\n';
        for (Kid k : newKidsList) {
            data += k.getKidForDB();
        }
        newKidsList.clear();

        return true;
    }

    static void addFullKidsToData() {
        data += '\n';
        for (FullKid k : fullList) {
            String s = k.getKidForDB();
            data += s;
        }
        data = data.substring(2);//2 for '\n' and '\t'
    }

    static boolean addAttToData() {
        if (attList == null || attList.size() == 0) return false;

        data += '\n';       //add the current attList info to data
        for (Kid k : attList) {
            data += k.getThisSessionForDB();
        }

        return true;
    }

    static void addFullSessionsToData() {
        data += '\n';
        for (FullKid k : fullList) {
            data += k.getAllSessionsForDB();
        }
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
                                e.printStackTrace();
                            }
                        }
//                        mBuilder.setContentText("Session Ended")
//                                .setProgress(0, 0, false);
//                        mNotificationManager.notify(mId, mBuilder.build());
                        mNotificationManager.cancel(mId);
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        CONT_THREAD = false;
        super.onDestroy();
    }


}
    /*      ////////////////DATA FORMAT \\\\\\\\\\\\\\\\\
     "<O,M,L>@<id>@<Name Of Kid>@MM<His Mobile>\n\t<id>#<coin taken>@<date ddmmyy>#<coin taken>@<date ddmmyy>"
    */
    /*
       data = "KidsCode DataBase File v1\n\tO542132635@Ali Mohamed Mohamed@01091178126\to9451635@Ismail Ahmed Mustafa@011365415\tO3264964@Amgd Hamouda Ibrahim@012541116
                 \tM43243544532@soha ahmed mohamed@0123645489\n
                 \t542132635#5@120315#2@180315#6@220315\t9451635#3@070415\t43243544532#7@221115\n
                 \tL345546364@Assad Ibrahim Ali@0106321456\t345546364#4@260115";
    */