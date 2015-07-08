package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 06/06/2015
 * within project KidsCode.
 */
public class Kid {

    static public final int SHOWCASE_NAME_ONLY = 1;
    static public final int SHOWCASE_NAME_AND_COINS = 2;

    public String id;
    public String name;
    public String mobile;
    public Session thisSession;
    char ageGroup;

    public Kid(String id, String name, String mobile) {
        this.name = name;
        this.id = id;
        this.mobile = mobile;
        this.thisSession = new Session(0, new Date());
    }

    public Kid() {
    }

    public String getKidForDB() {
        char a[] = {'\t',ageGroup};
        String s = new String(a);
        s+= id + "@" + name + "@" + mobile;
        return  s ;
    }
    @SuppressLint("SimpleDateFormat")
    public String getThisSessionForDB() {
        String dateStr = new SimpleDateFormat("ddMMyy").format(thisSession.date);
        return '\t' + id + "#" + thisSession.coin + "@" + dateStr;
    }



    public String toString(int SHOWMODE) {
        switch (SHOWMODE) {
            case Kid.SHOWCASE_NAME_AND_COINS:
                return this.name + "   " + this.thisSession.coin;
            default:    // Kid.SHOWCASE_NAME_ONLY
                return this.name;
        }
    }


}
