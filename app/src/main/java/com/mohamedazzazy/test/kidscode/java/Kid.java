package com.mohamedazzazy.test.kidscode.java;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 06/06/2015.
 */
public class Kid {

    static final int SHOWMODE_NAME_ONLY = 1;
    static final int SHOWMODE_NAME_AND_COINS = 2;

    public String name;
   public String code;
    String mobile;
    char ageGroup;
    boolean active;
   public ArrayList<Session> sessions;

    public Kid(String name, String code, String mobile, ArrayList<Session> sessions, char age) {
        this.name = name;
        this.code = code;
        this.mobile = mobile;
        this.sessions = sessions;
        this.ageGroup = age;
        active = isActive(age);
    }

    public Kid(String name, String code, String mobile) {
        this.name = name;
        this.code = code;
        this.mobile = mobile;
        this.sessions = new ArrayList<>();
        this.sessions.add(new Session(0, new Date()));
    }

    public Kid() {
    }

    static public boolean isActive(char c) {
        switch (c) {
            case 'O':
            case 'M':
            case 'L':
                return true;
            default:
                return false;
        }
    }

    public String toString(int SHOWMODE) {
        switch (SHOWMODE) {
            case Kid.SHOWMODE_NAME_AND_COINS:
                return this.name + "   " + this.sessions.get(0).coin;
            default:    // Kid.SHOWMODE_NAME_ONLY
                return this.name;
        }
    }
}
