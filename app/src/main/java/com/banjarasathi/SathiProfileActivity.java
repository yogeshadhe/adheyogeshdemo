package com.banjarasathi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static android.bluetooth.BluetoothDevice.EXTRA_NAME;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class SathiProfileActivity extends AppCompatActivity
{

    public  static  final  String  firstname="firstName";
    public  static final String middleName="middleName";
    public static final  String lastName="lastName";
    public static  final String  gender="gender";
    public static final String dob="DOB";
    public static final String age="age";

    public static final String mobileno="mobileno";
    public static final String emailid="emailid";
    public static final  String education_="education";
    public  static final  String profesion="profesion";
    public static final String income="income";
    public  static final  String caste="caste";
    public static final  String subcaste="subcaste";
    public static final String gotra="gotra";
    public static final String currentStateId="currentStateId";
    public static final String currentCityId="currentCityId";
    public static final String currentAddress="currentAddress";
    public static final String profilepath="profilepath";
    public static  final String hight_1="height";
    String url ="http://banjarasathi.com/Api/userlist.php?";

    public ProgressDialog progressDialog;
    RequestQueue requestQueue;
    Check_net_Connection connection;
    TextView name,pro,cas,edu,DOB,hight,sta,city,abt,p_education,p_profession,p_income,p_cas,p_subcas,p_got,a_sta,a_city,a_area;

    CollapsingToolbarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    String uId;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sathi_profile);

        name=(TextView)findViewById(R.id.name);
        pro=(TextView)findViewById(R.id.pro);
        cas=(TextView) findViewById(R.id.cas);
        edu=(TextView) findViewById(R.id.edu);
        DOB=(TextView)findViewById(R.id.DOB);
        hight=(TextView)findViewById(R.id.hight);
        sta=(TextView)findViewById(R.id.sta);
        city=(TextView)findViewById(R.id.city);
        abt=(TextView)findViewById(R.id.abt);
        p_education=(TextView)findViewById(R.id.education);
        p_profession=(TextView)findViewById(R.id.profession);
        p_income=(TextView)findViewById(R.id.income);
        p_cas=(TextView)findViewById(R.id.cc);
        p_subcas=(TextView)findViewById(R.id.ss);
        p_got=(TextView)findViewById(R.id.gg);
        a_sta=(TextView)findViewById(R.id.s);
        a_city=(TextView)findViewById(R.id.c);
        a_area=(TextView)findViewById(R.id.a);
        appBarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        connection=new Check_net_Connection();
        Intent intent = getIntent();
        uId = intent.getStringExtra("userid_people");
        Log.i("uId","=="+uId);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle("test");
        requestQueue = Volley.newRequestQueue(SathiProfileActivity.this);

        if (connection.hasConnection(SathiProfileActivity.this))
        {
            getAllProfileData();
        }
        else {
            connection.showNetDisabledAlertToUser(SathiProfileActivity.this);
        }

    }
    private void loadBackdrop()
    {
       // final ImageView imageView = findViewById(R.id.backdrop);
        //Glide.with(this).load(R.drawable.ic_email_black).apply(RequestOptions.centerCropTransform()).into(imageView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
    //get all profile data...
    public void getAllProfileData()
    {
        //http://banjarasathi.com/Api/userlist.php?uId=11
        progressDialog = ProgressDialog.show(SathiProfileActivity.this, "Please wait", "Loading data...", true);
        progressDialog.show();

        try
        {
            String query = String.format("uId=%s",
                    URLEncoder.encode(uId,"UTF-8"));

            final String profile_url = url + query;
            Log.i("url","profile_url_edit=="+profile_url);

            Response.Listener<String> successListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                    Log.i("response","profile=="+response);
                    onGetProfileResponse(response);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }

                    if (error.networkResponse == null)
                    {
                        if (error.getClass().equals(TimeoutError.class))
                        {
                            Toast.makeText(SathiProfileActivity.this,"Server Connection Timeout",Toast.LENGTH_SHORT).show();

                        }
                        if (error.getClass().equals(NoConnectionError.class))
                        {
                            Toast.makeText(SathiProfileActivity.this,"Check Internet Connection",Toast.LENGTH_SHORT).show();

                        }
                        if (error.getClass().equals(NetworkError.class))
                        {
                            Toast.makeText(SathiProfileActivity.this,"Check Internet Connection",Toast.LENGTH_SHORT).show();


                        }
                        if (error.getClass().equals(Network.class))
                        {
                            Toast.makeText(SathiProfileActivity.this,"Check Internet Connection id Active",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

            StringRequest request = new StringRequest(Request.Method.GET,profile_url,successListener,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onGetProfileResponse(String json)
    {
        Log.i("json","=="+json);
        try
        {
            JSONObject jsonObject1 = new JSONObject(json);
            JSONArray jsonArray=jsonObject1.getJSONArray("data");
            JSONObject object = jsonArray.getJSONObject(0);

            String first_nameB = object.getString(firstname);
            String middle_nameB = object.getString(middleName);
            String last_nameB = object.getString(lastName);
            String genderB = object.getString(gender);
            String agee=object.getString(age);
            String dobB = object.getString(dob);
            String mobileNoB = object.getString(mobileno);
            String emailB = object.getString(emailid);
            String hight1=object.getString(hight_1);

            String education = object.getString(education_);
            String profession = object.getString(profesion);
            String incomeP = object.getString(income);
            String cast = object.getString(caste);
            String subCastP = object.getString(subcaste);
            String gotraP = object.getString(gotra);
            //String familyDetailP = object.


            String state = object.getString(currentStateId);
            String city1 = object.getString(currentCityId);
            String address = object.getString(currentAddress);
            String photo = object.getString(profilepath);
            Log.i("photo","=="+photo);


            String img_url = "http://";
            Log.i("url","image="+img_url);
            String final_img_url = img_url+photo;
            Log.i("final_img_url","======================"+final_img_url);

            collapsingToolbar.setTitle(first_nameB+" "+last_nameB);
            name.setText(""+first_nameB+" "+last_nameB);
            pro.setText(""+profession);
            cas.setText(""+cast);
            edu.setText(""+education);
            DOB.setText(""+agee);
            hight.setText(""+hight1);
            sta.setText(state);
            city.setText(city1);

            p_education.setText(""+education);
            p_profession.setText(""+profession);
            p_income.setText(""+incomeP);
            p_cas.setText(""+cast);
            p_subcas.setText(""+subCastP);
            p_got.setText(""+gotraP);
            a_sta.setText(""+state);
            a_city.setText(""+city1);
            a_area.setText(""+address);

            //abt.setText("");

            final ImageView img = new ImageView(this);
            Picasso.with(img.getContext())
                    .load(final_img_url)
                    .into(img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            appBarLayout.setBackgroundDrawable(img.getDrawable());
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }



    }
}