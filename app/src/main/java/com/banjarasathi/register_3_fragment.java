package com.banjarasathi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by saiyog on 3/15/2018.
 */

public class register_3_fragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String PREFER_NAME = "MyPref";//username paass and uid
    private Resgister_1_frag.OnFragmentInteractionListener mListener;
    Toolbar toolbar;
    AutoCompleteTextView ac_state,ac_city;
    EditText ed_address,ed_password,ed_reenter_pass;
    Button btn_submit;
    String mobile_id="",Androidversion, android_id="",simSerialNo="",Androidversion_att;
    String state,city,addres,password,retype_pass;
    String userChoosenTask = "";
    int READ_PERMISSION = 1;
    private static final int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;
    private String imgPath;
    Button btn_browse_img;
    String selectedImagePath = "";
    //TextView ED_IMAGE_PATH;
    String image_encoded;
    private Bitmap bitmap;
    boolean flag_timeout = true;
    int count = 0;
    ProgressDialog progressDialog;
    String response;
    String response_register,myJson_regi;
    String fristname,middlename,lastname,gender,dob,mobile,email;
    String edu,prof,caste,income,sub_Caste,gotra;



    public  static SharedPreferences pref_regist;
    ImageView img_profile;
    public register_3_fragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.register_3_fragment, container, false);

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

        ac_state=(AutoCompleteTextView)v.findViewById(R.id.ac_state);
        ac_city=(AutoCompleteTextView)v.findViewById(R.id.ac_city);
        ed_address=(AutoCompleteTextView)v.findViewById(R.id.ed_address);
        ed_password = (EditText)v.findViewById(R.id.ed_password123);
        ed_reenter_pass = (EditText)v.findViewById(R.id.ed_reenter_pass123);
        //btn_browse_img = (Button)v.findViewById(R.id.btn_browse_img);

        btn_submit = (Button)v.findViewById(R.id.btn_submit);



        pref_regist=getActivity().getSharedPreferences(PREFER_NAME,getActivity().MODE_PRIVATE);

        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                //encodeImageBase64();
                password=ed_password.getText().toString();
                retype_pass=ed_reenter_pass.getText().toString();

                if (ac_state.getText().toString().equals("") &&
                        ac_city.getText().toString().equals("") &&
                        ed_address.getText().toString().equals("") &&
                        ed_password.getText().toString().equals("") &&
                        ed_reenter_pass.getText().toString().equals(""))
                {
                    ac_state.setError("Select state");
                    ac_city.setError("Select city");
                    ed_address.setError("Enter Address");
                    ed_password.setError("Enter Password");
                    ed_reenter_pass.setError("Retype Password");
                } else if (ac_state.getText().toString().equals("")) {
                    ac_state.setError("Select state");
                } else if (ac_city.getText().toString().equals("")) {
                    ac_city.setError("Select city");
                } else if (ed_address.getText().toString().equals("")) {
                    ed_address.setError("Enter Address");
                }
                else if (ed_password.getText().toString().equals("")) {
                    ed_password.setError("Enter Password");
                }
                else if (ed_reenter_pass.getText().toString().equals("")) {
                    ed_reenter_pass.setError("Retype Password");
                }
                else if (!password.equals(retype_pass))
                {
                    ed_reenter_pass.setError("Enter Correct Password");
                }
                else
                {





                    //personal
                    fristname= pref_regist.getString("fristname","");
                    middlename= pref_regist.getString("middlename","");
                    lastname= pref_regist.getString("lastname","");
                    gender= pref_regist.getString("gender","");
                    dob= pref_regist.getString("dob","");
                    mobile= pref_regist.getString("mobile","");
                    email= pref_regist.getString("email","");


                    //education
                    edu= pref_regist.getString("edu","");
                    prof= pref_regist.getString("prof","");
                    income= pref_regist.getString("income","");
                    caste= pref_regist.getString("caste","");
                    sub_Caste= pref_regist.getString("sub_Caste","");
                    gotra= pref_regist.getString("gotra","");


                    //address
                    state = ac_state.getText().toString();
                    city = ac_city.getText().toString();
                    addres = ed_address.getText().toString();
                    password = ed_password.getText().toString();
                    retype_pass = ed_reenter_pass.getText().toString();

                    resgisterUser();

                   //submitUserData2();
                }


            }
        });
/*
        btn_browse_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //selectImageFromGallery();
            }
        });*/



        return v;
    }



    public void resgisterUser()
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
                    String version_url = "http://www.banjarasathi.com/Api/registration.php?";


                    //http://www.banjarasathi.com/Api/registration.php?
                    // fname=yogesh&mname=Yogesh&lname=yogesh&gender=Male&
                    // dob=2018-02-02&email=abc77.com&mobNo=12345555&education=mca
                    // &profesion=devloper&income=100000&cast=banjara&subcast=banjara
                    // &goatra=abc&state=7&city=5&address=abc&photo=xxxbd4d&password=12345&height=5.2

                    String url1 = version_url + "fname=" + URLEncoder.encode(fristname, "UTF-8")
                            + "&mname=" + URLEncoder.encode(middlename, "UTF-8")
                            + "&lname=" + URLEncoder.encode(lastname, "UTF-8")
                            + "&gender=" + URLEncoder.encode(gender, "UTF-8")
                            + "&dob=" + URLEncoder.encode(dob, "UTF-8")
                            + "&email=" + URLEncoder.encode(email, "UTF-8")
                            + "&mobNo=" + URLEncoder.encode(mobile, "UTF-8")
                            + "&education=" + URLEncoder.encode(edu, "UTF-8")
                            + "&profesion=" + URLEncoder.encode(prof, "UTF-8")
                            + "&income=" + URLEncoder.encode(income, "UTF-8")
                            + "&cast=" + URLEncoder.encode(caste, "UTF-8")
                            + "&subcast=" + URLEncoder.encode(sub_Caste, "UTF-8")
                            + "&goatra=" + URLEncoder.encode(gotra, "UTF-8")
                            + "&state=" + URLEncoder.encode(state, "UTF-8")
                            + "&city=" + URLEncoder.encode(city, "UTF-8")
                            + "&address=" + URLEncoder.encode(addres, "UTF-8")
                            + "&password=" + URLEncoder.encode(password, "UTF-8")
                            + "&height=" + URLEncoder.encode("5", "UTF-8");


                    URL url_ver = new URL(url1);
                    Log.i("url_register",""+url_ver);

                    HttpURLConnection connection = (HttpURLConnection)url_ver.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(10000);
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
                            response_register = "";
                            response_register += line;
                            //Log.i("response_image", response_version);

                        }
                    }
                    else
                    {
                        response_register = "";
                    }
                }
                catch (Exception e){
                    //Log.e("Exception", e.toString());
                }

                return response_register;
            }

            @Override
            protected void onPostExecute(String result)
            {
                progressDialog.dismiss();
                if (result != null)
                {
                    myJson_regi = result;
                    Log.i("myJson_ver", myJson_regi);



                    //http://www.banjarasathi.com/Api/registration.php?
                    // fname=yogesh&mname=Yogesh&lname=yogesh&gender=Male&
                    // dob=2018-02-02&email=abc77.com&mobNo=12345555&education=mca
                    // &profesion=devloper&income=100000&cast=banjara&subcast=banjara
                    // &goatra=abc&state=7&city=5&address=abc&photo=xxxbd4d&password=12345&height=5.2

                    //{"success":0,"Mobileno":"12345555","message":"Mobile already exist..."}
                    try
                    {
                        JSONObject jsonObject1 = new JSONObject(myJson_regi);
                        //Log.i("jsonArray", "" + jsonArray);


                        String responceCode = jsonObject1.getString("success");
                        Log.i("responceCode",responceCode);
                        String responceMassage=jsonObject1.getString("message");

                        if (responceCode.equals("1"))
                        {
                            Log.i("responceCode123",responceCode);
                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getActivity());
                            alerBuilder.setTitle("Registration");
                            alerBuilder.setMessage(responceMassage);
                            alerBuilder.setCancelable(false);
                            alerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    loadFragment(new Login_fragment());
                                }
                            });
                            alerBuilder.show();
                        }
                        else if(responceCode.equals("0"))
                        {
                            Log.i("responceCode123",responceCode);
                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getActivity());
                            alerBuilder.setTitle("Registration");
                            alerBuilder.setMessage(responceMassage);
                            alerBuilder.setCancelable(false);
                            alerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alerBuilder.show();
                        }
                        else
                        {
                            Log.i("responceCode123",responceCode);
                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getActivity());
                            alerBuilder.setTitle("Registration");
                            alerBuilder.setMessage(responceMassage);
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
                        Log.e("JsonException", e.toString());
                    }
                }

            }
        }

        GeversionData getUrlData = new GeversionData();
        getUrlData.execute();
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
    /*public void submitUserData2()
    {
        flag_timeout = true;

        class SubmitUserData extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
                progressDialog.setTitle("Registration in process...");
                progressDialog.setMessage("Please Wait....");
                // progressDialog = ProgressDialog.show(Sign_up_3.this,"Please wait","Adding your claims...",true);

                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params)
            {

                response = "";
                String submit_url = http + domain_url + "/appapi.php?";
                Log.i("submit","userdata=="+submit_url);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("first_name",firstname));
                nameValuePairs.add(new BasicNameValuePair("last_name",lastname));
                nameValuePairs.add(new BasicNameValuePair("middle_name",middlename));
                nameValuePairs.add(new BasicNameValuePair("gender",gender));
                nameValuePairs.add(new BasicNameValuePair("dob",dob));
                nameValuePairs.add(new BasicNameValuePair("mobileNo",mobile));
                nameValuePairs.add(new BasicNameValuePair("caste",caste));
                nameValuePairs.add(new BasicNameValuePair("sub_caste",sub_caste));
                nameValuePairs.add(new BasicNameValuePair("state",state));
                nameValuePairs.add(new BasicNameValuePair("city",city));
                nameValuePairs.add(new BasicNameValuePair("address",address));
                nameValuePairs.add(new BasicNameValuePair("gotra",gotra));
                nameValuePairs.add(new BasicNameValuePair("photo",image_encoded));
                nameValuePairs.add(new BasicNameValuePair("password",password));
                nameValuePairs.add(new BasicNameValuePair("pageFlag","1"));

                Log.i("submit","nameValuePairs=="+nameValuePairs);
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(submit_url);
                    Log.i("submit", "httpPost==" + httpPost);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    response = EntityUtils.toString(httpResponse.getEntity());
                    Log.i("submit", "response==" +response);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch (SocketTimeoutException e) {

                    flag_timeout = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(coordinator_layout, "Server Connection Timeout", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                    });
                    e.printStackTrace();
                } catch (ConnectTimeoutException e) {

                    flag_timeout = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(coordinator_layout, "Server Connection Timeout", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                            snackbar.show();
                        }
                    });
                    e.printStackTrace();
                }
                catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

                if (flag_timeout)
                {
                    Log.i("response", "response=s==" + s);
                    if (s == null || s.equals("[]") || s.equals("null"))
                    {
                        Toast.makeText(Sign_up_3.this, "Bad Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        // [{"msg":"Email Already Exist","responsecode":0,"responsedata":"NA"}]
                        //[{"msg":"Mobile Already Exist","responsecode":0,"responsedata":"NA"}]
                        // [{"msg":"Success","responsecode":1,"responsedata":12}]

                        try
                        {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject object = jsonArray.getJSONObject(0);
                            String msg = object.getString("msg");
                            String responsecode = object.getString("responsecode");
                            if (responsecode.equals("1"))
                            {
                               *//* Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg, Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();*//*
                                Snackbar snackbar = Snackbar.make(coordinator_layout,"You are successfully registered on Teli Bandhan.\nNow LogIn using your password.",Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();

                                editor.clear();
                                editor.commit();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Sign_up_3.this,SignIn.class);
                                        overridePendingTransition(R.anim.enter,R.anim.exit);
                                        startActivity(intent);
                                        finish();
                                    }
                                },1000);

                            }
                            else
                            {
                                Snackbar snackbar = Snackbar.make(coordinator_layout,""+msg, Snackbar.LENGTH_INDEFINITE);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                                snackbar.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        SubmitUserData submitUserData = new SubmitUserData();
        submitUserData.execute();

    }
*/    //select poto...
    /*public void selectImageFromGallery() {

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
                    else {
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
                        intent.setType("image*//*");
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
                    intent.setType("image*//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
                }
            }
        }
    }

    public Uri setImageUri()
    {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        Log.i("img","path="+imgPath+" uri=="+imgUri);
        return imgUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
                        Toast.makeText(getActivity(),"Please Select Profile Photo",Toast.LENGTH_LONG).show();
                       *//* Snackbar snackbar = Snackbar.make(coordinator_layout,"Please Select Profile Photo",Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                        snackbar.show();*//*
                    }
                    else
                    {
                        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                        img_profile.setImageBitmap(bm);
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
                        Toast.makeText(getActivity(),"Please Select Profile Photo",Toast.LENGTH_LONG).show();
                        *//*Snackbar snackbar = Snackbar.make(coordinator_layout,"",Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.PinkBgColor));
                        snackbar.show();*//*
                    }
                    else
                    {
                        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                        img_profile.setImageBitmap(bm);
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
                    Toast.makeText(getActivity(),"Please Select Profile Photo",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);

                    img_profile.setImageBitmap(bm);
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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

                img_profile.setImageBitmap(bm);

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
            Toast.makeText(getActivity(),"Please Select Profile Photo",Toast.LENGTH_LONG).show();

        }
    }

    public String getImagePath()
    {
        return imgPath;
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

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        //Cursor cursor ;
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
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



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
           // Intent intent=new Intent(register_3_fragment.this,);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}

