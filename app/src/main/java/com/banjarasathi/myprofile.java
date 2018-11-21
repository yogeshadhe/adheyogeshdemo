package com.banjarasathi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.Snackbar;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;






//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adminsitrator on 23/10/2015.
 */

public class myprofile extends Fragment
{
    Activity activity;
    //ConnectionDetector cd;
    ProgressDialog dialog = null;
    ProgressDialog progressDialog;

    JSONArray ProfileData = null;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String uid = "uId";
    public static final String Tag_Sign = "responseCode";
    private static final String TAG_RESULTS = "result";
    private static final String Tag_FName = "firstName";
    private static final String Tag_LName = "lastName";
    private static final String Tag_mobile = "mobile";
    private static final String Tag_email = "email";

    public static final String Tagname = "firstName";
    public static final String TagLname = "lastName";

    String myJSON = null;
    String Usersessionid;
    String Fname, Lname, email, Mobile;
    String Url, IPaddress, APIKEY;

    EditText Name, LastName, Email, Contact, Address, City, State, Pincode;
   // CoordinatorLayout snackbarCoordinatorLayout;
   // Snackbar snackbar;
    Button Saveprofile;

    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    public myprofile()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myprofile_new, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
        SharedPreferences shared = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
        Usersessionid =   (shared.getString(uid,  ""));
        Button btn=(Button)rootView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadFragment(new FirstFragment());
            }
        });

        /* Name = (EditText)rootView.findViewById(R.id.firstname);
        LastName = (EditText)rootView.findViewById(R.id.lastname);
        Email = (EditText)rootView.findViewById(R.id.txtEmail);
        Contact = (EditText)rootView.findViewById(R.id.usercontact);
        snackbarCoordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.snackbarCoordinatorLayout_att);

        cd = new ConnectionDetector(getActivity());
        IPaddress =cd.getLocalIpAddress();
        APIKEY = cd.getAPIKEY();
        Url = cd.geturl();
*/
        /*if (cd.hasConnection(getActivity())){
            getData();
        }
        else {
            snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        Saveprofile = (Button)rootView.findViewById(R.id.Saveprofile);

        Saveprofile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cd.hasConnection(getActivity())) {
                    if (isvalidname()) {
                        if (isvalidlastname()) {
                            if (isValidEmail(Email.getText().toString())) {
                                if (isValidPhoneNumber()) {
                                    dialog = ProgressDialog.show(getActivity(), "Please Wait", "Updating Profile... ", true);
                                    new Thread(new Runnable() {
                                        public void run() {
                                            updateuserprofile();
                                        }
                                    }).start();
                                }
                            }
                        }
                    }
                }
                else {
                    snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });*/
        
        return rootView;
    }


    private void loadFragment(Fragment fragment)
    {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit(); // save the changes

    }
    /*public void updateuserprofile()
    {
        final String name = Name.getText().toString();
        final String lname = LastName.getText().toString();
        final String email = Email.getText().toString();
        final String mobile = Contact.getText().toString();

        try
        {
            String Transurl = ""+Url+"/basicDetails/?";
            String uri = Uri.parse(Transurl)
                    .buildUpon()
                    .appendQueryParameter("uId",Usersessionid)
                    .appendQueryParameter("firstName",name)
                    .appendQueryParameter("lastName", lname)
                    .appendQueryParameter("email",email)
                    .appendQueryParameter("mobile",mobile)
                    .appendQueryParameter("updateId","1")
                    .appendQueryParameter("ApiKey", APIKEY)
                    .appendQueryParameter("UserIPAddress", IPaddress)
                    .appendQueryParameter("UserID","1212")
                    .appendQueryParameter("UserAgent", "androidApp")
                    .appendQueryParameter("responsetype","2")
                    .build().toString();

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            JSONArray json = new JSONArray(response);
            JSONObject jsonObject = json.getJSONObject(0);
            final String LoginStatus =jsonObject.getString(Tag_Sign);
            final String Loginmsg = jsonObject.getString("responseMessage");
            //final String ResMsg = Loginmsg.substring(2, Loginmsg.length() - 2);

            getActivity().runOnUiThread(new Runnable()
            {
               @Override
               public void run() 
               {
                   dialog.dismiss();
                   if (LoginStatus.equals("1"))
                   {
                       editor = sharedpreferences.edit();
                       editor.putString(Tagname,name);
                       editor.putString(TagLname,lname);
                       editor.apply();

                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                       alertDialog.setTitle("Confirmed");
                       alertDialog.setMessage(Loginmsg);
                       alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which)
                           {
                               Intent in = new Intent(getActivity(),BaseNavigation.class);
                               startActivity(in);
                           }
                       });
                       alertDialog.show();
                   }
                   else
                   {
                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                       alertDialog.setTitle("Invalid");
                       alertDialog.setMessage(Loginmsg);
                       alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which)
                           {
                           }
                       });
                       alertDialog.show();
                   }
               }
           });
        }
        catch (Exception e)
        {
            Log.e("Fail 1", e.toString());
        }
    }

    public void showprofile()
    {
        try
        {
            JSONObject jsonObj = new JSONObject(myJSON);
            ProfileData = jsonObj.getJSONArray(TAG_RESULTS);
            Log.i("json", "" + ProfileData);

            for (int i = 0; i < ProfileData.length(); i++)
            {
                JSONObject c = ProfileData.getJSONObject(i);
                 Fname = c.getString(Tag_FName);
                 Lname  = c.getString(Tag_LName);
                 email  = c.getString(Tag_email);
                 Mobile  = c.getString(Tag_mobile);
                 Name.setText(Fname);
                 LastName.setText(Lname);
                 Email.setText(email);
                 Contact.setText(Mobile);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void getData()
    {
        class GetDataJSON extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute() 
            {
                progressDialog = ProgressDialog.show(getActivity(), "Please Wait", "Loading profile data...", true);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                String url = "http://jaidevcoolcab.cabsaas.in/sandbox";
                String url1 = "http://admin.clearcarrental.com/api";
                HttpPost httppost = new HttpPost(""+Url+"/basicDetails/?uId="+Usersessionid+"&firstName=&lastName=&email=&updateId=0&ApiKey="+APIKEY+"&UserIPAddress="+IPaddress+"&UserID=1212&UserAgent=Mozilla&responsetype=2");
                Log.i("Json", "Get Json");
                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try
                {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e)
                {
                    // Oops
                } finally
                {
                    try
                    {
                        if (inputStream != null)
                            inputStream.close();
                    }
                    catch (Exception squish)
                    {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result)
            {
                myJSON = result;
                showprofile();
                progressDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    *//* EMAIL VALIDATION *//*
    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (Email.getText().toString().trim().equals(""))
        {
            Email.setError("Enter Valid Email address");
            return false;
        }
        else
        {
            if (matcher.matches())
            {
                return true;
            }
            else
            {
                Email.setError("Enter Valid Email address");
                return false;
            }
        }
    }

    public boolean isValidPhoneNumber()
    {

       boolean chk = android.util.Patterns.PHONE.matcher(Contact.getText().toString()).matches();
       if(chk)
       {
           if(Contact.getText().toString().length() >= 10)
           {
               return true;
           }
           else
           {
               
               Contact.setError("Enter ten digit number");
               return false;
           }
       }
        
        else 
       {
          Contact.setError("Enter Correct Mobile Number"); 
          return false; 
       }
       
    }

    public boolean isvalidname()
    {
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(Name.getText().toString());
        boolean bs = ms.matches();
        if(bs)
        {
            return true;
        }
        else
        {
            Name.setError("Enter Traveller Name");
            return false;
        }

    }

    public boolean isvalidlastname()
    {
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(LastName.getText().toString());
        boolean bs = ms.matches();
        if(bs)
        {
            return true;
        }
        else
        {
            LastName.setError("Enter Traveller Name");
            return false;
        }
    }*/
}