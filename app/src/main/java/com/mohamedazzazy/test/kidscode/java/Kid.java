package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 06/06/2015
 * within project KidsCode.
 */
public class Kid {

    static public final int SHOWCASE_NAME_ONLY = 1;
    static public final int SHOWCASE_NAME_COINS = 2;
    static public final int SHOWCASE_ACTIVE_NAME_COINS = 3;

    public String id;
    public String name;
    public String mobile;
    public Session thisSession;
    public boolean active;
    ArrayList<Session> sessions;

    public enum AgeGroup {Old, Middle, Little}

    AgeGroup ageGroup;

    public Kid(String id, String name, String mobile) {
        this.name = name;
        this.id = id;
        this.mobile = (mobile.equals("un")) ? null : mobile;
        this.thisSession = new Session(0, new Date());
    }

    public Kid(String id, String name, String mobile, char ageChar) {
        this.active = isActive(ageChar);
        this.ageGroup = getAgeGroup(ageChar);
        this.name = name;
        this.id = id;
        this.mobile = (mobile.equals("un")) ? null : mobile;
        this.sessions = new ArrayList<>();
    }


    public String getKidForDB() {
        String s;
        if (!active) {
            s = "\t" + ageGroup.toString().toLowerCase().charAt(0) + "";
        } else {
            s = "\t" + ageGroup.toString().charAt(0) + "";
        }

        s += id + "@" + name + "@" + ((mobile == null) ? "un" : mobile);
        return s;
    }

   public static AgeGroup getAgeGroup(char c) {
        switch (c) {
            case 'O':
            case 'o':
                return AgeGroup.Old;
            case 'M':
            case 'm':
                return AgeGroup.Middle;
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


    public String getInfo(int SHOWMODE) {
        switch (SHOWMODE) {
            case Kid.SHOWCASE_NAME_COINS:
                return name + "   " + getTotalCoins();
            case Kid.SHOWCASE_NAME_ONLY:
                return name;
            case Kid.SHOWCASE_ACTIVE_NAME_COINS:
                return (active ? "" : "~") + name + "   " + getTotalCoins();
            default:
                return "";
        }
    }

    static boolean isActive(char c) {
        switch (c) {
            case 'O':
            case 'M':
            case 'L':
                return true;
            default:
                return false;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getAllSessionsForDB() {
        String all = '\t' + this.id;
        for (Session s : this.sessions) {
            String dateStr = new SimpleDateFormat("ddMMyy").format(s.date);
            all += "#" + s.coin + "@" + dateStr;
        }
        return all;
    }

    public int getTotalCoins() {
        if (thisSession == null) {
            int sum = 0;
            for (Session s : sessions) {
                sum += s.coin;
            }
            return sum;
        } else {
            return thisSession.coin;
        }
    }

    @Override
    public String toString() {
        return id;
    }

}
