package com.example.a.halalfoodworldwide.Models;

public class _User {
    private static String Token;
    private static String Email;

    public String getToken() {return Token;}

    public void setToken(String token){Token = token;}

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getEmail() {
        return Email;
    }

    private static final _User ourInstance = new _User();

    public static _User getInstance() {
        return ourInstance;
    }

    private _User() {
        Token = null;
        Email = null;
    }

    public Boolean IsEmpty(){
        return Token == null ? true : false;
    }
}
