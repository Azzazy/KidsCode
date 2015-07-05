package com.mohamedazzazy.test.kidscode.java;

import java.util.Date;

/**
 * Created by Mohamed Azzazy on 06/06/2015
 * within project KidsCode.
 */
public class Kid {

    static public final int SHOWMODE_NAME_ONLY = 1;
    static public final int SHOWMODE_NAME_AND_COINS = 2;

    public String id;
    public String name;
    public String mobile;
    public Session thisSession;


    public Kid(String id,String name,  String mobile) {
        this.name = name;
        this.id = id;
        this.mobile = mobile;
        this.thisSession = new Session(0, new Date());
    }

    public Kid() {
    }

    public String toString(int SHOWMODE) {
        switch (SHOWMODE) {
            case Kid.SHOWMODE_NAME_AND_COINS:
                return this.name + "   " + this.thisSession.coin;
            default:    // Kid.SHOWMODE_NAME_ONLY
                return this.name;
        }
    }


}
