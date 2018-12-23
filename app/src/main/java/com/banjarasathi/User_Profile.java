package com.banjarasathi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import  android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link User_Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link User_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    private static final String PREFER_NAME = "MyPref";//username paass and uid

    //state..
    private static final String Tag_state_msg = "msg";
    private static final String Tag_state_responsecode = "responsecode";
    private static final String Tag_state_responsedata = "responsedata";
    private static final String Tag_state_stateId = "stateId";
    private static final String Tag_state_stateName = "stateName";

    //city..
    private static final String Tag_city_msg = "msg";
    private static final String Tag_city_responsecode = "responsecode";
    private static final String Tag_city_responsedata = "responsedata";
    private static final String Tag_city_cityId = "cityId";
    private static final String Tag_city_cityName = "cityName";

    Toolbar toolbar;

    String http,domain_url,api_name,img_url_main;

    boolean flag_profileView,flag_baseActivity;

    Button btn_basic_profile,btn_caste,btn_profession;

    LinearLayout layout_basicProfile,layout_caste,layout_profession;

    AutoCompleteTextView ac_gender;
    EditText ed_select_dob;
    int day,month,year;
    String birth_date;

    ProgressDialog progressDialog;
    CoordinatorLayout coordinator_layout;
    RequestQueue requestQueue;
    String response_photo;

    //pref data...
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String uId;

    //state data...(basic prifile)
    HashMap<String,String> map_State;
    ArrayList<HashMap<String,String>> list_state;
    ArrayList<String> list_state2;
    AutoCompleteTextView ac_state;
    String state_ID;
    boolean flag_basicCity = false;

    //city data...(basic)
    HashMap<String,String> map_city;
    ArrayList<HashMap<String,String>> list_cityMap;
    ArrayList<String> list_cityName;
    AutoCompleteTextView ac_city;
    EditText ed_address;
    String city_ID;

    //blood group data...
    HashMap<String,String> map_blood;
    ArrayList<HashMap<String,String>> list_bloodMap;
    ArrayList<String> list_blood;
    String blood_ID;

    //marital status data...
    HashMap<String,String> map_marital;
    ArrayList<HashMap<String,String>> list_maritalMap;
    ArrayList<String> list_marital;
    String marital_ID;

    //basic profile...
    Button btn_basicProfile_submit;
    EditText ed_B_firstName,ed_B_middleName,ed_B_gender,ed_B_dob,ed_B_lastName,ed_B_mothersName,ed_B_mobileNo,ed_B_emailID;
    EditText ed_B_taluka,ed_B_village,ed_B_address;
    String firstName,lastName,middleName,mothersName,mobileNo,emailID,taluka,village,address,state,city,genderID,dob,dob_final;

    View v;
    //cate...
    AutoCompleteTextView ac_C_bloodGroup,ac_C_marital;
    EditText ed_C_caste,ed_C_subCaste,ed_C_gotra,ed_C_birthTime,ed_C_marriageDate;
    int mHour, mMinute,day_c,month_c,year_c;
    String format,birth_time,marriage_date;
    String subcaste,timeof_birth,marital_status1,marriage_anni,blood_group,marriage_anni_final;
    Button btn_caste_submit;

    //profession...
    HashMap<String,String> map_cityOFF;
    ArrayList<HashMap<String,String>> list_cityMapOFF;
    ArrayList<String> list_cityNameOFF;
    AutoCompleteTextView ac_P_officeState,ac_P_officeCity;
    String state_ID_office,city_ID_office;
    boolean flag_Officecity = false;
    EditText ed_P_highEducation,ed_P_degreeDetails,ed_P_officeName,ed_P_incomeRange,ed_P_officeAddress;
    EditText ed_P_hobbies,ed_P_achivements;
    Button btn_profession_submit;
    String highEdu,degreeDetails,officeName,incomeRange,officeAddress,hobbies,achivements;

    String edducation,profesion,income,caste,subCaste,gotra,family_details;
    AutoCompleteTextView ac_edducation,ac_profesion,ac_income,ac_caste,ac_subCaste,ac_gotra;
    TextView  ed_family_details;

    TextView tv_name_ProEdit;

    //for selecting image...
    String userChoosenTask = "";
    int READ_PERMISSION = 1;
    private static final int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;
    private String imgPath;
    String selectedImagePath = "";
    //TextView ED_IMAGE_PATH;
    String image_encoded;
    private Bitmap bitmap;
    ArrayList<String> list_gothr;
    ImageView img_profileEdit;



    public User_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static User_Profile newInstance(String param1, String param2) {
        User_Profile fragment = new User_Profile();
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
         v= inflater.inflate(R.layout.profile_edit_step_1, container, false);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        requestQueue = Volley.newRequestQueue(getActivity());

        btn_basic_profile = (Button)v.findViewById(R.id.btn_basic_profile);
        btn_caste = (Button)v.findViewById(R.id.btn_caste);
        btn_profession = (Button)v.findViewById(R.id.btn_profession);

        coordinator_layout = (CoordinatorLayout)v.findViewById(R.id.coordinator_layout);
        layout_basicProfile = (LinearLayout)v.findViewById(R.id.layout_basicProfile);
        layout_caste = (LinearLayout)v.findViewById(R.id.layout_caste);
        layout_profession = (LinearLayout)v.findViewById(R.id.layout_profession);
        img_profileEdit = (ImageView)v.findViewById(R.id.img_profileEdit);




        pref=getActivity().getSharedPreferences(PREFER_NAME,getActivity().MODE_PRIVATE);
        uId = (pref.getString("uId", ""));
        Log.i("uId",""+uId);

        //basic profile...
        basicProfileData();
        basicProfileViewOnError();


        //profession data...
        professionData();
        profesionViewOnError();

        //cate..
        casteData();
        casteViewOnError();


        //profile 1
        btn_basic_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_basic_profile.setTextColor(getResources().getColor(R.color.WhiteTextColor));
                btn_basic_profile.setBackgroundResource(R.drawable.tab_active);

                btn_caste.setTextColor(getResources().getColor(R.color.BlackTextColor));
                btn_caste.setBackgroundColor(Color.TRANSPARENT);

                btn_profession.setTextColor(getResources().getColor(R.color.BlackTextColor));
                btn_profession.setBackgroundColor(Color.TRANSPARENT);


                layout_basicProfile.setVisibility(View.VISIBLE);
                layout_caste.setVisibility(View.GONE);
                layout_profession.setVisibility(View.GONE);

            }
        });

        //btn profile 2
        btn_caste.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                btn_caste.setTextColor(getResources().getColor(R.color.WhiteTextColor));
                btn_caste.setBackgroundResource(R.drawable.tab_active);

                btn_basic_profile.setTextColor(getResources().getColor(R.color.BlackTextColor));
                btn_basic_profile.setBackgroundColor(Color.TRANSPARENT);

                btn_profession.setTextColor(getResources().getColor(R.color.BlackTextColor));
                btn_profession.setBackgroundColor(Color.TRANSPARENT);

                layout_basicProfile.setVisibility(View.GONE);
                layout_profession.setVisibility(View.GONE);
                layout_caste.setVisibility(View.VISIBLE);
            }
        });
        // btn  profile 3
        btn_profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_profession.setTextColor(getResources().getColor(R.color.WhiteTextColor));
                btn_profession.setBackgroundResource(R.drawable.tab_active);

                btn_basic_profile.setTextColor(getResources().getColor(R.color.BlackTextColor));
                btn_basic_profile.setBackgroundColor(Color.TRANSPARENT);

                btn_caste.setTextColor(getResources().getColor(R.color.BlackTextColor));
                btn_caste.setBackgroundColor(Color.TRANSPARENT);

                layout_profession.setVisibility(View.VISIBLE);
                layout_basicProfile.setVisibility(View.GONE);
                layout_caste.setVisibility(View.GONE);

            }
        });

        getAllProfileData();

        //for update profile pic...
        updateProfilePic();

        /*ac_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ac_state.showDropDown();
                state_ID = "";
                return true;

            }
        });
        ac_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String state = ac_state.getText().toString();
                state_ID = list_state.get(i).get(state);
                Log.i("stateID","state=="+state+" state_ID=="+state_ID);

                list_cityName.clear();
                list_cityMap.clear();
                ac_city.setAdapter(null);
                ac_city.setText("");
                ac_city.setHint("Select City");

                flag_basicCity = true;
                getCityData();
            }
        });
        ac_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac_state.setText("");
            }
        });

        ac_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ac_city.showDropDown();
                city_ID = "";
                return true;
            }
        });
        ac_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String city = ac_city.getText().toString();
                city_ID = list_cityMap.get(i).get(city);
                Log.i("city","city=="+city+" city_ID=="+city_ID);
            }
        });
        ac_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac_city.setText("");
            }
        });*/

        return v;


    }


    //get all profile data...
    public void getAllProfileData()
    {
        //http://banjarasathi.com/Api/userlist.php?uId=11
        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Getting data...", true);
        progressDialog.show();

        try
        {
            String url ="http://banjarasathi.com/Api/userlist.php?";
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

            StringRequest request = new StringRequest(Request.Method.GET,profile_url,successListener,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onGetProfileResponse(String json)
    {



        /*{"success":1,
                "data":[{"uId":"11","candidateId":"USER0011","firstName":"manisha",
                "middleName":"manisha","lastName":"jadhav","gender":"Male",
                "DOB":"2018-06-14","photo":"","education":"mca",
                "profesion":"ca","income":"1000","caste":"Banjara","subcaste":"Labhani",
                "gotra":"Banjara","height":"5.0","currentStateId":"1","currentCityId":"3",
                "currentAddress":"test","currentDateTime":"2018-06-06 18:43:49",
                "updatedDateTime":"2018-06-06 18:43:49","status":"1","loginId":"11",
                "mobileno":"1122334455","emailid":"manisha@gmail.com",
                "password":"e10adc3949ba59abbe56e057f20f883e",
                "currentdatetime":"2018-06-07 15:54:07","age":"0",
                "profilepath":"banjarasathi.com\/Api\/profileupload\/"}],
            "message":"User details"
        }*/
        try
        {
            JSONObject jsonObject1 = new JSONObject(json);
            JSONArray jsonArray=jsonObject1.getJSONArray("data");
            JSONObject object = jsonArray.getJSONObject(0);

            //basic profile...
            String first_nameB = object.getString("firstName");
            String middle_nameB = object.getString("middleName");
            String last_nameB = object.getString("lastName");
            String genderB = object.getString("gender");
            String dobB = object.getString("DOB");
            String mobileNoB = object.getString("mobileno");
            String emailB = object.getString("emailid");


            //tv_name_ProEdit.setText(first_nameB+" "+last_nameB);

            ed_B_firstName.setText(first_nameB);
            ed_B_middleName.setText(middle_nameB);
            ed_B_lastName.setText(last_nameB);

            if (genderB.equals("Male"))
            {
                ac_gender.setText("Male");
            }
            else
            {
                ac_gender.setText("Female");
            }
            ed_select_dob.setText(dobB);
            ed_B_emailID.setText(emailB);
            ed_B_mobileNo.setText(mobileNoB);


            //profession...
            String educationP = object.getString("education");
            String profession = object.getString("profesion");
            String incomeP = object.getString("income");
            String castP = object.getString("caste");
            String subCastP = object.getString("subcaste");
            String gotraP = object.getString("gotra");
           // String familyDetailP = object.getString("");



            ac_edducation.setText(educationP);
            ac_profesion.setText(profession);
            ac_income.setText(incomeP);
            ac_caste.setText(castP);
            ac_subCaste.setText(subCastP);
            ac_gotra.setText(gotraP);
            //ed_family_details.setText(familyDetailP);


            //marriage anniversary
            //caste...
            String state = object.getString("currentStateId");
            String city = object.getString("currentCityId");
            String address = object.getString("currentAddress");


            ac_state.setText(state);
            ac_city.setText(city);
            ed_address.setText(address);


            String photo = object.getString("profilepath");
            Log.i("photo","=="+photo);
            //http://telibandhan.safegird.com/images/userphotos/
            //String img_url = http + domain_url + "/images/userphotos/";
            String img_url = "http://";
            Log.i("url","image="+img_url);
            String final_img_url = img_url+photo;
            Log.i("final_img_url","======================"+final_img_url);

            Picasso.with(getActivity()).load(final_img_url)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE)
                    //.networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(img_profileEdit, new Callback()
                    {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable)img_profileEdit.getDrawable()).getBitmap();
                            RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getActivity().getResources(),bitmap);
                            round.setCornerRadius(Math.max(bitmap.getWidth(),bitmap.getHeight())/2.0f);
                            round.setCornerRadius(10);
                            round.setCircular(true);
                            img_profileEdit.setImageDrawable(round);
                        }

                        @Override
                        public void onError()
                        {
                            //display default image on error...
                            // Toast.makeText(context,"fail to set image",Toast.LENGTH_SHORT).show();
                        }
                    });

            List<String> list_gender = new ArrayList<>();
            list_gender.add("Male");
            list_gender.add("Female");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_gender);
            ac_gender.setAdapter(adapter);

            ac_gender.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ac_gender.showDropDown();
                    return true;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }




    ///profile data
    public void basicProfileViewOnError()
    {



        ed_B_firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_B_firstName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_B_middleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_B_middleName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_B_lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_B_lastName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ac_gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_gender.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed_select_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_select_dob.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_B_emailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_B_emailID.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed_B_mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String no = ed_B_mobileNo.getText().toString();
                if (no.length()>10)
                {
                    ed_B_mobileNo.setError("Number should be less than 10 digit");
                }
                else
                {
                    ed_B_mobileNo.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }
    public void basicProfileData()
    {

        btn_basicProfile_submit = (Button)v.findViewById(R.id.btn_basicProfile_submit);

        ed_B_firstName = (EditText)v.findViewById(R.id.ed_B_firstName);
        ed_B_middleName = (EditText)v.findViewById(R.id.ed_B_middleName);
        ed_B_lastName = (EditText)v.findViewById(R.id.ed_B_lastName);
        ac_gender = (AutoCompleteTextView)v.findViewById(R.id.ac_gender);
        ed_select_dob = (EditText)v.findViewById(R.id.ed_select_dob);
        ed_B_emailID = (EditText)v.findViewById(R.id.ed_B_emailID);
        ed_B_mobileNo = (EditText)v.findViewById(R.id.ed_B_mobileNo);


        ed_select_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                final int d = calendar.get(Calendar.DAY_OF_MONTH);
                final int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        year = i;
                        month = i1+1;
                        day = i2;

                        if (day < 10 && month < 10)
                        {
                            birth_date = "0" + day + "-" + "0" + month + "-" + year;
                            ed_select_dob.setText(birth_date);
                        }
                        else if (day < 10)
                        {
                            birth_date = "0" + day + "-" + month + "-" +year;
                            ed_select_dob.setText(birth_date);
                        }
                        else if (month < 10)
                        {
                            birth_date = day + "-" + "0" + month + "-" +year;
                            ed_select_dob.setText(birth_date);
                        }
                        else
                        {
                            birth_date =  day + "-" + month + "-" + year;
                            ed_select_dob.setText(birth_date);
                        }

                    }
                },y,m,d);

                pickerDialog.show();
            }
        });

        btn_basicProfile_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                firstName = ed_B_firstName.getText().toString();
                middleName = ed_B_middleName.getText().toString();
                lastName = ed_B_lastName.getText().toString();
                emailID = ed_B_emailID.getText().toString();
                mobileNo = ed_B_mobileNo.getText().toString();


                if (firstName.equals(""))
                {
                    ed_B_firstName.setError("Enter First Name");
                }
                else if (middleName.equals(""))
                {
                    ed_B_middleName.setError("Enter Middle Name");
                }
                else if (lastName.equals(""))
                {
                    ed_B_lastName.setError("Enter Last Name");
                }
                else if (emailID.equals(""))
                {
                    ed_B_emailID.setError("Enter Email-ID");
                }
                else if (mobileNo.equals(""))
                {
                    ed_B_mobileNo.setError("Enter Mobile No.");
                }

                else if (ac_gender.getText().toString().equals(""))
                {
                    ac_gender.setError("Select Gender Type");
                }
                else if (ed_select_dob.getText().toString().equals(""))
                {
                    ed_select_dob.setError("Select Date Of Birth");
                }
                else if (mobileNo.length()>10 || mobileNo.length()<10)
                {
                    ed_B_mobileNo.setError("Enter Valid Number");
                }
                else
                {
                    /*//for hiding keyboard....
                    View view1 = getActivity().getCurrentFocus();
                    if (view1 != null)
                    {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }*/

                    if (ac_gender.getText().toString().equals("Male"))
                    {
                        genderID = "Male";
                    }
                    else if (ac_gender.getText().toString().equals("Female")){
                        genderID = "Female";
                    }

                    dob = ed_select_dob.getText().toString();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    try
                    {
                        Date date = sdf1.parse(dob);
                        dob_final = sdf2.format(date);
                        Log.i("profileED","dateFinal=="+dob_final+" genderID="+genderID);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.i("email","=="+android.util.Patterns.EMAIL_ADDRESS.matcher(emailID).matches());

                    boolean check;
                    Pattern p;
                    Matcher m;
                    String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    p = Pattern.compile(EMAIL_STRING);
                    m = p.matcher(emailID);
                    check = m.matches();
                    Log.i("email","check=="+check);


                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailID).matches())
                    {
                        sendBasicProfileData();
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(coordinator_layout,"Please Enter Valid Email-ID",Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                        snackbar.show();
                    }
                }

            }
        });
    }
    public void sendBasicProfileData()
    {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Getting data...", true);
        progressDialog.show();

        try
        {
            String url = http + domain_url + api_name;
            String query = String.format("userId=%s&first_name=%s&middleName=%s&lastName=%s&genderID=%s&dob_final=%s&emailID=%s&mobileNo=%s",
                    URLEncoder.encode(uId,"UTF-8"),
                    URLEncoder.encode(firstName,"UTF-8"),
                    URLEncoder.encode(middleName,"UTF-8"),
                    URLEncoder.encode(lastName,"UTF-8"),
                    URLEncoder.encode(genderID,"UTF-8"),
                    URLEncoder.encode(dob_final,"UTF-8"),
                    URLEncoder.encode(emailID,"UTF-8"),
                    URLEncoder.encode(mobileNo,"UTF-8"));
            //URLEncoder.encode("6","UTF-8"));

            String url_basicProfile = url + query;
            Log.i("url","basic=="+url_basicProfile);

            Response.Listener<String> success = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }

                    Log.i("response","basicProfile="+response);
                    // [{"msg":"Success","responsecode":1,"responsedata":""}]
                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String msg = jsonObject.getString("msg");
                        String responsecode = jsonObject.getString("responsecode");

                        if (responsecode.equals("1"))
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg,Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg,Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener()
            {
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

            StringRequest request = new StringRequest(Request.Method.GET,url_basicProfile,success,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

    }


    //profile data
    public void profesionViewOnError()
    {


        ac_edducation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_edducation.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ac_profesion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_profesion.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ac_income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_income.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ac_caste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_caste.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ac_subCaste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_subCaste.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ac_gotra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_gotra.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_family_details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_family_details.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void professionData()
    {

        ac_gotra = (AutoCompleteTextView)v.findViewById(R.id.ac_gotra);

        ac_edducation = (AutoCompleteTextView)v.findViewById(R.id.ac_edducation);
        ac_profesion = (AutoCompleteTextView)v.findViewById(R.id.ac_profesion);

        ac_income = (AutoCompleteTextView)v.findViewById(R.id.ac_income);
        ac_caste = (AutoCompleteTextView)v.findViewById(R.id.ac_caste);
        ac_subCaste = (AutoCompleteTextView)v.findViewById(R.id.ac_subCaste);

        ed_family_details = (EditText)v.findViewById(R.id.ed_family_details);

        btn_profession_submit = (Button)v.findViewById(R.id.btn_profile_submit);

        /*ac_edducation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ac_edducation.showDropDown();
                city_ID_office = "";
                return true;
            }
        });
        ac_edducation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                *//*String off_city = ac_edducation.getText().toString();
                city_ID_office = list_cityMapOFF.get(i).get(off_city);
                Log.i("city_ID_office","=="+city_ID_office+" off_city=="+off_city);*//*
            }
        });
        ac_edducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ac_edducation.setText("");
            }
        });

        //profession
        ac_profesion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ac_profesion.showDropDown();
                city_ID_office = "";
                return true;
            }
        });
        ac_profesion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                *//*String off_city = ac_edducation.getText().toString();
                city_ID_office = list_cityMapOFF.get(i).get(off_city);
                Log.i("city_ID_office","=="+city_ID_office+" off_city=="+off_city);*//*
            }
        });
        ac_profesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ac_profesion.setText("");
            }
        });*/




        list_gothr = new ArrayList<String>();
        list_gothr.add("Banjara");
        list_gothr.add("Labhani");
        list_gothr.add("Gor");

        ArrayAdapter<String> adapter_gotr = new ArrayAdapter<String>(getActivity(),R.layout.dropdown_custom,list_gothr);
        ac_gotra.setAdapter(adapter_gotr);

        ac_gotra.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                ac_gotra.showDropDown();
                return true;
            }
        });


        List<String> list_income = new ArrayList<>();
        list_income.add("10000 to 200000");
        list_income.add("20000 to 300000");
        list_income.add("30000 to 400000");
        list_income.add("40000 and Above");
        ArrayAdapter<String> adapter_income = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_income);

        ac_income.setAdapter(adapter_income);
        Log.i("list_income",""+list_income);
        Log.i("adapter_income",""+adapter_income);
        ac_income.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {

                ac_income.showDropDown();
                return true;
            }
        });

        List<String> list_cast = new ArrayList<>();
        list_cast.add("Banjara");
        list_cast.add("Banjara");
        ArrayAdapter<String> adapter_cast = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_income);
        ac_caste.setAdapter(adapter_cast);

        Log.i("list_cast",""+list_cast);
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





        btn_profession_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                edducation = ac_edducation.getText().toString();
                profesion = ac_profesion.getText().toString();
                income = ac_income.getText().toString();
                caste = ac_caste.getText().toString();
                subCaste = ac_subCaste.getText().toString();
                gotra = ac_gotra.getText().toString();
                family_details = ed_family_details.getText().toString();

                if (edducation.equals(""))
                {
                    ac_edducation.setError("Select Education");
                }
                else if (profesion.equals(""))
                {
                    ac_profesion.setError("Enter Profesion");
                }
                else if (income.equals(""))
                {
                    ac_income.setError("Select Income");
                }
                else if (caste.equals(""))
                {
                    ac_caste.setError("Select caste");
                }
                else if (subCaste.equals(""))
                {
                    ac_subCaste.setError("Select subcaste");
                }
                else if (gotra.equals(""))
                {
                    ac_gotra.setError("Select Gotra");
                }
                else if (family_details.equals(""))
                {
                    ed_family_details.setError("Enter Family Details");
                }
                else
                {

                    //for hiding keyboard....
                   /* View view1 = getActivity().getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }*/

                    sendProfessionData();

                }

            }
        });
    }
    public void sendProfessionData()
    {

        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Getting data...", true);
        progressDialog.show();

        edducation = ac_edducation.getText().toString();
        profesion = ac_profesion.getText().toString();
        income = ac_income.getText().toString();
        caste = ac_caste.getText().toString();
        subCaste = ac_subCaste.getText().toString();
        gotra = ac_gotra.getText().toString();
        family_details = ed_family_details.getText().toString();


        try
        {
            String url = "";
            String query = String.format("userId=%s&edducation=%s&profesion=%s&income=%s&caste=%s&" +
                            "subCaste=%s&gotra=%s&family_details=%s",
                    URLEncoder.encode(uId,"UTF-8"),
                    URLEncoder.encode(edducation,"UTF-8"),
                    URLEncoder.encode(profesion,"UTF-8"),
                    URLEncoder.encode(income,"UTF-8"),
                    URLEncoder.encode(caste,"UTF-8"),
                    URLEncoder.encode(subCaste,"UTF-8"),
                    URLEncoder.encode(gotra,"UTF-8"),
                    URLEncoder.encode(family_details,"UTF-8"));


            String url_profession = url + query;
            Log.i("url","url_profession="+url_profession);

            Response.Listener<String> success = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }

                    Log.i("response","profession=="+response);
                    // [{"msg":"Success","responsecode":1,"responsedata":""}]
                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String msg = jsonObject.getString("msg");
                        String responsecode = jsonObject.getString("responsecode");
                        if (responsecode.equals("1"))
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg,Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg,Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

            StringRequest request = new StringRequest(Request.Method.GET,url_profession,success,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    // third form
    public void casteViewOnError()
    {

        ac_state = (AutoCompleteTextView)v.findViewById(R.id.ac_state);
        ac_city = (AutoCompleteTextView)v.findViewById(R.id.ac_city);
        ed_address = (EditText)v.findViewById(R.id.ed_address);

        ac_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_state.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ac_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ac_city.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ed_address.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void casteData()
    {


        ac_state = (AutoCompleteTextView)v.findViewById(R.id.ac_state);
        ac_city = (AutoCompleteTextView)v.findViewById(R.id.ac_city);
        ed_address = (EditText)v.findViewById(R.id.ed_address);

        btn_caste_submit = (Button)v.findViewById(R.id.btn_cast_submit);

        ac_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ac_state.showDropDown();
                blood_ID = "";
                return true;
            }
        });

        ac_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*String blood = ac_state.getText().toString();
                blood_ID = list_bloodMap.get(i).get(blood);
                Log.i("blood","group="+blood+" ID="+blood_ID);*/
            }
        });
        ac_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ac_state.setText("");
            }
        });

        ac_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ac_city.showDropDown();
                marital_ID = "";
                return true;
            }
        });
        ac_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               /* String marital_status = ac_city.getText().toString();
                marital_ID = list_maritalMap.get(i).get(marital_status);
                Log.i("marital","status="+marital_status+" ID="+marital_ID);*/
            }
        });
        ac_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac_city.setText("");
            }
        });

        btn_caste_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                state = ac_state.getText().toString();
                city = ac_city.getText().toString();
                address = ed_address.getText().toString();


                if (state.equals(""))
                {
                    ac_state.setError("Select State");
                }
                else if (city.equals(""))
                {
                    ac_city.setError("Select City");
                }
                else if (address.equals(""))
                {
                    ed_address.setError("Enter Address");
                }
                else
                {

                    /*//for hiding keyboard....
                    View view1 = ProfileEdit.this.getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }*/

                    sendcasteData();

                }
            }
        });
    }
    public void sendcasteData()
    {
        //http://telibandhan.safegird.com/appapinew.php?
        // userId=2&caste=Bramhin&sub_caste=Bramhin&timeofbirth=12:00:00&
        // gotra=Munibhargava&martial_status=1&marraige_anni=1999-10-01&blood_group=1&pageFlag=7



        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Getting data...", true);
        progressDialog.show();


        state = ac_state.getText().toString();
        city = ac_city.getText().toString();
        address = ed_address.getText().toString();


        try
        {
            String url = http + domain_url + api_name;
            String query = String.format("userId=%s&state=%s&city=%s&address=%s",
                    URLEncoder.encode(uId,"UTF-8"),
                    URLEncoder.encode(state,"UTF-8"),
                    URLEncoder.encode(city,"UTF-8"),
                    URLEncoder.encode(address,"UTF-8"));


            String url_sendCaste = url + query;
            Log.i("url","sendCaste="+url_sendCaste);

            Response.Listener<String> success = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }

                    Log.i("response","sendCaste=="+response);
                    // [{"msg":"Success","responsecode":1,"responsedata":""}]

                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String msg = jsonObject.getString("msg");
                        String responsecode = jsonObject.getString("responsecode");
                        if (responsecode.equals("1"))
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg,Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg,Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

            StringRequest request = new StringRequest(Request.Method.GET,url_sendCaste,success,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    //***********************for upadte image****************************************

    //for update profile pic...
    public void updateProfilePic()
    {
        img_profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageFromGallery();
            }
        });
    }
    //select poto...
    public void selectImageFromGallery()
    {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    userChoosenTask="Take Photo";

                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},READ_PERMISSION);
                    }
                    else
                    {
                        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                        startActivityForResult(intent, CAPTURE_IMAGE);
                    }


                }
                else if (options[item].equals("Choose from Gallery"))
                {

                    userChoosenTask = "Choose from Gallery";

                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},READ_PERMISSION);
                    }
                    else {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);

                    }

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        Log.i("img","path="+imgPath+" uri=="+imgUri);
        return imgUri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PERMISSION)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED)
            {
                if (userChoosenTask.equals("Take Photo"))
                {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    startActivityForResult(intent, CAPTURE_IMAGE);

                }
                else if (userChoosenTask.equals("Choose from Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != android.app.Activity.RESULT_CANCELED)
        {
            if (requestCode == PICK_IMAGE)
            {

                if(Build.VERSION.SDK_INT >19)
                {
                    Uri selectedImage= data.getData();
                    selectedImagePath = getPath(getActivity(),selectedImage);
                    Log.i("selectedImagePath","1=="+selectedImagePath);
                    if (selectedImagePath == null || selectedImagePath.equals(""))
                    {
                        Snackbar snackbar = Snackbar.make(coordinator_layout,"Please Select Profile Photo",Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                        snackbar.show();
                    }
                    else
                    {
                        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                        img_profileEdit.setImageBitmap(bm);


                        //for encode image....
                        encodeImageBase64();

                        //upload image action...1
                        //updateImage();
                        addReimbursement();

                    }
                    //ED_IMAGE_PATH.setText(selectedImagePath);

                }
                else
                {
                    selectedImagePath = getAbsolutePath(data.getData());
                    //ED_IMAGE_PATH.setText(selectedImagePath);
                    decodeFile(selectedImagePath);
                    //ED_IMAGE_PATH.setText(selectedImagePath);
                    Log.i("selectedImagePath","2=="+selectedImagePath);
                    if (selectedImagePath == null || selectedImagePath.equals(""))
                    {
                        Snackbar snackbar = Snackbar.make(coordinator_layout,"Please Select Profile Photo",Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                        snackbar.show();
                    }
                    else
                    {
                        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                        img_profileEdit.setImageBitmap(bm);

                        //for encode image...
                        encodeImageBase64();

                        //upload image action...2
                        //updateImage();
                        addReimbursement();
                    }

                }

            }
            else if (requestCode == CAPTURE_IMAGE)
            {
                selectedImagePath = getImagePath();
                Log.i("selectedImagePath","3=="+selectedImagePath);
                decodeFile(selectedImagePath);
                //ED_IMAGE_PATH.setText(selectedImagePath);
                if (selectedImagePath == null || selectedImagePath.equals(""))
                {
                    Snackbar snackbar = Snackbar.make(coordinator_layout,"Please Select Profile Photo",Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                    snackbar.show();
                }
                else
                {
                    Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                    img_profileEdit.setImageBitmap(bm);

                    //for encode image....
                    encodeImageBase64();

                    //upload image action...3
                    //updateImage();
                    addReimbursement();
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public  String getPath(final Context context, final Uri uri)
    {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public  String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index; index = cursor.getColumnIndexOrThrow(column);
                selectedImagePath = cursor.getString(index);
                Log.i("selected_gal123",""+selectedImagePath);
                //ED_IMAGE_PATH.setText(selectedImagePath);
                //decodeFile(selectedImagePath);

                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public String getAbsolutePath(Uri uri)
    {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        //Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void encodeImageBase64()
    {
        image_encoded = "";
        // if(ED_IMAGE_PATH.length()>0)
        if (selectedImagePath != null || !selectedImagePath.equals(""))
        {

            if (selectedImagePath.length() > 0)
            {
                Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                img_profileEdit.setImageBitmap(bm);

                Bitmap imageScaled = Bitmap.createScaledBitmap(bm, 720, 360, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageScaled.compress(Bitmap.CompressFormat.JPEG, 50, bos);

                byte[] data = bos.toByteArray();

                image_encoded = android.util.Base64.encodeToString(data, Base64.DEFAULT);
                Log.i("BaseImage", image_encoded);
                System.out.print("image_encoded_print" + image_encoded);
                StringBuilder str = new StringBuilder(image_encoded);
                //Log.i("str",""+str);

            }
            else
            {
                Toast.makeText(getActivity(), "Image not found please select again.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinator_layout,"Please Select Profile Photo",Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
            snackbar.show();
        }
    }

    //for upload image action...
    public void updateImage()
    {
       /*     //http://www.banjarasathi.com/Api/profilephotoupload.php?uId=10&fileToUpload=
        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Getting data...", true);
        progressDialog.show();*/

        String image_url ="http://www.banjarasathi.com/Api/profilephotoupload.php?";

        //{"success":1,"message":"Image uploaded"}

        //String image_url ="http://banjarasathi.com/Api/userlist.php?";

        StringRequest request = new StringRequest(Request.Method.POST, image_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                      /*  if (progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }*/
                        Log.i("response_photo123", "==============" + response);
                        //[{"msg":"Success","responsecode":1,"responsedata":"7"}]
                        try
                        {

                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("jsonObject", "==============" + jsonObject);
                            String msg = jsonObject.getString("message");
                            String responsecode = jsonObject.getString("success");
                            Log.i("responsecode", "==============" + responsecode);
                            if (responsecode.equals("1"))
                            {
                               /* Snackbar snackbar = Snackbar.make(coordinator_layout,msg,Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/

                                LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                                View dialogLayout = inflater_alert.inflate(R.layout.success_alert, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setView(dialogLayout);
                                TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                                massage.setText("update");
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
                            else
                            {
                                /*Snackbar snackbar = Snackbar.make(coordinator_layout,msg,Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        /*if (progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }*/
                        if (error.networkResponse == null)
                        {
                            if (error.getClass().equals(TimeoutError.class))
                            {
                               /* Log.i("volley","error==time out--in");
                                //Toast.makeText(Sign_up.this,"error-time out",Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(coordinator_layout,"Server Connection Timeout",Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
                            }
                            if (error.getClass().equals(NoConnectionError.class))
                            {
                               /* Snackbar snackbar = Snackbar.make(coordinator_layout,"Check Internet Connection",Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
                            }
                            if (error.getClass().equals(NetworkError.class))
                            {
                               /* Snackbar snackbar = Snackbar.make(coordinator_layout,"Check Internet Connection id Active",Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
                            }
                            if (error.getClass().equals(Network.class))
                            {/*
                                Snackbar snackbar = Snackbar.make(coordinator_layout,"Check Internet Connection id Active",Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
                            }
                        }
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();

                params.put("uId",uId);//uId
                params.put("fileToUpload",image_encoded);
                Log.i("params",""+params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        Log.i("request_photo",""+request);
        requestQueue.add(request);
    }


    //by using Http Client...
    public void addReimbursement()
    {


        class AddReimbursement extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                /*progressDialog = ProgressDialog.show(getActivity(),"Please wait","Adding your claims...",true);
                progressDialog.show();*/
            }
            @Override
            protected String doInBackground(String... params)
            {
                  /*
                  String parameters = "uId="+uId+"&reimbursClaimTypeId="+claimType_ID +
                  "&reimbursementBillNo="+bill_no+"&reimbursementBillDate="+billdate+
                  "&reimbursementAmount="+amount+"&remarks="+remark+
                  "&reimbursementBill="+image_encoded;
                  */

                String AddClaims_url = "http://www.banjarasathi.com/Api/profilephotoupload.php?";
                Log.i("AddClaims_url","addReimbursement=="+AddClaims_url);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("uId",uId));
                nameValuePairs.add(new BasicNameValuePair("fileToUpload",image_encoded));

                Log.i("nameValuePairs","=="+nameValuePairs);
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(AddClaims_url);
                    Log.i("httpPost", "" + httpPost);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    response_photo = EntityUtils.toString(httpResponse.getEntity());
                    Log.i("response_photo", "response_photo==" +response_photo);

                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                return response_photo;
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
            /*    if (progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }*/
                Log.i("response", "addclaims2=s==" + s);
                if (s == null || s.equals("[]"))
                {
                    Toast.makeText(getActivity(), "Bad Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    //[{"responseMessage":"Inserted SuccessFully.","responseCode":1}]
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        Log.i("jsonObject", "==============" + jsonObject);
                        String msg = jsonObject.getString("message");
                        String responsecode = jsonObject.getString("success");
                        Log.i("responsecode", "==============" + responsecode);
                        if (responsecode.equals("1"))
                        {

                            Log.i("responsecode_+_+", "==============" + responsecode);
                               /* Snackbar snackbar = Snackbar.make(coordinator_layout,msg,Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
/*
                            LayoutInflater inflater_alert = LayoutInflater.from(getActivity());
                            View dialogLayout = inflater_alert.inflate(R.layout.success_alert, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogLayout);
                            TextView massage = (TextView) dialogLayout.findViewById(R.id.txt_masage);
                            massage.setText("update");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int arg1)
                                {
                                    Intent intent =new Intent(getActivity(),BaseNavigation.class);
                                    startActivity(intent);
                                }
                            }).show();*/


                        }
                        else
                        {
                                /*Snackbar snackbar = Snackbar.make(coordinator_layout,msg,Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*/
                        }




                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }
        AddReimbursement addReimbursement = new AddReimbursement();
        addReimbursement.execute();

    }

    public void decodeFile(String path) {
        try {
            // Decode image size
            Log.i("decodeFile123","decodeFile");

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024*10;

            // Find the correct scale value. It should be the power of 2.

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(path, o2);


        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String getImagePath()
    {
        return imgPath;
    }

//***********************for upadte image************************************














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
    public void onPause()
    {
        super.onPause();
        Log.i("onPause","onPause_fragment");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
