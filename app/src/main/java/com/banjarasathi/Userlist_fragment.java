package com.banjarasathi;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.support.design.widget.CoordinatorLayout;



public class Userlist_fragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static final String TAG_firstName = "firstName";
    public static final String TAG_lastName = "lastName";
    public static final String TAG_profe = "profesion";
    public static final String TAG_income = "income";
    public static final String TAG_photo = "photo";
    public static final String TAG_uId = "uId";
    public  static final  String TAG_age="age";
    public static final String TAG_hight="height";
    public static final String TAG_cast="caste";
    public static final String TAG_sub_cast="subcaste";


    String auto_gender,auto_age,auto_profession,auto_cast,auto_subcast,auto_state,auto_city;

    URL url;
    attListAdapter adapter;
    Check_net_Connection connection;
    UserSession session;
    //pref data...
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String PREFER_NAME = "MyPref";
    String uid;

    //for filter data...
    SharedPreferences pref_filter;
    SharedPreferences.Editor editor_filter;
    public static final String PREF_NAME_FILTER = "pref_filter";


    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    Toolbar toolbar;

    LinearLayout lay_dashbordmain;
    TextView marque;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout txt_no_data;
    ListView attList_list;
    LinearLayout layout_progress;
    Snackbar snackbar;
    TextView load_more;
    EditText ed_search;
    LinearLayout layout_img;
    PopupWindow pw;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinator_layout;
    RequestQueue requestQueue;
    ProgressBar bar;

    boolean flag_timeout_AttendanceListing = true;
    boolean date_select = false;
    boolean hit_once = false;
    boolean userScrolled = false;
    boolean refresh = false;
    int lastVisible = 0;
    int total = 0;
    int arraylenght;
    int startIndex = 0;
    String auto_district1="";
    String auto_taluka1="";
    String auto_city1="";
    String para="";
    String pageFlag="";
    String gender_ID = "",marital_ID="";

    String firstName,lastName,email,photo;
    String fullName;
    int pack_version_code;
    String pack_verion;
    String notitital="";
    String notitital1;
    String http,domain_url,api_name,image_path;
    String response;
    String myJson;
    View loadMoreView;
    JSONArray jsonArray;
    String jsonArray_noti;
    ArrayList<HashMap<String,String>> list_peopleMAP;
    ArrayList<get_set_AttListing> array_list1 = new ArrayList<get_set_AttListing>();
    ArrayList<String> list_city_name;
    ArrayList<String> list_city_ID;
    ArrayList<String> list_taluka;
    ArrayList<String> list_districk;



    public Userlist_fragment()
    {// Required empty public constructor
    }
    public static Userlist_fragment newInstance(String param1, String param2) {
        Userlist_fragment fragment = new Userlist_fragment();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.user_list_fragment, container, false);



        connection=new Check_net_Connection();
        list_peopleMAP = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getActivity());
        layout_img = (LinearLayout)v.findViewById(R.id.layout_img);
        attList_list = (ListView)v.findViewById(R.id.att_listing);
        //lv_people = (ListView)findViewById(R.id.lv_people);
        coordinator_layout = (CoordinatorLayout)v.findViewById(R.id.coordinator_layout);
        ed_search = (EditText)v.findViewById(R.id.att_list_edSearch);
        lay_dashbordmain=(LinearLayout)v.findViewById(R.id.lay_dashbordmain) ;

        marque = (TextView) v.findViewById(R.id.marque_scrolling_text);
        marque.setSelected(true);



        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout_att_list);
        layout_progress = (LinearLayout)v.findViewById(R.id.att_list_Progress);

        txt_no_data = (LinearLayout)v.findViewById(R.id.txt_no_aprv_data);
        //ed_search = (EditText)findViewById(R.id.att_list_edSearch);
        loadMoreView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loadmore, null, false);

        load_more = (TextView)loadMoreView.findViewById(R.id.txt_loadmore);
        bar = (ProgressBar)loadMoreView.findViewById(R.id.bar);
       // loadMoreView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loadmore, null, false);
        //load_more = (TextView)loadMoreView.findViewById(R.id.txt_loadmore);
        //bar = (ProgressBar)loadMoreView.findViewById(R.id.bar);
        //attList_list.addFooterView(loadMoreView);

        list_city_name = new ArrayList<>();
        list_city_ID = new ArrayList<>();
        list_taluka = new ArrayList<>();
        list_districk = new ArrayList<>();

        session=new UserSession(getActivity());

        pref=getActivity().getSharedPreferences(PREFER_NAME,getActivity().MODE_PRIVATE);
        editor=pref.edit();
        //firstName = pref.getString("first_name","");
        //lastName = pref.getString("last_name","");
        //email = pref.getString("email","");
        uid = pref.getString("userId","null");
        Log.i("prefdata","uid="+uid);

        pref_filter = getActivity().getSharedPreferences(PREF_NAME_FILTER,getActivity().MODE_PRIVATE);
        editor_filter = pref_filter.edit();



        Log.i("signup","url==http=="+http+" domain=="+domain_url+" api_name=="+api_name+" image_path="+image_path);


        if (Fare_Details.checkflag)
        {
            auto_gender = pref_filter.getString("gender","");
            Log.i("auto_gender", "auto_gender=" + auto_gender);
            //auto_taluka1 = getIntent().getStringExtra("auto_taluka1");
            auto_age = pref_filter.getString("age","");
            Log.i("auto_age", "auto_age=" + auto_age);
            //auto_city1 = getIntent().getStringExtra("auto_city1");
            auto_profession = pref_filter.getString("profession","");
            Log.i("auto_profession", "=" + auto_profession);
            auto_cast = pref_filter.getString("cast","");
            Log.i("auto_cast", "=" + auto_cast);
            auto_subcast = pref_filter.getString("subcast","");
            Log.i("auto_subcast", "=" + auto_subcast);
            auto_state = pref_filter.getString("state","");
            Log.i("auto_state", "=" + auto_state);
            auto_city = pref_filter.getString("city","");
            Log.i("auto_city", "=" + auto_city);
            //para = getIntent().getStringExtra("para");
          /*  para = pref_filter.getString("para","0");
            Log.i("filterPREF", "para=" + para);

            */

        }
        else
        {
            auto_gender="";
            auto_age="";
            auto_cast="";
            auto_subcast = "";
            auto_state = "";
            auto_city="";
            //para="0";

        }
        if (connection.hasConnection(getActivity()))
        {
            hit_once = false;
            getAttendanceListing();

        }
        else {
            connection.showNetDisabledAlertToUser(getActivity());
        }

        try
        {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            pack_verion = pInfo.versionName;
            if (connection.hasConnection(getActivity()))
            {
                //checkVersion();
            }
            else
            {
                connection.showNetDisabledAlertToUser(getActivity());
            }

        }
        catch (Exception e)
        {
        }
        attList_list.setTextFilterEnabled(true);
        attList_list.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {
                    userScrolled = false;
                    if (arraylenght > startIndex)
                    {
                        updateListView();
                        total = firstVisibleItem + visibleItemCount;
                        lastVisible = visibleItemCount - 1;

                        bar.setVisibility(View.VISIBLE);
                        load_more.setText("Loading more data");

                        if (ed_search.getText().toString().equals("")) {
                            if (attList_list.getFooterViewsCount() == 0) {
                                attList_list.addFooterView(loadMoreView);
                            }
                        }
                    } else {
                        if (attList_list.getFooterViewsCount() == 0) {
                            attList_list.addFooterView(loadMoreView);
                        }

                        bar.setVisibility(View.GONE);
                        total = 0;
                        load_more.setText("No more data");
                        attList_list.removeFooterView(loadMoreView);
                    }
                }
            }
        });
        ///////////////////////////////////

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {

                refresh = true;
                startIndex = 0;
                arraylenght = 0;
                total = 0;
                lastVisible = 0;
                array_list1.clear();
                attList_list.setAdapter(null);
                Calendar c = Calendar.getInstance();
                if (attList_list.getFooterViewsCount() > 0)
                {
                    attList_list.removeFooterView(loadMoreView);
                }
                auto_gender="";
                auto_age="";
                auto_cast="";
                auto_subcast = "";
                auto_state = "";
                auto_city="";
                //para="0";
                getAttendanceListing();

            }
        });

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        ed_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(ed_search, InputMethodManager.SHOW_IMPLICIT);
                }else {
                    imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                }
            }
        });

        ed_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());

                if (adapter != null) {
                    adapter.filter(text);
                    total = 0;
                    lastVisible = 0;
                    attList_list.setSelection(total);
                    attList_list.removeFooterView(loadMoreView);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        layout_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { loadFragment(new UserListFilter()); }
        });
        return  v;
    }


    public void checkVersion()
    {
        try
        {
            String url = "http://banjarasathi.com/Api/version.php";
            String version_url = url ;
            Log.i("url","version="+version_url);

            Response.Listener<String> success = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    /*
                   {"success":1,"data":[{"Id":"1","version name":"1.0.1","status":"1",
                     "createdDateTime":"2018-12-10 21:18:14"}],
                     "message":"success"}
                   */

                    Log.i("response","version="+response);
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        String responsecode = jsonObject.getString("success");
                        Log.i("responsecode","=="+responsecode);
                        if (responsecode.equals("1"))
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);

                            String version = jsonObject1.getString("version name");
                            Log.i("version","=="+version);
                            String dateTime = jsonObject1.getString("createdDateTime");
                            Log.i("dateTime","=="+dateTime);

                            if (!version.equals(pack_verion))
                            {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                alertDialog.setTitle("New Update");
                                alertDialog.setMessage("Please update your app");
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.telibandhan&hl=en"));
                                        startActivity(intent);
                                        //finish();
                                    }
                                });

                                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(Intent.CATEGORY_HOME);
                                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(startMain);
                                        //finish();
                                    }
                                });

                                alertDialog.show();

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   /* if (progressDialog1.isShowing())
                    {
                        progressDialog1.dismiss();
                    }*/
                    if (error.networkResponse == null)
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
                    }

                }
            };

            StringRequest request = new StringRequest(Request.Method.GET,version_url,success,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAttendanceListing()
    {
        flag_timeout_AttendanceListing = true;

        class GetAttListingData extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                if (!refresh)
                {
                    if (date_select)
                    {
                        layout_progress.setVisibility(View.VISIBLE);
                        attList_list.setVisibility(View.GONE);
                        txt_no_data.setVisibility(View.GONE);
                    }
                    else
                    {
                        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Getting data...", true);
                        progressDialog.show();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    auto_gender="";
                    auto_age="";
                    auto_cast="";
                    auto_subcast = "";
                    auto_state = "";
                    auto_city="";
                    //para="0";
                    //pageFlag="16";

                    String leave_url = "http://banjarasathi.com/Api/userlist.php";
                   /* String query3 = String.format("userId=%s&auto_gender=%s&auto_age=%s&auto_cast=%s&auto_subcast=%s&auto_state=%s&auto_city=%s&para=%s",
                            URLEncoder.encode(uid, "UTF-8"),
                            URLEncoder.encode(auto_gender,"UTF-8"),
                            URLEncoder.encode(auto_age,"UTF-8"),
                            URLEncoder.encode(auto_cast,"UTF-8"),
                            URLEncoder.encode(auto_subcast,"UTF-8"),
                            URLEncoder.encode(auto_state,"UTF-8"),
                            URLEncoder.encode(auto_city,"UTF-8"),
                            URLEncoder.encode(para,"UTF-8"));*/
                   // url = new URL(leave_url + query3);


                    url = new URL(leave_url );
                    Log.i("url", "datewiseattendance2 " + url);

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
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
                        response = "";
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        while ((line = br.readLine()) != null)
                        {
                            response += line;
                        }
                    }
                    else
                    {
                        response = "";
                    }
                }catch (SocketTimeoutException e)
                {

                    flag_timeout_AttendanceListing = false;
                   Toast.makeText(getActivity(),"Server Connection Timeout",Toast.LENGTH_LONG).show();
                   e.printStackTrace();
                } catch (ConnectTimeoutException e) {

                    flag_timeout_AttendanceListing = false;
                    Toast.makeText(getActivity(),"Server Connection Timeout",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    Log.e("Exception", e.toString());
                }

                Log.i("response","people="+response);
                return response;
            }

            @Override
            protected void onPostExecute(String result)
            {
                mSwipeRefreshLayout.setRefreshing(false);

                if (result != null || !result.equals("null"))
                {
                    myJson = result;
                    Log.i("myJson_list", myJson);
                    if (!refresh)
                    {
                        if (date_select)
                        {
                            date_select = false;
                            layout_progress.setVisibility(View.GONE);
                        }
                        else
                        {
                            progressDialog.dismiss();
                        }
                    }

                    if (myJson.equals("[]"))
                    {
                        txt_no_data.setVisibility(View.VISIBLE);
                        layout_progress.setVisibility(View.GONE);
                        attList_list.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (flag_timeout_AttendanceListing)
                        {
                            txt_no_data.setVisibility(View.GONE);
                            layout_progress.setVisibility(View.GONE);
                            attList_list.setVisibility(View.VISIBLE);
                            try
                            {
                                //http://banjarasathi.com/Api/userlist.php
                                //{"notification":"For more details, Please contact to Admin No.8275937714","success":1,"data":[{"uId":"1","firstName":"yogesh","lastName":"yogesh","mobileno":"1234520","emailid":"abc.com","profesion":"devloper","income":"100000","caste":"banjara","subcaste":"banjara","height":"0.0","DOB":"1992-02-02","age":"26"},
                                JSONObject jsonObject1 = new JSONObject(myJson);
                                Log.i("jsonObject1", "" + jsonObject1);

                                String responceCode=jsonObject1.getString("success");
                                Log.i("responceCode", "" + responceCode);

                                String responceMassage=jsonObject1.getString("message");
                                Log.i("responceMassage", "" + responceMassage);


                                jsonArray_noti=jsonObject1.getString("notification");
                                Log.i("jsonArray_noti", "" + jsonArray_noti);


                                jsonArray=jsonObject1.getJSONArray("data");
                                Log.i("jsonArray", "" + jsonArray);



                                //jsonArray = new JSONArray(myJson);
                                //Log.i("jsonArray", "" + jsonArray);

                                //////////////////////////
                                int length = jsonArray.length();
                                Log.i("response", "jsonArray " + length);
                                int end;
                                if (length > 13)
                                {
                                    end = startIndex + 13;
                                    Log.i("response", "end " + end);
                                    attList_list.addFooterView(loadMoreView);
                                }
                                else
                                {
                                    end = length;
                                    Log.i("response", "end_else " + end);
                                    if (attList_list.getFooterViewsCount() > 0)
                                    {
                                        attList_list.removeFooterView(loadMoreView);
                                    }
                                }
                                //////////////////////
                                //for (intr jsonArray.lenth
                                for (int i = startIndex; i < end; i++)
                                {
                                    Log.i("full", "i== " + i);

                                    get_set_AttListing get_set = new get_set_AttListing();

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    firstName = object.getString(TAG_firstName);
                                    lastName = object.getString(TAG_lastName);
                                    fullName = firstName + " " + lastName;
                                    Log.i("full", "name " + fullName);



                                    String age = object.getString(TAG_age);
                                    Log.i("age", "age " + age);
                                    String hight = object.getString(TAG_hight);
                                    Log.i("hight", "hight " + hight);
                                    String cast = object.getString(TAG_cast);
                                    Log.i("cast","="+cast);
                                    String sub_cast = object.getString(TAG_sub_cast);
                                    String photo = "staticPhoto";
                                    //String photo = object.getString(TAG_photo);
                                    Log.i("full","photo=="+photo);


                                    Log.i("sub_cast", "sub_cast " + sub_cast);

                                    get_set.setuId(object.getString(TAG_uId));
                                    get_set.setFullName(fullName);
                                    get_set.setage(object.getString(TAG_age));
                                    get_set.sethight(object.getString(TAG_hight));
                                    get_set.setcast(object.getString(TAG_cast));
                                    get_set.setsubcast(object.getString(TAG_sub_cast));
                                    get_set.setprofe(object.getString(TAG_profe));
                                    get_set.setincome(object.getString(TAG_income));
                                    get_set.setProfile_photo("photosttic");

                                    Log.i("first", "name " + get_set.getFullName());
                                    arraylenght = jsonArray.length();

                                    array_list1.add(get_set);
                                    Log.i("array_list1", "array_list1 " + array_list1.size());

                                    adapter = new attListAdapter(getActivity(), array_list1);
                                    attList_list.setAdapter(adapter);
                                }

                                Log.i("array_list1", "array_list1 " + array_list1.size());


                                if (jsonArray_noti.toString().equals(""))
                                {

                                }
                                else
                                {
                                    /*for (int i = 0; i < jsonArray_noti.length(); i++)
                                    {
                                        JSONObject jsonObject2=jsonArray_noti.getJSONObject(i);
                                        //String notifi_detail=jsonObject2.getString("description");
                                        notitital1=jsonObject2.getString("tile");
                                        Log.i("notitital1",""+notitital1);
                                        notitital=(notitital+"  "+"")+ notitital1;

                                        Log.i("notitital",""+notitital);
                                    }*/
                                    marque.setText(jsonArray_noti);
                                }

                                if(jsonArray.toString().equals("[]"))
                                {
                                    txt_no_data.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    txt_no_data.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                    startIndex = adapter.getCount();
                                    Log.i("startIndex","Infirstcasll=="+startIndex);

                                }
                            }
                            catch (JSONException e)
                            {
                                Log.e("JsonException", e.toString());
                            }
                        }
                    }
                }
                else
                {
                    if (progressDialog.isShowing() && progressDialog != null){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Sorry...Bad internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
        GetAttListingData getAttListingData = new GetAttListingData();
        getAttListingData.execute();
    }

    public class attListAdapter extends BaseAdapter
    {
        private Context mContext;
        public LayoutInflater inflater = null;
        ArrayList<get_set_AttListing> citylist;
        private List<get_set_AttListing> attDetails_list = null;
        private ArrayList<get_set_AttListing> arraylist;

        public attListAdapter(Context context, List<get_set_AttListing> attDetails_list)
        {
            this.mContext = context;
            this.attDetails_list = attDetails_list;
            this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.citylist = new ArrayList<get_set_AttListing>();
            this.citylist.addAll(attDetails_list);
        }

        @Override
        public int getCount() {
            return attDetails_list.size();
        }

        @Override
        public get_set_AttListing getItem(int position)
        {
            return attDetails_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent)
        {
            View vi = convertView;
            if(convertView == null)
            {
                vi = inflater.inflate(R.layout.serch_custom, null);
            }

            final  TextView txt_uID=(TextView)vi.findViewById(R.id.uId);
            final TextView txt_fullName = (TextView)vi.findViewById(R.id.tv_fullName);
            final TextView txt_profession=(TextView)vi.findViewById(R.id.tv_profession);
            final TextView txt_income=(TextView)vi.findViewById(R.id.tv_income);
            final TextView txt_age   = (TextView)vi.findViewById(R.id.tv_age);
            final TextView txt_hight   = (TextView)vi.findViewById(R.id.tv_hight);
            final TextView txt_cast   = (TextView)vi.findViewById(R.id.tv_cast);
            final TextView txt_subcast   = (TextView)vi.findViewById(R.id.tv_subcast);
            final ImageView img_people_pic = (ImageView)vi.findViewById(R.id.img_people_pic);

            LinearLayout layout_customPeople=(LinearLayout)vi.findViewById(R.id.layout_customPeople);


            int srno = position + 1;

            final String uId=attDetails_list.get(position).getuId();
            final String full_name = attDetails_list.get(position).getFullName();
            Log.i("full_name","full_name "+full_name);
            final String age    = attDetails_list.get(position).getage();
            Log.i("age","age "+age);
            final String hight = attDetails_list.get(position).gethight();
            Log.i("hight",hight+hight);
            final  String cast=attDetails_list.get(position).getcast();
            Log.i("cast",cast);
            final String subcast=attDetails_list.get(position).getsubcast();
            Log.i("subcast",subcast);
            final  String profe="test";
                    //attDetails_list.get(position).getprofe();
           // Log.i("profe",profe);
            final String income="tesr";
                   // attDetails_list.get(position).getincome();
            //Log.i("income",income);


            String photo = attDetails_list.get(position).getProfile_photo();
            Log.i("adapter","photo="+photo);

            txt_uID.setText(uId);
            txt_fullName.setText(full_name);
            txt_age.setText(age);
            txt_hight.setText(hight);
            txt_cast.setText(cast);
            txt_subcast.setText(subcast);
            txt_profession.setText(profe);


            layout_customPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    String userid=txt_uID.getText().toString();
                    Intent intent=new Intent(getActivity(),SathiProfileActivity.class);
                    intent.putExtra("userid_people",userid);
                    //intent.putExtra("people_flag",true);
                    startActivity(intent);
                    ///finish();

                    //loadFragment(new SathiProfile());
                }
            });

            String image_url = "";
            Log.i("adapter","image_url=="+image_url);

          /*  Picasso.with(getActivity()).load(image_url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(img_people_pic, new Callback()
                    {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable)img_people_pic.getDrawable()).getBitmap();
                            RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getActivity().getResources(),bitmap);
                            //round.setCornerRadius(Math.max(bitmap.getWidth(),bitmap.getHeight())/2.0f);
                            round.setCornerRadius(10);
                            round.setCircular(true);
                            img_people_pic.setImageDrawable(round);
                        }

                        @Override
                        public void onError()
                        {
                            //display default image on error...
                            // Toast.makeText(context,"fail to set image",Toast.LENGTH_SHORT).show();
                        }
                    });*/

            return vi;
        }

        public void filter(String charText)
        {
            charText = charText.toLowerCase(Locale.getDefault());
            attDetails_list.clear();

            if (charText.length() == 0) {

                attDetails_list.addAll(citylist);
            }
            else {
                int i = 0;
                for (get_set_AttListing wp : citylist)
                {
                    if (wp.getFullName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        attDetails_list.add(wp);
                    }
                }
            }

            notifyDataSetChanged();
        }
    }


    private void updateListView()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                int end = startIndex + 13;
                Log.i("end","updateListView"+end);
                for (int i = startIndex; i < end; i++)
                {
                    Log.i("startIndex","==updateListView"+startIndex);
                    try
                    {
                        get_set_AttListing get_set = new get_set_AttListing();
                        JSONObject object = jsonArray.getJSONObject(i);

                        firstName = object.getString(TAG_firstName);
                        lastName = object.getString(TAG_lastName);
                        fullName = firstName + " " + lastName;


                        get_set.setuId(object.getString(TAG_uId));
                        get_set.setFullName(fullName);
                        get_set.setage(object.getString(TAG_age));
                        get_set.sethight(object.getString(TAG_hight));
                        get_set.setcast(object.getString(TAG_cast));
                        get_set.setcast(object.getString(TAG_cast));
                        get_set.setsubcast(object.getString(TAG_sub_cast));

                        get_set.setProfile_photo("tttttt");



                        array_list1.add(get_set);

                        arraylenght = jsonArray.length();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (arraylenght > startIndex)
                {
                    if (attList_list.getFooterViewsCount() == 0) {
                        attList_list.addFooterView(loadMoreView);
                    }

                    bar.setVisibility(View.GONE);
                    load_more.setText("No more data");
                }
                adapter = new attListAdapter(getActivity(), array_list1);
                attList_list.setAdapter(adapter);

                startIndex = adapter.getCount();
                attList_list.setSelection(total - lastVisible);
                adapter.notifyDataSetChanged();

            }
        }, 2000);
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
