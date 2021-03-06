package com.banjarasathi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 05-11-2016.
 */
public class Fare_Details extends AppCompatActivity
{
    AutoCompleteTextView auto_age ,auto_profe,auto_cast,auto_SubCast,auto_state,auto_City;;
    String city_id;
    ArrayList<String> list_city_ID;
    ArrayList<String> list_taluka;
    ArrayList<String> list_districk;
    ArrayList<String> list_city_name;
    String response_myleave;
    String myleave_json;
    LinearLayout lay_close_img;
    public static boolean checkflag=false;

    //UrlClass urlClass;
    String http,domain_url,api_name;

    HashMap<String,String> map_cityID;
    List<HashMap<String,String>> list_mapCity = new ArrayList<>();
    String cityID = "";

    SharedPreferences pref_filter;
    SharedPreferences.Editor editor_filter;
    public static final String PREF_NAME_FILTER = "pref_filter";

    AutoCompleteTextView auto_gender,auto_mariatl;
    String gender_ID = "";

    //marital status data...
    HashMap<String,String> map_marital;
    ArrayList<HashMap<String,String>> list_maritalMap;
    ArrayList<String> list_marital;
    String marital_ID = "";

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.village_listi_popup);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        this.setFinishOnTouchOutside(true);


        auto_gender = (AutoCompleteTextView)findViewById(R.id.auto_gender);
        auto_age = (AutoCompleteTextView)findViewById(R.id.auto_age);
        auto_profe = (AutoCompleteTextView) findViewById(R.id.auto_profession);
        auto_cast = (AutoCompleteTextView) findViewById(R.id.auto_cast);
        auto_SubCast = (AutoCompleteTextView) findViewById(R.id.auto_SubCast);
        auto_state = (AutoCompleteTextView) findViewById(R.id.auto_state);
        auto_City = (AutoCompleteTextView) findViewById(R.id.auto_City);

        lay_close_img=(LinearLayout)findViewById(R.id.lay_close_img);
        Button btn_submit=(Button)findViewById(R.id.btn_submit);


        requestQueue = Volley.newRequestQueue(this);

        pref_filter = getApplicationContext().getSharedPreferences(PREF_NAME_FILTER,this.MODE_PRIVATE);
        editor_filter = pref_filter.edit();




        auto_gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_gender.showDropDown();
                return true;
            }
        });

        auto_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String gender1 = auto_gender.getText().toString();
                if (gender1.equals("Male"))
                {
                    gender_ID = "1";
                }
                else if (gender1.equals("Female"))
                {
                    gender_ID = "2";
                }
            }
        });
        /////////////////////

        auto_age.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_age.showDropDown();
                return true;
            }
        });

        auto_age.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getAge = auto_age.getText().toString();

            }
        });
        /////////////////////

        auto_profe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_profe.showDropDown();
                return true;
            }
        });

        auto_profe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getProfe = auto_profe.getText().toString();

            }
        });
        /////////////////////

        auto_cast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_cast.showDropDown();
                return true;
            }
        });

        auto_cast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getcast = auto_cast.getText().toString();

            }
        });
        /////////////////////

        auto_SubCast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_SubCast.showDropDown();
                return true;
            }
        });

        auto_SubCast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getsubcast = auto_SubCast.getText().toString();

            }
        });
        /////////////////////

        auto_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_state.showDropDown();
                return true;
            }
        });

        auto_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getstate = auto_state.getText().toString();

            }
        });
        ///////////////////
        auto_City.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_City.showDropDown();
                return true;
            }
        });

        auto_City.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String getcity = auto_City.getText().toString();

            }
        });
        //////////////////
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor_filter.clear();
                editor_filter.commit();

                checkflag = true;
                String gender=auto_gender.getText().toString();
                String age = auto_age.getText().toString();
                String profession = auto_profe.getText().toString();
                String cast = auto_cast.getText().toString();
                String subcast = auto_SubCast.getText().toString();
                String state = auto_state.getText().toString();
                String city = auto_City.getText().toString();



                editor_filter.putString("gender", gender);
                editor_filter.putString("age", age);
                editor_filter.putString("profession", profession);
                editor_filter.putString("cast", cast);
                editor_filter.putString("subcast", subcast);
                editor_filter.putString("state", state);
                editor_filter.putString("city", city);
                editor_filter.putString("para", "1");
                editor_filter.putString("pageFlag", "16");
                editor_filter.commit();

                Intent intent = new Intent(Fare_Details.this, Userlist_fragment.class);
                   /* intent.putExtra("gender",gender);
                    intent.putExtra("age",age);
                    intent.putExtra("profession",profession);
                    intent.putExtra("cast",cast);
                    intent.putExtra("subcast",subcast);
                    intent.putExtra("state",state);
                    intent.putExtra("city",city);*/
                    //intent.putExtra("para","1");
                    //intent.putExtra("pageFlag","16");
                startActivity(intent);
                finish();

            }
        });


        lay_close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setadapter()
    {

        List<String> list_gender = new ArrayList<>();
        list_gender.add("Male");
        list_gender.add("Female");



        //city
        //profession
        //education
        ArrayAdapter<String> adapter_age = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_age.setAdapter(adapter_age);

        ArrayAdapter<String> adapter_prof = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_profe.setAdapter(adapter_prof);

        ArrayAdapter<String> adapter_cast = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_cast.setAdapter(adapter_cast);

        ArrayAdapter<String> adapter_subcast = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_SubCast.setAdapter(adapter_subcast);

        ArrayAdapter<String> adapte_state = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_state.setAdapter(adapte_state);

        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_City.setAdapter(adapter_city);

    }

}
