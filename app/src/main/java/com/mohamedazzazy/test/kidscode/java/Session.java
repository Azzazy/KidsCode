package com.mohamedazzazy.test.kidscode.java;

import java.util.Date;

/**
 * Created by Mohamed Azzazy on 06/06/2015.
 */
public class Session {
    Boolean isAttended;
    int coin;
    Date date;

    public Session( int coin, Date date) {
        this.isAttended = true;
        this.coin = coin;
        this.date = date;
    }
}
