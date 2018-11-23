package com.banjarasathi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;*/
//test demo check for push
@SuppressWarnings("deprecation")
public class BaseActivity extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private android.app.Fragment fragment = null;
   // public ExpandableListView expListView;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapter listAdapter;
    public ExpandableListView expListView;

    Toolbar toolbar;

    private List<String> listDataHeader;

    UserSession session;

    static int[] icon = { R.drawable.my_profile_menu_icon,
                          R.drawable.my_profile_menu_icon,
            R.drawable.my_profile_menu_icon,

            R.drawable.my_profile_menu_icon,

            R.drawable.my_profile_menu_icon,

            R.drawable.my_profile_menu_icon,

                            R.drawable.about_us_menu_icon,
                          R.drawable.logout_menu_icon,

                          };
   /* static int[] icon_att = { R.drawable.dashboard_icon,
            R.drawable.my_profile_menu_icon,
            R.drawable.event_icon,
            R.drawable.directory_menu_icon,
            R.drawable.attendance,
            R.drawable.leave_menu_icon,
            R.drawable.account_icon,
            R.drawable.my_information_icon,

    };

    static  int[] icon_child_leave = {
            R.drawable.apply_leave,
            R.drawable.holidays,
            R.drawable.history,
            R.drawable.approved,
            R.drawable.pending
            };//R.drawable.my_profile_menu_icon,

    static  int[] icon_child={R.drawable.salary_menu_icon


     };

    static  int[] icon_child1 = {
                                    R.drawable.about_us_menu_icon,
                                    R.drawable.settings_menu_icon,
                                    R.drawable.logout_menu_icon};//R.drawable.my_profile_menu_icon,



    static  int[] icon_child_event =
            {
            R.drawable.birthday_icon,
            R.drawable.work_anniversary_icon,
            R.drawable.marriage_anniversary_icon};//R.drawable.my_profile_menu_icon,
*/
    static  int[]icon_arrow={R.drawable.documents_menu_icon};
    View view_Group;

    private static final String PREFER_NAME = "MyPref";
    private static final int FILTER_ID = 0;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    SharedPreferences pref;
    String uId,reporting_Manager;
    public static String sales_attendance_person;
    String firstName, LastName, Email;
    //UserSessionManager session;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
       // session = new UserSessionManager(getApplicationContext());

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //expListView=(ExpandableListView) findViewById(R.id.left_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = mDrawerTitle = getTitle();

        //UserSession   session=new UserSession(getApplicationContext());
        pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        uId=pref.getString("uid","");
        Log.i("uId","uId"+uId);
        reporting_Manager=pref.getString("reporting_manager","");
        Log.i("reporting_Manager_Base","reporting_Manager"+reporting_Manager);

        sales_attendance_person=pref.getString("sales_attendance","");
        Log.i("sales_attendance_person",""+sales_attendance_person);

        session = new UserSession(getApplicationContext());


        setUpDrawer();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        makeActionOverflowMenuShown();

    }


    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // display view for selected nav drawer item
            setUpDrawer();
        }
    }
    // actionbar over flow icon
    public void makeActionOverflowMenuShown()
    {
        // devices with hardware menu button (e.g. Samsung ) don't show action
        // overflow menu
        try {
            final ViewConfiguration config = ViewConfiguration.get(this);
            final Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (final Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }

    /**
     *
     * Get the names and icons references to build the drawer menu...
     */
    public void setUpDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerListener);
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
        prepareListData();

        /*LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.side_menu_header, null, false);
        expListView.addHeaderView(listHeaderView);

        TextView txtFirstname = (TextView)findViewById(R.id.userFirstName);
        txtFirstname.setText(firstName);
        TextView txtLastname = (TextView)findViewById(R.id.userLastName);
        txtLastname.setText(LastName);
        TextView txtEmail = (TextView)findViewById(R.id.userEmailId);
        txtEmail.setText(Email);
        Log.i("listDataChild159",""+listDataChild);*/
        Log.i("listDataHeader55",""+listDataHeader);
        Log.i("listDataChild55",""+listDataChild);
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listDataChild);//listDataChild
        Log.i("listAdapter159",""+listAdapter);
        Log.i("listAdapter159",""+listAdapter.isEmpty());
        expListView.setAdapter(listAdapter);
        //expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                v.setSelected(true);

                    switch (groupPosition) {

                        // booking
                        case 0:
                            Intent intent = new Intent(getApplicationContext(), Register.class);
                            startActivity(intent);
                            finish();
                            break;

                        // dispatch
                        case 1:


                            break;
                        case 2:
                      /*  Intent intent2 = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent2);
                        finish();*/
                            break;

                        case 3:
                            Intent intent3 = new Intent(getApplicationContext(), Register.class);
                            startActivity(intent3);
                            finish();
                            break;

                        case 4:
                            Intent intent_4 = new Intent(getApplicationContext(), Register.class);
                            startActivity(intent_4);
                            finish();
                            break;
                        case 5:
                        /*Intent intent2 = new Intent(getApplicationContext(), NewFed.class);
                        startActivity(intent2);
                        finish();*/
                            break;
                        case 6:
                        /*Intent intent2 = new Intent(getApplicationContext(), NewFed.class);
                        startActivity(intent2);
                        finish();*/
                            break;
                        case 7:
                       /* Intent intent3 = new Intent(getApplicationContext(), NewFed.class);
                        startActivity(intent3);
                        finish();*/
                            break;
                        default:
                            break;

                    }

                return false;
            }
        });
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // setbackground color for list that is selected in child group
                v.setSelected(true);
                if (view_Group != null) {
                }
                view_Group = v;

                switch (groupPosition)
                {

                        // booking
                        case 0:
                            switch (childPosition)
                            {

                                case 0:
                               /* Intent intent = new Intent(getApplicationContext(), ViewBooking.class);
                                startActivity(intent);
                                finish();*/
                                    break;

                                default:
                                    break;
                            }
                            break;


                        case 1:
                            switch (childPosition) {

                                case 0:
                               /* Intent intent = new Intent(getApplicationContext(), ViewBooking.class);
                                startActivity(intent);
                                finish();*/
                                    break;

                                default:
                                    break;
                            }
                            break;

                        case 2:
                            switch (childPosition) {
                                case 0:
                               /* Intent intent = new Intent(getApplicationContext(), ViewBooking.class);
                                startActivity(intent);
                                finish();*/
                                    break;

                                default:
                                    break;
                            }
                            break;
                        // tariff
                        case 3:
                            switch (childPosition) {

                                case 0:
                                /*Intent intent = new Intent(getApplicationContext(), NewFed.class);
                                startActivity(intent);
                                finish();*/
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 4:
                            switch (childPosition) {

                                case 0:
                                /*Intent intent = new Intent(getApplicationContext(), NewFed.class);
                                startActivity(intent);
                                finish();*/
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 5:
                            switch (childPosition) {
                                case 0:
                               /* Intent intent = new Intent(getApplicationContext(), ViewBooking.class);
                                startActivity(intent);
                                finish();*/
                                    break;

                                default:
                                    break;
                            }
                            break;

                        case 6:
                            switch (childPosition)
                            {
                                case 0:
                               /* Intent intent = new Intent(getApplicationContext(), ViewBooking.class);
                                startActivity(intent);
                                finish();*/
                                    break;

                                default:
                                    break;
                            }
                            break;
                        case 7:
                            switch (childPosition)
                            {
                                case 0:
                               /* Intent intent = new Intent(getApplicationContext(), ViewBooking.class);
                                startActivity(intent);
                                finish();*/
                                    break;

                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }

                expListView.setItemChecked(childPosition, true);
                expListView.setSelection(groupPosition);
                mDrawerLayout.closeDrawer(expListView);
                return false;
            }
        });
}

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState); // Sync the toggle state after
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); // Pass any configuration
        // change to the drawer
        // toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            if (mDrawerLayout.isDrawerOpen(expListView))
            {
                mDrawerLayout.closeDrawer(expListView);
            }
            else
            {
                mDrawerLayout.openDrawer(expListView);
            }
        }
        return super.onOptionsItemSelected(item);
    }

// Catch the events related to the drawer to arrange views according to this
// action if necessary...
private DrawerListener mDrawerListener = new DrawerListener()
{

    @Override
    public void onDrawerStateChanged(int status)
    {

    }
    @Override
    public void onDrawerSlide(View view, float slideArg)
    {

    }
    @Override
    public void onDrawerOpened(View view) {
        getSupportActionBar().setTitle(mDrawerTitle);
        // calling onPrepareOptionsMenu() to hide action bar icons
        supportInvalidateOptionsMenu();
    }
    @Override
    public void onDrawerClosed(View view)
    {
        getSupportActionBar().setTitle(mTitle);
        // calling onPrepareOptionsMenu() to show action bar icons
        supportInvalidateOptionsMenu();
    }
};
    private void prepareListData()
    {
        String[] leave_1={};
        String[] attendance_1={};
        listDataHeader = new ArrayList<String>();

        listDataChild = new HashMap<String, List<String>>();

        String[] array = getResources().getStringArray(R.array.nav_drawer_items);
            //String[] array={"abc"};
            listDataHeader = Arrays.asList(array);

            // Adding child data
            // view booking
            List<String> dash = new ArrayList<String>();
            String[] dash_1 = getResources().getStringArray(R.array.My_Profile);
            dash = Arrays.asList(dash_1);

            List<String> newfed = new ArrayList<String>();
            String[] newfed_1 = getResources().getStringArray(R.array.Home);
            newfed = Arrays.asList(newfed_1);


            // dispatch
            List<String> event = new ArrayList<String>();
            String[] event_1 = getResources().getStringArray(R.array.notification);
            event = Arrays.asList(event_1);


            List<String> directory = new ArrayList<String>();
            String[] directory_1 = getResources().getStringArray(R.array.news);
            directory = Arrays.asList(directory_1);


            List<String> attendance = new ArrayList<String>();
            attendance_1 = getResources().getStringArray(R.array.aboutus);
            attendance = Arrays.asList(attendance_1);


            List<String> leave = new ArrayList<String>();
            leave_1 = getResources().getStringArray(R.array.banjara_history);
            leave = Arrays.asList(leave_1);

            List<String> information = new ArrayList<String>();
            String[] information_1 = getResources().getStringArray(R.array.account_set);
            information = Arrays.asList(information_1);


            // dispatch
            List<String> account = new ArrayList<String>();
            String[] account_1 = getResources().getStringArray(R.array.logout);
            account = Arrays.asList(account_1);


            // assigning values to menu and submenu
            listDataChild.put(listDataHeader.get(0), dash); // Header, Child data
            listDataChild.put(listDataHeader.get(1), newfed); // Header, Child data
            listDataChild.put(listDataHeader.get(2), event); // Header, Child data
            listDataChild.put(listDataHeader.get(3), directory);
            listDataChild.put(listDataHeader.get(4), attendance);
            listDataChild.put(listDataHeader.get(5), leave);
            listDataChild.put(listDataHeader.get(6), information); // Header, Child data
            listDataChild.put(listDataHeader.get(7), account);

    }
}
