package com.nef.corgi.apppowercorpore.DTO;

import android.content.Context;
import android.content.SharedPreferences;

import com.nef.corgi.apppowercorpore.Activitys.MainActivity;

import java.text.SimpleDateFormat;

public class Preferences {
   private static final  SimpleDateFormat FORMATO = new SimpleDateFormat("y-M-d-H-m-s");

    public void saveCredentials(SharedPreferences preferences,UserDTO prefuser){

        SharedPreferences.Editor editor;
        editor = preferences.edit();

        String login_timestamp=FORMATO.format(System.currentTimeMillis());
        editor.putString("USER",prefuser.getUser_name());
        editor.putString("LoginTimestamp", FORMATO.format(login_timestamp));


        editor.commit();
    }
}