package com.patachadmin.patachou.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

public class Constant {

    public static String USERID;
    public static String TYPE;
    public static String PRODUCTID;
    public static boolean getAdminLoginStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("admin", false);
    }

    public static void setAdminLoginStatus(Context context , boolean s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("admin", s).commit();
    }
    public static boolean getSuperAdminLoginStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("superAdmin", false);
    }

    public static void setSuperAdminLoginStatus(Context context , boolean s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("superAdmin", s).commit();
    }

    public static String createOrderId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


    public static String getAdminEmail(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("email","");
    }

    public static void setAdminEmail(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("email", s).commit();
    }
    public static String getAdminPassword(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("password","");
    }

    public static void setAdminPassword(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("password", s).commit();
    }


    public static String getUserId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("id","");
    }

    public static void setUserId(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("id", s).commit();
    }
    public static String getUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("name","");
    }

    public static void setUsername(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("name", s).commit();
    }
}
