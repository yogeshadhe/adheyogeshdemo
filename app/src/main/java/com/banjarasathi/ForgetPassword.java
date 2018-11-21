package com.banjarasathi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForgetPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetPassword extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    //{"success":1,"mobileno":"9325588434","OTP":"932307","message":"OTP Send Successfully."}

    private static String TAG_forget_responseCode = "success";
    private static String TAG_forget_responseMessage = "message";
    private static String TAG_forget_OTP = "OTP";


    private static final  String  Tag_RESETCODE="success";
    private static final String Tag_RESETMSG="message";

    ProgressDialog progressDialog;
    Check_net_Connection checkNet_on_click;
    EditText txt_email, edt_otp, edt_new_pass, edt_confirm_pass;
    Button finish, btn_submt;
    LinearLayout lay_resetpass, lay_btn_finish;
    String response_for, myJson_for, emailmob;
    HttpURLConnection conn;
    String otp, new_pass, com_pass;
    String password1, conf_pass1,response_resetpass,MY_json_reset;


    public ForgetPassword()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForgetPassword newInstance(String param1, String param2) {
        ForgetPassword fragment = new ForgetPassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.forget_pass, container, false);

        txt_email = (EditText) v.findViewById(R.id.edt_email);
        finish = (Button) v.findViewById(R.id.btn_finish);
        btn_submt = (Button) v.findViewById(R.id.btn_submt);

        edt_otp = (EditText) v.findViewById(R.id.edt_otp);
        edt_new_pass = (EditText) v.findViewById(R.id.edt_new_pass);
        edt_confirm_pass = (EditText) v.findViewById(R.id.edt_confirm_pass);

        lay_resetpass = (LinearLayout) v.findViewById(R.id.lay_resetpass);
        lay_btn_finish = (LinearLayout) v.findViewById(R.id.lay_btn_finish);

        lay_btn_finish.setVisibility(View.VISIBLE);
        lay_resetpass.setVisibility(View.GONE);


        checkNet_on_click = new Check_net_Connection();

        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

                if (txt_email.length() <= 0) {
                    txt_email.setError("Please enter Email or Mobile No");
                    txtChange();
                    //isValidEmail(otp_email);
                } else {
                    emailmob = txt_email.getText().toString();

                    if (checkNet_on_click.hasConnection(getActivity())) {
                        txtChange();
                        forgetPass();
                    } else {
                        checkNet_on_click.showNetDisabledAlertToUser(getActivity());
                    }

                }


            }
        });

        btn_submt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v)
            {

                otp = edt_otp.getText().toString();
                new_pass = edt_new_pass.getText().toString();
                com_pass = edt_confirm_pass.getText().toString();

                ////tempcomment
                if ((edt_otp.length() <= 0 && edt_new_pass.length() <= 0 && edt_confirm_pass.length() <= 0)) {
                    edt_otp.setError("Please Enter OTP");
                    edt_new_pass.setError("Please Enter PassWord");
                    edt_confirm_pass.setError("Please Retype Password");
                    txtChange();

                } else if (edt_otp.length() <= 0) {
                    txtChange();
                    edt_otp.setError("Please Enter OTP");

                } else if (edt_new_pass.length() <= 0) {
                    txtChange();
                    edt_new_pass.setError("Please Enter PassWord");


                } else if (edt_confirm_pass.length() <= 0) {
                    txtChange();
                    edt_confirm_pass.setError("Please Retype Password");

                } else if (checkPassword() && isValidPassword(com_pass, new_pass)) {
                    if (checkNet_on_click.hasConnection(getActivity()))
                    {

                        Reset_pass();

                    } else {
                        checkNet_on_click.showNetDisabledAlertToUser(getActivity());
                    }

                } else {
                    edt_confirm_pass.setError("Please enter Strong PassWord(Ex. hrgird123)");

                }
            }


        });

        return v;

    }

    //http://www.banjarasathi.com/Api/sendotp.php?contactdetail=9325588434
    public void forgetPass() {
        class GeversionData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
                progressDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String version_url = "http://www.banjarasathi.com/Api/sendotp.php?";

                    String url1 = version_url + "contactdetail=" + URLEncoder.encode(emailmob, "UTF-8");


                    URL url_ver = new URL(url1);

                    HttpURLConnection connection = (HttpURLConnection) url_ver.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    int responseCode = connection.getResponseCode();
                    Log.i("responsecode", "" + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response_for = "";
                            response_for += line;
                            //Log.i("response_image", response_version);

                        }
                    } else {
                        response_for = "";
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }

                return response_for;
            }

            @Override
            protected void onPostExecute(String result) {

                //{"success":1,"mobileno":"9325588434","OTP":"932307","message":"OTP Send Successfully."}
                progressDialog.dismiss();
                if (result != null) {
                    myJson_for = result;
                    Log.i("myJson_ver", myJson_for);
                    try {

                        final JSONObject jsonObject = new JSONObject(myJson_for);
                        //final String check_att_res_code = "0";
                        final String responseCode_forgetPass = jsonObject.getString(TAG_forget_responseCode);
                        Log.i("responseCode_forgetPass", "" + responseCode_forgetPass);
                        final String responseMessage = jsonObject.getString(TAG_forget_responseMessage);
                        Log.i("responseMessage", "" + responseMessage);

                        if (responseCode_forgetPass.equals("1")) {
                            final String OTP = jsonObject.getString(TAG_forget_responseMessage);
                            Log.i("OTP", "" + OTP);

                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.success_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText(responseMessage);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1) {
                                    lay_btn_finish.setVisibility(View.GONE);
                                    lay_resetpass.setVisibility(View.VISIBLE);

                                }
                            }).show();

                        } else if (responseCode_forgetPass.equals("0")) {
                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.unable_to_process_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText(responseMessage);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1) {
                                    lay_btn_finish.setVisibility(View.VISIBLE);
                                    lay_resetpass.setVisibility(View.GONE);

                                }
                            }).show();
                        } else {
                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.unable_to_process_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText(responseMessage);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1) {

                                }
                            }).show();

                        }


                    } catch (JSONException e)
                    {
                        //Log.e("JsonException", e.toString());
                    }
                }

            }
        }

        GeversionData getUrlData = new GeversionData();
        getUrlData.execute();
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
                    ///http://www.banjarasathi.com/Api/forgetpassword.php?mobileno=9325588434&otp=489049&newpassword=12345
                    //{"success":0,"message":"OTP not matched."}

                    String url_reset = "http://www.banjarasathi.com/Api/forgetpassword.php?";


                    String url = url_reset + "mobileno=" + URLEncoder.encode(emailmob, "UTF-8")
                            + "&otp=" + URLEncoder.encode(otp, "UTF-8")
                            + "&newpassword=" + URLEncoder.encode(com_pass, "UTF-8");

                    Log.i("url_reset_pass",""+url);
                    URL url1 = new URL(url);


                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    //open coonection for httpUrlConnection
                    conn = (HttpURLConnection) url1.openConnection();
                    conn.setReadTimeout(10000);//read time
                    conn.setConnectTimeout(10000);//connection time out time
                    conn.setRequestMethod("GET");//method type by default get
                    //setDoOutput to true as we recieve data from json file
                    conn.setDoOutput(true);//set true for read data from  server

                    //resp get from conn obj
                    int response_code = conn.getResponseCode();
                    // Check if successful connection made then get response in result obj using strinf buffer

                    if (response_code == HttpURLConnection.HTTP_OK)
                    {
                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null)
                        {
                            result.append(line);
                        }
                        response_resetpass = result.toString();
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
                return response_resetpass;
            }
            @Override
            protected void onPostExecute(String result)
            {
                //{"success":0,"message":"OTP not matched."}
                progressDialog.dismiss();

                if (result != null)
                {
                    MY_json_reset=result;
                    Log.i("MY_json_reset",""+MY_json_reset);
                    try {
                        JSONObject jsonObject = new JSONObject(MY_json_reset);

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
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface d, int arg1)
                                {
                                    Intent intent=new Intent(getActivity(),Login_Activity.class);
                                    startActivity(intent);


                                }
                            }).show();
                        }
                        else
                        {
                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.unable_to_process_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText(resetMsg);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1)
                                {

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


    public void txtChange() {
        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt)
            {
                if (txt_email.getText().length() > 0)
                {
                    txt_email.setError(null);
                }
            }
        });
        edt_otp.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt)
            {
                if (edt_otp.getText().length() > 0) {
                    edt_otp.setError(null);
                }
            }
        });
        edt_new_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt)
            {
                if (edt_new_pass.getText().length() > 0)
                {
                    edt_new_pass.setError(null);
                }
            }
        });
        edt_confirm_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt)
            {
                if (edt_confirm_pass.getText().length() > 0)
                {
                    edt_confirm_pass.setError(null);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (txt_email.getText().toString().trim().equals("")) {

            //Toast.makeText(bookingdetails.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
            txt_email.setError("Enter Valid Email address");
            return false;
        } else {
            if (matcher.matches()) {
                return true;
            } else {

                txt_email.setError("Enter Valid Email address");
                return false;
            }
        }
    }


    private boolean checkPassword()
    {
        password1 = edt_new_pass.getText().toString();
        conf_pass1 = edt_confirm_pass.getText().toString();

        if (password1.equals(conf_pass1))
        {
            return true;

        }
        else
        {
            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
            View dialogLayout = inflater_alert.inflate(R.layout.alert_infomation, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(dialogLayout);
            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
            massage.setText("New and Confirm password does not match");
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


    /// regular password validation
    public boolean isValidPassword(final String password,final String re_password) {

        Pattern pattern;
        Matcher matcher;

//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
