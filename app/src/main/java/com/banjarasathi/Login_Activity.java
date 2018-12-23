package com.banjarasathi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login_Activity extends AppCompatActivity
{
    UserSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);


        session=new UserSession(getApplicationContext());

        if (session.isUserLogin())
        {
            //Log.i("isUserLogin", "" + session.isUserLogin());
            Intent intent = new Intent(Login_Activity.this, BaseNavigation.class);
            startActivity(intent);
            finish();
        }
        else
        {
            loadFragment(new Login_fragment());
        }
    }



    private void loadFragment(Fragment fragment)
    {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();

        Log.i("count","=="+count);
      /*  if (count == 0)
        {
            super.onBackPressed();
            //additional code
        }
       *//* else if (count == 1)
        {
            super.onBackPressed();
            //additional code
        }*//*
        else
        {
            getFragmentManager().popBackStack();
        }*/
        //super.onBackPressed();
    }
}
