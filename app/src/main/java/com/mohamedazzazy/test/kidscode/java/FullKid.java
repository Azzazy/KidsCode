package com.mohamedazzazy.test.kidscode.java;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mohamed Azzazy on 06/06/2015
 * within project KidsCode.
 */
public class FullKid extends Kid {

    boolean active;
    ArrayList<Session> sessions;

    public FullKid(String id, String name, String mobile, AgeGroup ageGroup) {
        this.active = isActive(ageGroup);
        this.ageGroup = ageGroup;
        this.name = name;
        this.id = id;
        this.mobile = mobile;
        this.sessions = new ArrayList<>();
    }



     static boolean isActive(AgeGroup c) {
        switch (c.toString().charAt(0)) {
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

    @Override
    public String toString() {
        return id;
    }
}
