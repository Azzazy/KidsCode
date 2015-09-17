package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mohamedazzazy.test.kidscode.ActionsActivity;
import com.mohamedazzazy.test.kidscode.MainActivity;
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
    public static boolean IS_OPENED_BEFORE, NEED_REWRITE = false;
    volatile boolean CONT_THREAD = true;
    static boolean READ_KIDS_ONLY = true, READ_ALL = false;
    static public ArrayList<Kid> newKidsList, fullList, attList, theList;
    public static String EXTERNAL_FILE_PATH, PREFS_FILE = "ThePrefs";
    static String INTERNAL_FILE_NAME = "MainDB.txt", data, END_TIME_FIXED;
    static int DB_VERSION = 1, END_TIME;
    public static int MAX_ID, ALL = 1, EXACT = 2, CALL_COUNTER;
    public static char AGE_CHAR;
    public static MainActivity mainActivity;
    public static boolean CALL_ACTIVE = true;

    public static void rewriteFullDB() {
        if (NEED_REWRITE) {
            data = "";
            addFullKidsToData();
            addFullSessionsToData();
            writeInternalFile(Context.MODE_PRIVATE);
            NEED_REWRITE = false;
            fullList = null;
        }
    }

    public static void addNewKidFromMain(Kid k) {
        readFullDBComplete(ALL);
        fullList.add(k);
        NEED_REWRITE = true;
        rewriteFullDB();
    }

    public static int arrangeDataBase() {
        readFullDBComplete(ALL);
        data = "";
        int n = fullList.size();
        addFullKidsToData();
        addFullSessionsToData();
        writeInternalFile(Context.MODE_PRIVATE);
        fullList = null;
        return n;
    }

    public static void updateMaxID() {
        SharedPreferences.Editor editor = mainActivity.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).edit();
        editor.putInt("max_id", DB.MAX_ID);
        Log.e("TEST_VALUES", "MAX_ID In : " + DB.MAX_ID);        //test
        editor.apply();
    }

    public static void updateCallCounter() {
        SharedPreferences.Editor editor = mainActivity.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).edit();
        editor.putInt("call_count", DB.CALL_COUNTER);
        Log.e("TEST_VALUES", "CALL_COUNTER In : " + DB.CALL_COUNTER);        //test
        editor.apply();
    }

    public static void getCallCounter() {
        CALL_COUNTER = mainActivity.getSharedPreferences(DB.PREFS_FILE, MODE_PRIVATE).getInt("call_count", 0);

    }

    public static void updateCallActive() {
        SharedPreferences.Editor editor = mainActivity.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).edit();
        editor.putBoolean("call_active", DB.CALL_ACTIVE);
        Log.e("TEST_VALUES", "CALL_ACTIVE In : " + DB.CALL_COUNTER);        //test
        editor.apply();
    }

    public static void getCallActive() {
        CALL_ACTIVE = mainActivity.getSharedPreferences(DB.PREFS_FILE, MODE_PRIVATE).getBoolean("call_active", true);

    }

    public static void appendDataAfterSession() {

        data = "";
        boolean flag = addNewKidsToData();
        addAttToData();
        writeInternalFile(Context.MODE_APPEND);
        if (flag) arrangeDataBase();
    }

    public static void importDataBase() {
        if (readExternalFile() && isRealDataBase()) {
            removeHeader();
            writeInternalFile(Context.MODE_PRIVATE);
            Toast.makeText(mainActivity, "Import finished", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mainActivity, "File is missing or not a valid one", Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportDataBase() {
        readInternalFile(READ_ALL);
        addHeader();
        writeExternalFile();
        Toast.makeText(mainActivity, "Export finished", Toast.LENGTH_SHORT).show();
    }


    public static boolean readAttDataBase() {
        if (!readInternalFile(READ_KIDS_ONLY)) return false;
        attList = new ArrayList<>();
        for (String kidStr : data.split("\t")) {
            if (AGE_CHAR == 'A' || kidStr.charAt(0) == AGE_CHAR) {
                String kidTermStr[] = kidStr.split("@");
                attList.add(new Kid(kidTermStr[0].substring(1), kidTermStr[1], kidTermStr[2]));
            }
        }
        data = "";
        return (attList.size() > 0);
    }

    public static void readFullDBInService(Context a) {
        Intent theServiceTntent = new Intent(a, ReadFullDB.class);
        a.startService(theServiceTntent);
    }
    public static boolean readFullDBComplete(int MODE){
        Toast.makeText(mainActivity, "Reading the DataBase", Toast.LENGTH_SHORT).show();

        int mId = 7;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mainActivity)
                        .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager mNotificationManager =
                (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!DB.readFullDatabase(MODE)) {
            Toast.makeText(mainActivity, "No kids in the DataBase", Toast.LENGTH_LONG).show();
            mBuilder.setContentTitle("Error!")
                    .setContentText("Problem in reading the DataBase")
                    .setOngoing(false)
                    .setAutoCancel(true);
            mNotificationManager.notify(mId, mBuilder.build());
            return false;
        }else{
            Toast.makeText(mainActivity, "Done Reading", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public static boolean readFullDatabase(int MODE) {
        if (!readInternalFile(READ_ALL)) return false;
        Log.e("TEST", "readFullDatabase ");     //test
        Log.e("TEST", "data : " + data);        //test

        fullList = new ArrayList<>();
        for (String dataParts : data.split("\n")) {
            for (String dataPartStr : dataParts.split("\t")) {
                if (dataPartStr.length() == 0) continue;
                if (isKid(dataPartStr.charAt(0))) {
                    if (MODE == ALL || (MODE == EXACT && (AGE_CHAR == 'A' || dataPartStr.toUpperCase().charAt(0) == AGE_CHAR))) {
                        String kidTermStr[] = dataPartStr.split("@");
                        fullList.add(new Kid(kidTermStr[0].substring(1), kidTermStr[1], kidTermStr[2], kidTermStr[0].charAt(0)));
                    }
                } else {
                    Log.e("TEST", "dataPartStr : " + dataPartStr);        //test
                    int idIndex = findByIdInFull(idFrom(dataPartStr));
                    if (idIndex == -1) continue;
                    Kid k = fullList.get(idIndex);
                    dataPartStr = dataPartStr.substring(dataPartStr.indexOf('#') + 1);
                    for (String onetSesStr : dataPartStr.split("#")) {
                        String sesTermStr[] = onetSesStr.split("@");
                        k.sessions.add(new Session(sesTermStr[0], sesTermStr[1]));
                    }
                    fullList.set(idIndex, k);
                }
            }
        }
        data = "";
        return (fullList.size() > 0);
    }

    public static int findByIdInAtt(String id) {
        for (int i = 0; i < DB.attList.size(); i++) {
            if (DB.attList.get(i).id.equals(id))
                return i;
        }
        return -1;
    }


    private static String idFrom(String s) {// to get id from a session record
        return s.substring(0, s.indexOf('#'));
    }

    public static ArrayAdapter<String> getAdapterOfKidsInAtt(Kid.SHOWCASE mode,
                                                             boolean FIRST_NULL) {
        int i = 0;
        String k[];
        if (FIRST_NULL) {
            k = new String[DB.attList.size() + 1];
            k[i++] = "All";
        } else {
            k = new String[DB.attList.size()];
        }
        for (Kid x : DB.attList) {
            k[i++] = x.getInfo(mode);
        }
        return new ArrayAdapter<>(mainActivity,
                android.R.layout.simple_list_item_1, k);
    }


    public static ArrayAdapter<String> getAdapterOfKidsInFull(Kid.SHOWCASE mode) {
        int i = 0;
        String k[] = new String[DB.fullList.size()];
        for (Kid x : DB.fullList) {
            k[i++] = x.getInfo(mode);
        }
        return new ArrayAdapter<>(mainActivity,
                android.R.layout.simple_list_item_1, k);
    }

    public static ArrayAdapter<String> getAdapterOfSessions(int INDEX) {
        int i = 0;
        ArrayList<Session> s = fullList.get(INDEX).sessions;
        String k[];
        k = new String[s.size()];
        for (Session x : s) {
            k[i++] = x.getSession();
        }
        return new ArrayAdapter<>(mainActivity,
                android.R.layout.simple_list_item_1, k);
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

    static int findByIdInFull(String id) {
        for (int i = 0; i < fullList.size(); i++) {
            if (fullList.get(i).id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static boolean writeInternalFile(int mode) {
        try {
            FileOutputStream fos;
            fos = mainActivity.openFileOutput(INTERNAL_FILE_NAME, mode);
            fos.write(data.getBytes());
            fos.close();
            data = "";
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean writeExternalFile() {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(EXTERNAL_FILE_PATH));
            out.write(data);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(mainActivity, new String[]{EXTERNAL_FILE_PATH}, null, null);
            data = "";
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
        Log.e("TEST_READ", "readExternalFile :" + data);            //test
        return true;
    }

    public static boolean readInternalFile(boolean READ_CONDITION) {
        data = "";
        try {
            FileInputStream fis = mainActivity.openFileInput(INTERNAL_FILE_NAME);
            int c;
            while ((c = fis.read()) != -1) {
                if (c == '\n' && READ_CONDITION) break;
                data += (char) c;
            }
            fis.close();
            Log.e("TEST_READ", "readInternalFile :" + data);            //test
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
        newKidsList = null;

        return true;
    }

    static void addFullKidsToData() {
        data += '\n';
        for (Kid k : fullList) {
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
        for (Kid k : fullList) {
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