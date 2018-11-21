package com.banjarasathi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Login_fragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    private static final String Tag_uid="uId";
    private static final  String Tag_Frist_name="firstName";
    private static  final  String Tag_M_name="middleName";
    private static final String Tag_last_name="lastName";
    private static final String Tag_email="emailid";
    private static final  String Tag_mobile_no="mobileno";
    private static final String PREFER_NAME = "MyPref";//username paass and uid


    UserSession session;
    Check_net_Connection checkNet_on_click;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    String response_login,myJson_sign,uname,pass;

    TextView txt_name,txt_pass,tv_foregut_pass ,tv_sign_up;
    Button btn_signIn;




    public Login_fragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Login_fragment newInstance(String param1, String param2) {
        Login_fragment fragment = new Login_fragment();
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
        View v=inflater.inflate(R.layout.login, container, false);
        txt_name=(TextView)v.findViewById(R.id.ed_username);
        txt_pass=(TextView)v.findViewById(R.id.ed_password);
        btn_signIn=(Button)v.findViewById(R.id.btn_signIn);
        tv_foregut_pass=(TextView)v.findViewById(R.id.tv_foregut_pass);
        tv_sign_up=(TextView)v.findViewById(R.id.tv_sign_up);

        session=new UserSession(getActivity());
        checkNet_on_click=new Check_net_Connection();
        pref=getActivity().getSharedPreferences(PREFER_NAME,getActivity().MODE_PRIVATE);
        editor=pref.edit();


        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uname=txt_name.getText().toString();
                pass=txt_pass.getText().toString();
                if(uname.equals("") && pass.equals(""))
                {
                    txt_name.setError("Enter UserName");
                    txt_pass.setError("Enter Password");
                }
                else if(uname.equals(""))
                {
                    txt_name.setError("Enter UserName");
                }
                else if (pass.equals(""))
                {
                    txt_pass.setError("Enter Password");
                }
                else
                {
                    if (checkNet_on_click.hasConnection(getActivity()))
                    {
                          //Intent intent=new Intent(getActivity(),BaseNavigation.class);
                                                //   startActivity(intent);
                         signIn();
                    }
                    else
                    {
                        checkNet_on_click.showNetDisabledAlertToUser(getActivity());
                    }
                }

            }
        });
        tv_foregut_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loadFragment(new ForgetPassword());
            }
        });
        tv_sign_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getActivity(),Register.class);
                startActivity(intent);
                //finish();
            }
        });



        return  v;
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

    public void signIn()
    {
        class GeversionData extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
                progressDialog.show();

            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    String signIn_url = "http://www.banjarasathi.com/Api/signin.php?";

                    String url1 = signIn_url + "username=" + URLEncoder.encode(uname, "UTF-8")
                            + "&password=" + URLEncoder.encode(pass, "UTF-8");

                    Log.i("url1", ""+url1);
                    URL url_ver = new URL(url1);

                    HttpURLConnection connection = (HttpURLConnection)url_ver.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    int responseCode = connection.getResponseCode();
                    Log.i("responsecode", ""+responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK)
                    {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = br.readLine()) != null)
                        {
                            response_login = "";
                            response_login += line;
                            //Log.i("response_image", response_version);

                        }
                    }
                    else
                    {
                        response_login = "";
                    }
                }
                catch (Exception e){
                    //Log.e("Exception", e.toString());
                }

                return response_login;
            }

            @Override
            protected void onPostExecute(String result)
            {
                if (result != null)
                {
                    progressDialog.dismiss();
                    myJson_sign = result;
                    Log.i("myJson_ver", myJson_sign);


                    //http://www.banjarasathi.com/Api/signin.php?username=abc99.com&password=12345
                    //{"success":1,"data":{"firstName":"yogesh","middleName":"Yogesh","lastName":"yogesh","uId":"7","mobileno":"123456","emailid":"abc99.com"},"message":"Login Successfully."}

                    try
                    {
                        JSONObject jsonObject = new JSONObject(myJson_sign);
                        //Log.i("jsonArray", "" + jsonArray);
                        final String LoginStatus =jsonObject.getString("success");
                        Log.i("LoginStatus123",LoginStatus);

                        final  String login_massage=jsonObject.getString("message");
                        Log.i("login_massage",login_massage);

                        if(LoginStatus.equals("1"))
                        {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            Log.i("jsonObject11",""+jsonObject1);

                            final String uid = jsonObject1.getString(Tag_uid);

                            final String frist_name = jsonObject1.getString(Tag_Frist_name);
                            Log.i("frist_name", "frist_name" + frist_name);

                            final String M_name = jsonObject1.getString(Tag_M_name);
                            Log.i("M_name", "M_name" + M_name);

                            final String last_name = jsonObject1.getString(Tag_last_name);
                            Log.i("last_name", "last_name" + last_name);

                            final String email = jsonObject1.getString(Tag_email);
                            Log.i("email", "email" + email);

                            final String mobile_no = jsonObject1.getString(Tag_mobile_no);
                            Log.i("mobile_no", "mobile_no" + mobile_no);

                            editor.putString("uId", uid);
                            editor.putString("frist_name", frist_name);
                            editor.putString("M_name", M_name);
                            editor.putString("last_name", last_name);
                            editor.putString("email", email);
                            editor.putString("mobile_no", mobile_no);
                            editor.apply();

                        }

                        if(LoginStatus.equals("1"))
                        {
                            session.CreteSession(uname,pass);

                            //loadFragment(new Userlist_fragment());

                            Intent intent=new Intent(getActivity(),BaseNavigation.class);
                            startActivity(intent);
                            //finish();
                        }
                        else
                        {
                            Log.i("LoginStatus123456",LoginStatus);
                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getActivity());
                            alerBuilder.setTitle("Login...");
                            alerBuilder.setMessage(login_massage);
                            alerBuilder.setCancelable(false);
                            alerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alerBuilder.show();

                        }
                    }
                    catch (JSONException e)
                    {
                        //Log.e("JsonException", e.toString());
                    }
                }

            }
        }

        GeversionData getUrlData = new GeversionData();
        getUrlData.execute();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
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
