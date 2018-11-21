package com.banjarasathi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by infogird128 on 04/12/2016.
 */

public class UserSession
{
    public static final String PREFName="Mypref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    public UserSession(Context context)
    {
        this._context=context;
        pref=_context.getSharedPreferences(PREFName, Context.MODE_PRIVATE);
        editor=pref.edit();

    }
    public void CreteSession(String name, String pass)
    {
        editor.putBoolean(IS_USER_LOGIN,true);
        editor.putString("name",name);
        editor.putString("pass",pass);
        editor.commit();
        Log.i("seesion_create","seesion_create");
    }
    public  void logout()
    {
        editor.clear();
        editor.commit();
        Intent intent=new Intent(_context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }
    public boolean isUserLogin()
    {
        return pref.getBoolean(IS_USER_LOGIN,false);
    }



}
