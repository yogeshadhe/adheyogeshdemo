package com.banjarasathi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import org.apache.http.client.HttpClient;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseNavigation extends AppCompatActivity
{
//implements MyBooking.DataPassListener
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Toolbar toolbar;

    private static final String PREFER_NAME = "MyPref";//username paass and uid
    public static final String TagLname = "lastName";
    public static final String Tagname = "firstName";
    public static final String Tagemail = "email";
    public static final String uid = "uId";

    ////////////
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "cityId";
    private static final String TAG_Cityname = "ctname";

    JSONArray city = null;
    SharedPreferences pref;

    ProgressDialog progressDialog;
    ProgressDialog dialog = null;
    List<String> list2 = new ArrayList<String>();
    //ConnectionDetector cd;
    //UserSessionManager session;
    //public DatabaseHandler db;

    ImageView edit_profile;
    private ImageView Dialer, CCRlogo;

    int version_code;

    String myJSON;
    String myJson,Packagename;
    String FName, uId, M_name;
    String Url,IPaddress,APIkey;
    String L_name, Lname, Email,mobile;
    UserSession userSession;

    ////////////

    ProgressDialog Dialog1;
    public static List<String> source_city = new ArrayList<String>();
    public static List<String> destination_city = new ArrayList<String>();

    boolean allowRefresh = false;
    boolean backPressTwice = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navigation);
        allowRefresh = false;

        mTitle = mDrawerTitle = getTitle();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //cd = new ConnectionDetector(getApplicationContext());
        //Url = cd.geturl();
        //IPaddress = cd.getLocalIpAddress();
        //APIkey = cd.getAPIKEY();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        userSession=new UserSession(BaseNavigation.this);

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_code = info.versionCode;
            Packagename = info.packageName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.i("NameNotFoundException", e.toString());
        }

       // checkversion();

        Dialer = (ImageView) findViewById(R.id.ccrnumber);
        Dialer.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8275937714"));
                startActivity(intent);
            }
        });

        pref=getApplicationContext().getSharedPreferences(PREFER_NAME,getApplicationContext().MODE_PRIVATE);
        uId = (pref.getString("uId", ""));
        Log.i("uId",""+uId);
        FName = (pref.getString("frist_name", ""));
         M_name = (pref.getString("M_name", ""));
        L_name = (pref.getString("last_name", ""));
        Email = (pref.getString("email", ""));
        mobile=(pref.getString("mobile_no",""));

        //session = new UserSessionManager(getApplicationContext());

        if (getSupportActionBar() != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*if (findViewById(R.id.container) != null)
        {
            if (savedInstanceState != null) 
            {
                return;
            }

            //MyBooking myBooking = new MyBooking();
            android.app.Fragment fragment = null;

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myBooking).commit();
        }
      */
        /*CCRlogo = (ImageView) findViewById(R.id.ccrlogo);
        CCRlogo.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                Intent in = new Intent(getApplicationContext(), BaseNavigation.class);
                startActivity(in);
                finish();
            }
        });*/

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.list1);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.headerview_new, null, false);
        mDrawerList.addHeaderView(listHeaderView);

        TextView name = (TextView)listHeaderView.findViewById(R.id.profile_name);
        TextView email = (TextView)listHeaderView. findViewById(R.id.login_email);
        edit_profile = (ImageView)listHeaderView.findViewById(R.id.edit_profile);
        name.setText(FName + " " + M_name+" "+L_name);
        email.setText(mobile);

        edit_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {

                 Fragment  fragment = new User_Profile();
                //finish();


               /* myprofile my_profile = new myprofile();//Get Fragment Instance
                Bundle data = new Bundle();
                //data.putString("edttext", "1");
                //my_profile.setArguments(data);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, test).commit();
                NavDrawerListAdapter.setSelectedPosition(1);
                adapter.notifyDataSetChanged();
                mDrawerLayout.closeDrawer(mDrawerList)*/;
            }
        });
        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        /*navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        */navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));

        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        NavDrawerListAdapter.setSelectedPosition(0);
        adapter.notifyDataSetChanged();

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(BaseNavigation.this, mDrawerLayout, toolbar, //nav menu toggle icon
                R.string.drawer_open,
                R.string.drawer_close)
        {
            public void onDrawerClosed(View view) 
            {
                if (getActionBar() != null) 
                {
                    getActionBar().setTitle(mDrawerTitle);
                }
                if (getSupportActionBar() != null) 
                {
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setTitle(mTitle);
                }
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) 
            {
                if (getActionBar() != null)
                {
                    getActionBar().setTitle(mDrawerTitle);
                }
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) 
        {
            displayView(0);
        }
    }

    /*public void checkversion()
    {
        try
        {
            String urlnew =""+Url+"/versionCheck/?";
            String uri = Uri.parse(urlnew)
                    .buildUpon()
                    .appendQueryParameter("ptopVersion", String.valueOf(version_code))
                    .appendQueryParameter("uId", Uid)
                    .appendQueryParameter("type", "0")
                    .appendQueryParameter("ApiKey", APIkey)
                    .appendQueryParameter("UserID", "1212")
                    .appendQueryParameter("UserIPAddress", IPaddress)
                    .appendQueryParameter("UserAgent", "androidApp")
                    .appendQueryParameter("responsetype", "2")
                    .build().toString();

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            myJson = response;

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        JSONArray json = new JSONArray(myJson);
                        //    Log.i("json", "" + json);
                        JSONObject jsonObject = json.getJSONObject(0);
                        String resp = jsonObject.getString("responseCode");
                        String respmsg = jsonObject.getString("responseMessage");
                        final String Status_res_massge1 = respmsg.substring(2, respmsg.length() - 2);

                        if(resp.equals("0"))
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseNavigation.this);
                            alertDialogBuilder.setMessage(Status_res_massge1)
                                    .setTitle("update app")
                                    .setCancelable(false)
                                    .setPositiveButton(" Yes ", new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+Packagename+"&hl=en"));
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton(" No ", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                                    startMain.addCategory(Intent.CATEGORY_HOME);
                                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startMain);
                                    finish();
                                }
                            });

                            AlertDialog alert = alertDialogBuilder.create();
                            alert.show();
                        }
                    }
                    catch (JSONException j)
                    {
                        j.printStackTrace();
                    }
                }
            });
        }
        catch (IOException e)
        {
            Log.i("Error", "" + e.toString());
        }
    }*/
    
   /* @Override
    public boolean onOptionsItemSelected(MsssssssssssssssssssenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public void displayView(int position) 
    {
        Fragment fragment = new Fragment();
        switch (position) 
        {
            case 0:
                fragment = new Userlist_fragment();

                break;

            case 1:
               // fragment = new Userlist_fragment();

                break;


            case 2:
                fragment = new User_Profile();

                break;
            
         /*   case 3:
                fragment = new User();

                break;
            
            case 4:
                fragment = new myprofile();
                break;*/
            
            case 3:


                break;
            
            case 4:

                fragment = new Reset_fragment();

                break;
            
            case 5:
               // if (cd.hasConnection(BaseNavigation.this))
                //{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.CarBookingIndia&hl=en"));
                startActivity(intent);
                //fragment = new HomeFragment();
               /* /}
                else
                {
                    Toast.makeText(BaseNavigation.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }*/
                break;
            
            case 6:


                break;

            case 7:
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(BaseNavigation.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alertDialog.setTitle("LogOut");
                alertDialog.setMessage("Do you want to Logout from application?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        userSession.logout();
                        Intent intent=new Intent(BaseNavigation.this,Login_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                alertDialog.show();


                break;

            default:
                break;
        }

        if (fragment != null) 
        {
            FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.addToBackStack;
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } 
        else
        {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            NavDrawerListAdapter.setSelectedPosition(position - 1);
            displayView(position);
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        if (getActionBar() != null)
        {
            getActionBar().setTitle(mTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
   /* @Override
    public void passData(String data)
    {
        Cancellations fragmentB = new Cancellations();
        Bundle args = new Bundle();
        args.putString(Cancellations.DATA_RECEIVE, data);
        fragmentB.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragmentB).commit();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        allowRefresh = true;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (allowRefresh)
        {
            allowRefresh = false;
            Intent intent = new Intent(this, BaseNavigation.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed()
    {
        if (backPressTwice)
        {
            super.onBackPressed();
            //finish();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
            return;
        }
        this.backPressTwice = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        handler.postDelayed(mExitRunnable, 2000);

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0)
        {
            super.onBackPressed();
            //additional code
        }
        else
        {
            getFragmentManager().popBackStack();
        }

    }

    public void alertDialoge()
    {
        Log.i("Alert", "Alert");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You will be logout from this App")
                .setTitle("")
                .setCancelable(false)
                .setPositiveButton(" Yes ", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //session.logoutUser();
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton(" No ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            backPressTwice = false;
        }
    };

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i("onPause", "onpasued_basenavigation");
        handler.removeCallbacks(mExitRunnable);
    }
}
