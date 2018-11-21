package com.banjarasathi;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class
Resgister_1_frag extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private static final String PREFER_NAME = "MyPref";//username paass and uid

    public  static SharedPreferences pref_regist;
    public static  SharedPreferences.Editor editor_regist;

    private OnFragmentInteractionListener mListener;
    Toolbar toolbar;
    EditText ed_firstName,ed_middleName,ed_lastName,ed_mobileNo,ed_email;
           TextView tv_birth_date,tv_email;
    Button btn_next_signup1;
    AutoCompleteTextView ac_gender;
    String first_name,middle_N,last_N,dob,mobile_No,email;
    String gender,dob_final;
    int day,month,year;
    String birth_date;



    public Resgister_1_frag()
    {
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.register_1_fragment, container, false);


        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        TextView Header = (TextView)v.findViewById(R.id.toolText);
        ImageView img_logout = (ImageView)v. findViewById(R.id.toolImg);
        //setSupportActionBar(toolbar);

        /*if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("");
            Header.setText("Sign Up");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            img_logout.setImageResource(R.drawable.back_arrow);
        }
        */

        pref_regist=getActivity().getSharedPreferences(PREFER_NAME,getActivity().MODE_PRIVATE);
        editor_regist=pref_regist.edit();

        ed_firstName = (EditText)v.findViewById(R.id.ed_firstName);
        ed_middleName = (EditText)v.findViewById(R.id.ed_middleName);
        ed_lastName = (EditText)v.findViewById(R.id.ed_lastName);
        ac_gender = (AutoCompleteTextView)v.findViewById(R.id.ac_gender);
        tv_birth_date = (TextView)v.findViewById(R.id.tv_birth_date);

        ed_mobileNo = (EditText)v.findViewById(R.id.ed_mobileNo);
        ed_email = (EditText) v.findViewById(R.id.ed_email);
        btn_next_signup1 = (Button)v.findViewById(R.id.btn_next_signup1);

        if (register_2_fragment.regist_1)
        {
            first_name= pref_regist.getString("fristname","");
            middle_N=pref_regist.getString("middlename","");
            last_N= pref_regist.getString("lastname","");
            gender=pref_regist.getString("gender","");
            birth_date= pref_regist.getString("dob","");
            mobile_No=  pref_regist.getString("mobile","");
            email=pref_regist.getString("email","");

            ed_firstName.setText(first_name);
            ed_middleName.setText(middle_N);
            ed_lastName.setText(last_N);
            ac_gender.setText(gender);
            tv_birth_date.setText(birth_date);
            ed_mobileNo.setText(mobile_No);
            ed_email.setText(email);


        }


        List<String> list_gender = new ArrayList<>();
        list_gender.add("Male");
        list_gender.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_gender);
        ac_gender.setAdapter(adapter);

        ac_gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                ac_gender.showDropDown();
                return true;
            }
        });

        btn_next_signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                first_name = ed_firstName.getText().toString();
                middle_N = ed_middleName.getText().toString();
                last_N = ed_lastName.getText().toString();
                dob = tv_birth_date.getText().toString();
                mobile_No = ed_mobileNo.getText().toString();
                email=ed_email.getText().toString();

                if (ed_firstName.getText().toString().equals("") &&
                    ed_lastName.getText().toString().equals("") &&
                    ed_middleName.getText().toString().equals("") &&
                    ac_gender.getText().toString().equals("") &&
                    tv_birth_date.getText().toString().equals("") &&
                    ed_mobileNo.getText().toString().equals("")
                    && ed_email.getText().toString().equals(""))

                {

                ed_firstName.setError("Enter First Name");
                ed_lastName.setError("Enter Last Name");
                ed_middleName.setError("Enter Last Name");
                ac_gender.setError("Select Gender");
                tv_birth_date.setError("Enter Date Of Birth");
                ed_mobileNo.setError("Enter Mobile No.");
                ed_email.setError("Enter Email ID");

                }
                else if (ed_firstName.getText().toString().equals(""))
                {
                    ed_firstName.setError("Enter First Name");
                }
                else if (ed_lastName.getText().toString().equals(""))
                {
                    ed_lastName.setError("Enter Last Name");
                }
                else if (ed_middleName.getText().toString().equals(""))
                {
                    ed_middleName.setError("Enter Last Name");
                }
                else if(ac_gender.getText().toString().equals(""))
                {
                    ac_gender.setError("Select Gender");
                }
                else if (tv_birth_date.getText().toString().equals(""))
                {
                    tv_birth_date.setError("Enter Date Of Birth");
                }
                else if (ed_email.getText().toString().equals(""))
                {
                    ed_email.setError("Enter Email ID");
                }
                else if (ed_mobileNo.getText().toString().equals(""))
                {
                    ed_mobileNo.setError("Enter Mobile No.");
                }

                else if (mobile_No.length() < 10 || mobile_No.length() > 10)
                {
                    ed_mobileNo.setError("Enter Valid Number");
                }
                else
                {
                    if (ac_gender.getText().toString().equals("Male"))
                    {
                        gender = "Male";
                    }
                    else if (ac_gender.getText().toString().equals("Female"))
                    {
                        gender = "Female";
                    }

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    try
                    {
                        Date date = sdf1.parse(dob);
                        dob_final = sdf2.format(date);
                        Log.i("signup","dateFinal=="+dob_final);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    //for mobile number validation...
                    mobileNumberValidation();
                    editTextErrors();



                    Resgister_1_frag.editor_regist.putString("fristname", first_name);
                    Resgister_1_frag.editor_regist.putString("middlename", middle_N);
                    Resgister_1_frag.editor_regist.putString("lastname", last_N);
                    Resgister_1_frag.editor_regist.putString("gender", gender);
                    Resgister_1_frag.editor_regist.putString("dob", dob_final);
                    Resgister_1_frag.editor_regist.putString("mobile", mobile_No);
                    Resgister_1_frag.editor_regist.putString("email",email);
                    editor_regist.apply();


                    loadFragment(new register_2_fragment());


                }
            }
        });

        tv_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_birth_date.setError(null);

                Calendar calendar = Calendar.getInstance();
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int m = calendar.get(Calendar.MONTH);
                final int y = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        day = i2;
                        month = i1+1;
                        year = i;
                        Log.i("date","day=2="+day+" month=i1="+month+" year=i="+year);

                        if (day < 10 && month < 10)
                        {
                            birth_date = "0" + day + "-" + "0" + month + "-" +year;
                            tv_birth_date.setText(birth_date);
                        }
                        else if (day < 10)
                        {
                            birth_date = "0" + day + "-" + month + "-" +year;
                            tv_birth_date.setText(birth_date);
                        }
                        else if (month < 10)
                        {
                            birth_date = "" + day + "-" + "0" + month + "-" +year;
                            tv_birth_date.setText(birth_date);
                        }
                        else
                        {
                            birth_date = day + "-" + month + "-" +year;
                            tv_birth_date.setText(birth_date);
                        }

                    }
                },y,m,d);

                pickerDialog.show();

            }
        });





        return v;
    }


    public void editTextErrors()
    {
        ed_firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_firstName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_lastName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_middleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_middleName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ac_gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ac_gender.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //for mobile number validation...
    public void mobileNumberValidation()
    {
        ed_mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String no = ed_mobileNo.getText().toString();
                Log.i("mobile","length=="+no.length());
                if (no.length() > 10)
                {
                    ed_mobileNo.setError("Number Should be less than 10 digit");
                }
                else
                {
                    ed_mobileNo.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
