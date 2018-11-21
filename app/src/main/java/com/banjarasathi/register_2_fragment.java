package com.banjarasathi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.banjarasathi.R.id.ac_profesion;
import static com.banjarasathi.R.id.toolbar;


public class register_2_fragment extends Fragment
{

    View v;
    String response;
    Button btn_next;
    String myJSON;
    Toolbar toolbar;
    Button btn_signup_2;
    HashMap<String,String> prof_list;
    ArrayList<String> prof_list1;


    AutoCompleteTextView ac_edu,ac_profe,ac_income,ac_caste,ac_subCaste,ac_gotra;
    EditText ed_family_det;

    public  static boolean regist_1=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.registre_2_fragment
                , container, false);
       // get the reference of Button
        btn_next = (Button) v.findViewById(R.id.btn_signup_2);
        prof_list=new HashMap<String, String>();
        prof_list1=new ArrayList<String>();







        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        TextView Header = (TextView) v.findViewById(R.id.toolText);
        ImageView img_logout = (ImageView) v.findViewById(R.id.toolImg);
        //setSupportActionBar(toolbar);

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");




            Header.setText("Sign Up");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            img_logout.setImageResource(R.drawable.back_arrow);
        }*/


        ac_edu = (AutoCompleteTextView)v.findViewById(R.id.ac_edducation);
        ac_profe = (AutoCompleteTextView)v.findViewById(ac_profesion);
        ac_income = (AutoCompleteTextView)v.findViewById(R.id.ac_occupation);
        ac_caste = (AutoCompleteTextView)v.findViewById(R.id.ac_caste);
        ac_subCaste = (AutoCompleteTextView)v.findViewById(R.id.ac_subCaste);
        ac_gotra = (AutoCompleteTextView)v.findViewById(R.id.ac_gotra);
        ed_family_det = (EditText)v.findViewById(R.id.ed_family_details);
        btn_signup_2 = (Button)v.findViewById(R.id.btn_signup_2);


        //ac_profe=(AutoCompleteTextView)v.findViewById(R.id.ac_profesion);
        //getprofesionData();
        /*ac_profe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                ac_profe.showDropDown();
                //state_ID = "";
                return true;

            }
        });
        ac_profe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String state = ac_profe.getText().toString();
              *//*  prof_ID = prof_list.get(i).get(state);
                Log.i("ac_state","state=="+state+" state_ID=="+state_ID);
          *//*


            }
        });*/
        /* ac_profesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac_profesion.setText("");
            }
        });
        */


        List<String> list_cast = new ArrayList<>();
        list_cast.add("Banjara");
        list_cast.add("Banjara");
        ArrayAdapter<String> adapter_cast = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_cast);
        ac_caste.setAdapter(adapter_cast);

        ac_caste.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                ac_caste.showDropDown();
                return true;
            }
        });

        List<String> list_subcast = new ArrayList<>();
        list_subcast.add("Labhani");
        list_subcast.add("Gor");
        ArrayAdapter<String> adapter_sub = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_subcast);
        ac_subCaste.setAdapter(adapter_sub);

        ac_subCaste.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                ac_subCaste.showDropDown();
                return true;
            }
        });

        List<String> list_income = new ArrayList<>();
        list_income.add("10000");
        list_income.add("20000");
        ArrayAdapter<String> adapter_income = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_income);
        ac_income.setAdapter(adapter_income);

        ac_income.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                ac_income.showDropDown();
                return true;
            }
        });
       // C:\Program Files\Git

        List<String> list_gothr = new ArrayList<>();
        list_gothr.add("Banjara");
        list_gothr.add("Labhani");
        list_gothr.add("Gor");
        ArrayAdapter<String> adapter_gotr = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_gothr);
        ac_gotra.setAdapter(adapter_gotr);

        ac_gotra.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                ac_gotra.showDropDown();
                return true;
            }
        });


        btn_signup_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (ac_edu.getText().toString().equals("") &&
                        ac_profe.getText().toString().equals("") &&
                        ac_income.getText().toString().equals("") &&
                        ac_caste.getText().toString().equals("") &&
                        ac_subCaste.getText().toString().equals("") &&
                        ac_gotra.getText().toString().equals("")
                        )
                {
                    ac_edu.setError("Select Education");
                    ac_profe.setError("Enter Profesion");
                    ac_income.setError("Enter Income");
                    ac_caste.setError("Select Caste");
                    ac_subCaste.setError("Select SubCaste");
                    ac_gotra.setError("Select Gotra");

                }
                else if (ac_edu.getText().toString().equals(""))
                {
                    ac_edu.setError("Select Education");
                }
                else if (ac_profe.getText().toString().equals(""))
                {
                    ac_profe.setError("Enter Profesion");
                }
                else if (ac_income.getText().toString().equals(""))
                {
                    ac_income.setError("Enter Income");
                }
                else if (ac_caste.getText().toString().equals(""))
                {
                    ac_caste.setError("Select your caste");
                }
                else if (ac_subCaste.getText().toString().equals(""))
                {
                    ac_subCaste.setError("Select subcaste");
                }
                else if (ac_gotra.getText().toString().equals(""))
                {
                    ac_gotra.setError("Select Gotra");
                }

                else
                {
                    /* View view1 = Sign_up_2.this.getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }*/
                    String edu = ac_edu.getText().toString();
                    String prof = ac_profe.getText().toString();
                    String income = ac_income.getText().toString();
                    String caste = ac_caste.getText().toString();
                    String sub_Caste = ac_subCaste.getText().toString();
                    String gotra = ac_gotra.getText().toString();

                    Resgister_1_frag.editor_regist.putString("edu", edu);
                    Resgister_1_frag.editor_regist.putString("prof", prof);
                    Resgister_1_frag.editor_regist.putString("income", income);
                    Resgister_1_frag.editor_regist.putString("caste", caste);
                    Resgister_1_frag.editor_regist.putString("sub_Caste", sub_Caste);
                    Resgister_1_frag.editor_regist.putString("gotra", gotra);
                    Resgister_1_frag.editor_regist.apply();

                    loadFragment(new register_3_fragment());


                }


            }
        });











        return v;
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

    public void getprofesionData()
    {
        class GetData extends AsyncTask<String, Void, String>
        {
            ProgressDialog pd;

            @Override
            protected void onPreExecute()
            {
                pd = ProgressDialog.show(getActivity(), "", "Loading Data... Please Wait", true);
                pd.show();
            }

            @Override
            protected String doInBackground(String... params)
            {
                /*[{"responseMessage":"success","responseCode":1,
                    "leavetype":[{"mast_id":6,"mast_title":"Casual Leave"},{"mast_id":7,"mast_title":"Sick Leave"},{"mast_id":8,"mast_title":"Privilege Leave"},{"mast_id":9,"mast_title":"Maternity Leave"}],
                    "reportingPerson":[{"firstName":"Vitthal","lastName":"Mogal","email":"contactgogird@infogird.com"}]}]*/
                try
                {
                    //String url=""+http+Login_url+api_name+"signIn?";

                    String leave_url = "leaves";
                    Log.i("leave_url", leave_url);
                    String query3 = String.format("id=%s", URLEncoder.encode("", "UTF-8"));
                    URL url = new URL(leave_url + query3);
                    Log.i("url", ""+ url);

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    int responceCode = connection.getResponseCode();

                    if (responceCode == HttpURLConnection.HTTP_OK)
                    {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = br.readLine()) != null)
                        {
                            response = "";
                            response += line;
                            Log.i("response_line", response);
                        }
                    }
                    else
                    {
                        response = "";
                    }
                }
                catch (Exception e)
                {
                    Log.e("Exception", e.toString());
                }

                return response;
            }

            @Override
            protected void onPostExecute(String result)
            {
                myJSON = result;
                Log.i("json",myJSON);
                show_leave();
                pd.dismiss();
            }
        }

        GetData getData = new GetData();
        getData.execute();
    }

    public void show_leave()
    {
        try
        {
            JSONArray json = new JSONArray(myJSON);
            JSONObject object = json.getJSONObject(0);
            JSONArray obj = object.getJSONArray("leavetype");

            //Log.i("json","json: "+json +"\n object" +object+"\n obj"+obj);

            //spinnerArray = new String[obj.length()];

            for(int i=0;i< obj.length();i++)
            {
                JSONObject jobj = obj.getJSONObject(i);
                final String mast_id = jobj.getString("mast_id");
                final String mast_title =jobj.getString("mast_title");
                prof_list.put(mast_id, mast_title);
                prof_list1.add(mast_title);
            }

            //Log.i("map",spinnerMaptype+"1");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_custom, prof_list1);
            ac_profe.setAdapter(dataAdapter);

            Log.e("pass ", "connection success ");

        }
        catch (Exception e)
        {
            Log.e("Fail ", e.toString());
        }

    }



}
