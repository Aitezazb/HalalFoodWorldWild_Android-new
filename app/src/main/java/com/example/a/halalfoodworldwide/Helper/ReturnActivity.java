package com.example.a.halalfoodworldwide.Helper;

import com.example.a.halalfoodworldwide.LoginActivity;

public class ReturnActivity {

    private static Object ReturnActivityName;

    public static void setReturnActivityName(Object returnActivityName) {
        ReturnActivityName = returnActivityName;
    }

    public Object getReturnActivityName() {
        return ReturnActivityName;
    }
}
