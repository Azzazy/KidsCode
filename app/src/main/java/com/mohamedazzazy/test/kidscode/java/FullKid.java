package com.mohamedazzazy.test.kidscode.java;

import java.util.ArrayList;

/**
 * Created by Mohamed Azzazy on 06/06/2015
 * within project KidsCode.
 */
public class FullKid extends Kid {

    char ageGroup;
    boolean active;
    ArrayList<Session> sessions;

    public FullKid(String id,String name,  String mobile, char ageGroup) {
        this.active = isActive(ageGroup);
        this.ageGroup = ageGroup;
        this.name = name;
        this.id = id;
        this.mobile = mobile;
        this.sessions = new ArrayList<>();
    }


    private static boolean isActive(char c) {
        switch (c) {
            case 'O':
            case 'M':
            case 'L':
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return id;
    }
}
