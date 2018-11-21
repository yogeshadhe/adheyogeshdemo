package com.banjarasathi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class Fare_Details extends Activity
{
    AutoCompleteTextView auto_district,auto_taluka;
    AutoCompleteTextView auto_city;
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


        auto_district = (AutoCompleteTextView) findViewById(R.id.auto_district);
        auto_taluka = (AutoCompleteTextView) findViewById(R.id.auto_taluka);
        auto_city = (AutoCompleteTextView) findViewById(R.id.auto_city);
        lay_close_img=(LinearLayout)findViewById(R.id.lay_close_img);
        Button btn_submit=(Button)findViewById(R.id.btn_submit);

        auto_gender = (AutoCompleteTextView)findViewById(R.id.auto_gender);
        auto_mariatl = (AutoCompleteTextView)findViewById(R.id.auto_mariatl);

        requestQueue = Volley.newRequestQueue(this);

        list_city_name = new ArrayList<>();
        list_city_ID = new ArrayList<>();
        list_taluka = new ArrayList<>();
        list_districk = new ArrayList<>();

        list_maritalMap = new ArrayList<>();
        list_marital = new ArrayList<>();

        pref_filter = getApplicationContext().getSharedPreferences(PREF_NAME_FILTER,this.MODE_PRIVATE);
        editor_filter = pref_filter.edit();

       /* l = urlClass.getUrl();urlClass = new UrlClass();
        http = urlClass.getHTTP();
        domain_ur
        api_name = urlClass.getApiName();
        Log.i("signup","url==http=="+http+" domain=="+domain_url+" api_name=="+api_name);
*/
        show_my_leave();

        getMaritalStatusData();

        auto_city.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                auto_city.showDropDown();
                return true;
            }
        });

        auto_city.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                city_id = "";
                //claimType_ID = list_claimTypeID.get(position);
                String cityName = auto_city.getText().toString();
                //Log.i("ac_claimType","ID="+claimType_ID+" claimType="+claimType);

                city_id = list_city_ID.get(position);
                Log.i("claimType_ID","=="+city_id);

                cityID = list_mapCity.get(position).get(cityName);
                Log.i("city","cityID="+cityID+" cityName="+cityName);

            }
        });

        auto_taluka.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                auto_taluka.showDropDown();
                return true;
            }
        });

        auto_district.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                auto_district.showDropDown();
                return true;
            }
        });


        List<String> list_gender = new ArrayList<>();
        list_gender.add("Male");
        list_gender.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Fare_Details.this,R.layout.dropdown,list_gender);
        auto_gender.setAdapter(adapter);

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

        auto_mariatl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                auto_mariatl.showDropDown();
                marital_ID = "";
                return true;
            }
        });
        auto_mariatl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String marital_status = auto_mariatl.getText().toString();
                marital_ID = list_maritalMap.get(i).get(marital_status);
                Log.i("marital","status="+marital_status+" ID="+marital_ID);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (auto_district.getText().toString().equals("")&&
                        auto_taluka.getText().toString().equals("")&&
                        auto_city.getText().toString().equals("")&&
                        auto_gender.getText().toString().equals("")&&
                        auto_mariatl.getText().toString().equals(""))
                {
                    auto_district.setError("Select District");
                    auto_taluka.setError("Select Taluka");
                    auto_city.setError("Select City");
                    auto_gender.setError("Select Gender");
                    auto_mariatl.setError("Select Marital Status");
                }
                else
                {
                    editor_filter.clear();
                    editor_filter.commit();

                    checkflag=true;
                    String auto_district1= auto_district.getText().toString();
                    String auto_taluka1= auto_taluka.getText().toString();
                    //String auto_city1= auto_city.getText().toString();
                    String auto_city1= cityID;

                    editor_filter.putString("auto_district1",auto_district1);
                    editor_filter.putString("auto_taluka1",auto_taluka1);
                    editor_filter.putString("auto_city1",auto_city1);
                    editor_filter.putString("para","1");
                    editor_filter.putString("pageFlag","16");
                    editor_filter.putString("gender_ID",gender_ID);
                    editor_filter.putString("marital_ID",marital_ID);
                    editor_filter.commit();

                    Intent intent =new Intent(Fare_Details.this,Userlist_fragment.class);
                    /*intent.putExtra("auto_district1",auto_district1);
                    intent.putExtra("auto_taluka1",auto_taluka1);
                    intent.putExtra("auto_city1",auto_city1);
                    intent.putExtra("para","1");
                    intent.putExtra("pageFlag","16");
                    intent.putExtra("gender_ID",gender_ID);
                    intent.putExtra("marital_ID",marital_ID);*/
                    startActivity(intent);
                    finish();

                }
            }
        });


        lay_close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void show_my_leave()
    {
        auto_city.setAdapter(null);
        auto_district.setAdapter(null);
        auto_taluka.setAdapter(null);
        list_city_ID.clear();
        list_taluka.clear();
        list_districk.clear();
        list_city_name.clear();

        class GeversionData extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
            }
            @Override
            protected String doInBackground(String... params)
            {
                try
                {

                    //http://telibandhan.safegird.com/appapinew.php?pageFlag=17

                    //String myleave_url = "http://telibandhan.safegird.com/appapinew.php?";
                    //String Url = myleave_url+"pageFlag="+ URLEncoder.encode("17","UTF-8");

                    String url = http + domain_url + api_name;
                    String query = String.format("pageFlag=%s",
                            URLEncoder.encode("17","UTF-8"));


                    URL myleave_url_1 = new URL(url + query);
                    Log.i("url", "falg-17=filter= "+ myleave_url_1);

                    HttpURLConnection connection = (HttpURLConnection)myleave_url_1.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(10000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK)
                    {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = br.readLine()) != null)
                        {
                            response_myleave = "";
                            response_myleave += line;
                            Log.i("response_image", response_myleave);
                        }
                    }
                    else
                    {
                        response_myleave = "";
                    }
                }
                catch (Exception e)
                {

                }
                return response_myleave;
            }

            @Override
            protected void onPostExecute(String result)
            {
                //[{"msg":"Success","responsecode":0,"responsedata":
                // {"city":[{"city":"25","cityName":"Aurangabad"},{"city":"311","cityName":"Silvassa"},{"city":"42","cityName":"Bhimashankar"},{"city":"1","cityName":"Adilabad"},{"city":"50","cityName":"Bodh Gaya"}],
                // "taluka":[{"taluka":"t"}],
                // "district":[]}}]

                if (result != null)
                {
                    myleave_json = result;
                    Log.i("myleave_json", myleave_json);
                    if (myleave_json.equals("[]"))
                    {
                        Toast.makeText(Fare_Details.this,"No data", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        try
                        {
                            //[{"msg":"Success","responsecode":0,"responsedata":
                            // {"city":[{"city":"25","cityName":"Aurangabad"},{"city":"311","cityName":"Silvassa"},{"city":"42","cityName":"Bhimashankar"},{"city":"1","cityName":"Adilabad"},{"city":"50","cityName":"Bodh Gaya"}],
                            // "taluka":[{"taluka":"t"}],
                            // "district":[]}}]

                            JSONArray jsonArray=new JSONArray(myleave_json);
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            JSONObject jsonObject1=jsonObject.getJSONObject("responsedata");
                            Log.i("jsonObject1", ""+jsonObject1);
                            JSONArray jsonArray_city=jsonObject1.getJSONArray("city");
                            Log.i("jsonArray_city", ""+jsonArray_city);
                            JSONArray jsonArray_taluka=jsonObject1.getJSONArray("taluka");
                            Log.i("jsonArray_taluka",""+ jsonArray_taluka);
                            JSONArray jsonArray_districk=jsonObject1.getJSONArray("district");
                            Log.i("jsonArray_districk", ""+jsonArray_districk);

                            for (int i = 0; i < jsonArray_city.length(); i++)
                            {
                                JSONObject jsonObject_city = jsonArray_city.getJSONObject(i);

                                String cityid = jsonObject_city.getString("city");//leavesday
                                Log.i("cityid", " " + cityid);

                                String city_name = jsonObject_city.getString("cityName");//startendmon
                                Log.i("city_name", "city_name" + city_name);

                                list_city_name.add(city_name);
                                list_city_ID.add(cityid);

                                map_cityID = new HashMap<>();
                                map_cityID.put(city_name,cityid);
                                list_mapCity.add(map_cityID);

                            }
                            ArrayAdapter arrayAdapter_city = new ArrayAdapter(Fare_Details.this,R.layout.dropdown, list_city_name);
                            auto_city.setAdapter(arrayAdapter_city);

                            //jsonArray_taluka
                            for (int i = 0; i < jsonArray_taluka.length(); i++)
                            {
                                JSONObject jsonObject_taluka = jsonArray_taluka.getJSONObject(i);

                                // String taluka = jsonObject_taluka.getString("");//leavesday
                                //Log.i("cityid", " " + cityid);

                                String taluka = jsonObject_taluka.getString("taluka");//startendmon
                                Log.i("taluka", "taluka" + taluka);

                                list_taluka.add(taluka);
                                //list_city_ID.add(cityid);

                            }

                            ArrayAdapter arrayAdapter_taluka = new ArrayAdapter(Fare_Details.this, R.layout.dropdown, list_taluka);
                            auto_taluka.setAdapter(arrayAdapter_taluka);

                            // jsonArray_districk
                            for (int i = 0; i < jsonArray_districk.length(); i++)
                            {
                                JSONObject jsonObject_disc = jsonArray_districk.getJSONObject(i);

                                String districk = jsonObject_disc.getString("district");//leavesday
                                Log.i("districk", " " + districk);

                                //String city_name = jsonObject_disc.getString("");//startendmon
                                //Log.i("city_name", "city_name" + city_name);

                                list_districk.add(districk);
                                //list_city_ID.add(cityid);

                            }
                            ArrayAdapter arrayAdapter_districk = new ArrayAdapter(Fare_Details.this, R.layout.dropdown, list_districk);
                            auto_district.setAdapter(arrayAdapter_districk);

                            Log.i("list_city_name", "list_city_name" + list_city_name);
                            Log.i("list_taluka", "list_taluka" + list_taluka);
                            Log.i("list_districk", "list_districk" + list_districk);
                        }
                        catch (JSONException e) {

                        }
                    }
                }
                else
                {

                }
            }
        }
        GeversionData getUrlData = new GeversionData();
        getUrlData.execute();
    }

    public void getMaritalStatusData()
    {
        //http://telibandhan.safegird.com/appapinew.php?pageFlag=4

        try
        {
            String url = http + domain_url + api_name;
            String query = String.format("pageFlag=%s",
                    URLEncoder.encode("4","UTF-8"));
            String marital_url = url + query;
            Log.i("url","marital=="+marital_url);

            Response.Listener<String> success = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("response","marital=="+response);
//[{"msg":"Success","responsecode":1,"responsedata":[{"ms_id":"1","ms_title":"Single",
// "ms_desc":"Single (including living common law)\nThis category includes persons who have never
// married (including all persons less than 15 years of age). It also includes persons whose marriage
// has been legally annulled who were single before the annulled marriage and who have not remarried.
// Those who live with a common-law partner are included in this category."},{"ms_id":"2","ms_title":"Married","ms_desc":"Married (and not separated)\r\nThis category includes persons whose opposite- or same-sex spouse is living, unless the couple is separated or a divorce has been obtained. Also included are persons in civil unions."},{"ms_id":"3","ms_title":"Divorced","ms_desc":"Divorced (including living common law)\r\nThis category includes persons who have obtained a legal divorce and have not remarried. Those who live with a common-law partner are included in this category."},{"ms_id":"4","ms_title":"Widowed","ms_desc":"Widowed (including living common law)\r\nThis category includes persons who have lost their legally-married spouse through death and have not remarried. Those who live with a common-law partner are included in this category."},{"ms_id":"5","ms_title":"Separated","ms_desc":"Separated (including living common law)\r\nThis category includes persons currently legally married but who are no longer living with their spouse (for any reason other than illness, work or school) and have not obtained a divorce. Those who live with a common-law partner are included in this category."}]}]

                    /*if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }*/

                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String msg = jsonObject.getString("msg");
                        String responsecode = jsonObject.getString("responsecode");
                        if (responsecode.equals("1"))
                        {
                            JSONArray array = jsonObject.getJSONArray("responsedata");
                            for (int i=0 ; i<array.length() ; i++)
                            {
                                JSONObject object = array.getJSONObject(i);
                                String ms_id = object.getString("ms_id");
                                String ms_title = object.getString("ms_title");

                                map_marital = new HashMap<>();
                                map_marital.put(ms_title,ms_id);
                                list_maritalMap.add(map_marital);
                                list_marital.add(ms_title);

                            }
                            ArrayAdapter adapter = new ArrayAdapter(Fare_Details.this,R.layout.dropdown,list_marital);
                            auto_mariatl.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   /* if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }*/
                    /*if (error.networkResponse == null)
                    {
                        if (error.getClass().equals(TimeoutError.class))
                        {
                            Log.i("volley","error==time out--in");
                            //Toast.makeText(Sign_up.this,"error-time out",Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(coordinator_layout,"Server Connection Timeout",Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                        if (error.getClass().equals(NoConnectionError.class))
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,"Check Internet Connection",Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                        if (error.getClass().equals(NetworkError.class))
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,"Check Internet Connection id Active",Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                        if (error.getClass().equals(Network.class))
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,"Check Internet Connection id Active",Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                    }*/
                }
            };

            StringRequest request = new StringRequest(Request.Method.GET,marital_url,success,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
