package com.mohamedazzazy.test.kidscode.java;

import com.mohamedazzazy.test.kidscode.java.Session;

import java.util.ArrayList;

/**
 * Created by Mohamed Azzazy on 06/06/2015.
 */
public class Kid {
    String name;
    String code;
    String mobile;
    ArrayList<Session> sessions;

    public Kid(String name, String code, String mobile, ArrayList<Session> sessions) {
        this.name = name;
        this.code = code;
        this.mobile = mobile;
        this.sessions = sessions;
    }
}
