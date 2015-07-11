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

    public enum AgeGroup {Old, Meddle, Little }

    AgeGroup ageGroup;

    public Kid(String id, String name, String mobile) {
        this.name = name;
        this.id = id;
        this.mobile = (mobile.equals("un")) ? null : mobile;
        this.thisSession = new Session(0, new Date());
    }

    public Kid() {
    }

    public String getKidForDB() {
        char a[] = {'\t', ageGroup.toString().charAt(0)};
        String s = new String(a);
        s += id + "@" + name + "@" + ((mobile == null) ? "un" : mobile);
        return s;
    }

    public static AgeGroup getAgeGroupfromChar(char c) {
        switch (c) {
            case 'O':
            case 'o':
                return AgeGroup.Old;
            case 'M':
            case 'm':
                return AgeGroup.Meddle;
            case 'L':
            case 'l':
                return AgeGroup.Little;
            default:
                return null;
        }
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
