package com.mohamedazzazy.test.kidscode.java;

import java.util.ArrayList;

/**
 * Created by Mohamed Azzazy on 06/06/2015.
 */
public class Kid {

    public String name;
    String code;
    String mobile;
    char ageGroup;
    boolean active;
    ArrayList<Session> sessions;

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


}
