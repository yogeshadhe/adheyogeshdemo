package com.banjarasathi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reset_fragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private static final  String  Tag_RESETCODE="success";
    private static final String Tag_RESETMSG="message";
    private static final String PREFER_NAME = "MyPref";//username paass and uid



    ProgressDialog progressDialog;
    HttpURLConnection conn;
    EditText edit_old_pass,edit_new_pass,edit_Confirm_pass;
    Button submit;
    String response,MY_json;
    String new_pass,re_pass,old_pass;
    String newpass,confirmpass;
    SharedPreferences pref;
    String uId;

    public Reset_fragment()
    {
        // Required empty public constructor
    }


    public static Reset_fragment newInstance(String param1, String param2) {
        Reset_fragment fragment = new Reset_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.resetpass, container, false);

        edit_old_pass=(EditText) v.findViewById(R.id.ed_old_pass);
        edit_new_pass=(EditText)v.findViewById(R.id.ed_new_password);
        edit_Confirm_pass=(EditText)v.findViewById(R.id.ed_retype_pass);
        submit=(Button)v.findViewById(R.id.btn_forgotPass);

        //unable_to_process_alert
        //alert_infomation
        //success_alert

        pref=getActivity().getSharedPreferences(PREFER_NAME,getActivity().MODE_PRIVATE);
        uId = (pref.getString("uId", ""));
        Log.i("uId",""+uId);

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                old_pass=edit_old_pass.getText().toString();
                new_pass = edit_new_pass.getText().toString();
                re_pass = edit_Confirm_pass.getText().toString();

                if ((edit_old_pass.length() <= 0 && edit_new_pass.length() <= 0 && edit_Confirm_pass.length() <= 0))
                {
                    edit_old_pass.setError("Please Enter Old PassWord");
                    edit_new_pass.setError("Please Enter New PassWord");
                    edit_Confirm_pass.setError("Retype New Password");
                    txtChange();
                }
                else if (edit_old_pass.length() <= 0)
                {
                    txtChange();
                    edit_old_pass.setError("Please enter Old PassWord");
                }
                else if (edit_new_pass.length() <= 0) {
                    txtChange();
                    edit_new_pass.setError("Please enter PassWord");
                }
                else if (edit_Confirm_pass.length() <= 0) {
                    txtChange();
                    edit_Confirm_pass.setError("Retype Password");
                }
                else if (checkPassword() && isValidPassword(re_pass, new_pass))
                {
                    Reset_pass();
                }
                else
                {
                    edit_Confirm_pass.setError("Please enter Strong PassWord (Ex. fleet12)    ");
                }


            }
        });



        return v;

    }

    public  void Reset_pass()
    {
        class getdata extends AsyncTask<String,String,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "", "Loading Data... Please Wait", true);
                progressDialog.show();

            }
            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                   //http://www.banjarasathi.com/Api/resetpassword.php?uId=2&oldpassword=123456&newpassword=12345
                   //{"success":0,"message":"Old Password not matched."}
                    String url_reset = "http://www.banjarasathi.com/Api/resetpassword.php?";


                    String url = url_reset + "uId=" + URLEncoder.encode(uId, "UTF-8")
                            + "&oldpassword=" + URLEncoder.encode(old_pass, "UTF-8")
                            + "&newpassword=" + URLEncoder.encode(re_pass, "UTF-8");

                    Log.i("url_reset",""+url);
                    URL url1 = new URL(url);


                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    //open coonection for httpUrlConnection
                    conn = (HttpURLConnection) url1.openConnection();
                    conn.setReadTimeout(5000);//read time
                    conn.setConnectTimeout(5000);//connection time out time
                    conn.setRequestMethod("GET");//method type by default get
                    //setDoOutput to true as we recieve data from json file
                    conn.setDoOutput(true);//set true for read data from  server

                    //resp get from conn obj
                    int response_code = conn.getResponseCode();
                    // Check if successful connection made then get response in result obj using strinf buffer

                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        response = result.toString();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    conn.disconnect();
                }
                return response;
            }
            @Override
            protected void onPostExecute(String result)
            {
                //http://www.banjarasathi.com/Api/resetpassword.php?uId=2&oldpassword=123456&newpassword=12345
                //{"success":0,"message":"Old Password not matched."}
                progressDialog.dismiss();

                if (result != null)
                {
                    MY_json=result;
                    try {
                        JSONObject jsonObject = new JSONObject(MY_json);

                        final String resetStatus = jsonObject.getString(Tag_RESETCODE);
                        final String resetMsg = jsonObject.getString(Tag_RESETMSG);

                        if (resetStatus.equals("1"))
                        {
                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.success_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText(resetMsg);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1)
                                {
                                    Intent intent =new Intent(getActivity(),BaseNavigation.class);
                                    startActivity(intent);
                                }
                            }).show();
                        }
                        else {
                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.unable_to_process_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText(resetMsg);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1) {

                                }
                            }).show();
                        }
                    } catch (Exception e) {
                        Log.e("parse", e.toString());
                    }
                }
                else {
                    Log.i("no data","");
                }
            }
        }
        getdata getdata=new getdata();
        getdata.execute();
    }
    public void txtChange()
    {
        edit_old_pass.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            public void afterTextChanged(Editable edt)
            {
                if (edit_old_pass.getText().length() > 0)
                {
                    edit_old_pass.setError(null);
                }
            }
        });
        edit_new_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void afterTextChanged(Editable edt)
            {

                if (edit_new_pass.getText().length() > 0)
                {
                    edit_new_pass.setError(null);
                }
            }
        });
        edit_Confirm_pass.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt)
            {
                if (edit_Confirm_pass.getText().length() > 0)
                {
                    edit_Confirm_pass.setError(null);
                }
            }
        });
    }

    private boolean checkPassword()
    {
        edit_Confirm_pass.setError(null);
        newpass = edit_new_pass.getText().toString();
        confirmpass = edit_Confirm_pass.getText().toString();
        Log.i("check_newpass",""+newpass);
        Log.i("check_confirmpass",""+confirmpass);
        if (newpass.equals(confirmpass))
        {
            return true;
        }
        else

        {
            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
            View dialogLayout = inflater_alert.inflate(R.layout.alert_infomation, null);
            AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
            builder.setView(dialogLayout);
            TextView massage = (TextView)dialogLayout.findViewById(R.id.txt_masage);
            massage.setText("New password and Confirm password does not match");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int arg1)
                {

                }
            }).show();


            return false;
        }
    }
    public boolean isValidPassword(final String password,final String re_password)
    {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d).{6,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        matcher = pattern.matcher(re_password);
        return matcher.matches();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
