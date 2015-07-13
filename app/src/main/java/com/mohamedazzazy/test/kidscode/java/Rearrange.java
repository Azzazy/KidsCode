package com.mohamedazzazy.test.kidscode.java;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mohamedazzazy.test.kidscode.R;

/**
 * Created by Mohamed Azzazy on 07/07/2015
 * within project KidsCode.
 */
public class Rearrange extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int mId = 6;
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(Rearrange.this)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Rearranging DataBase")
                                        .setContentText("....")
                                        .setOngoing(true)
                                        .setAutoCancel(false);

                        startForeground(mId, mBuilder.build());

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        int n = DB.arrangeDataBase(getApplicationContext());
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        stopForeground(true);
                        mBuilder.setContentTitle("Task Finished")
                                .setContentText( n + " Kids have been rearranged correctly")
                                .setOngoing(false)
                                .setAutoCancel(true);
                        mNotificationManager.notify(mId, mBuilder.build());
                        stopSelf();
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
