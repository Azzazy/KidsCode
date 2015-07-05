package com.mohamedazzazy.test.kidscode.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mohamed Azzazy on 06/06/2015
 * within project KidsCode.
 */
public class Session {
    public int coin;
    Date date;

    public Session(String coin, String dateStr) {
        this.coin = Integer.parseInt(coin);
        setDate(dateStr);
    }

    public Session(int coin, Date date) {
        this.coin = coin;
        this.date = date;
    }
    private void setDate(String dateStr) {
        try {
            this.date = new SimpleDateFormat("ddMMyy").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
