package com.es.ccisapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.es.model.UserProfile;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;
import com.es.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class Login_Fragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    //    private static TextView signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static RelativeLayout reLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;

    private static CheckBox cbLoginOffline;

    private static String TAG = "Login_Fragment msg ";

    public Login_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        String x = "26400.50";
        Double d = Double.parseDouble(x);
        String str = Utils.numberToString(d);
        Log.e(TAG, str);
        initViews();
        setListeners();


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent m = new Intent(getActivity(), CauHinhActivity.class);
            startActivity(m);
            return true;
        }
//        else if (id == R.id.action_report) {
//
//        } else if (id == R.id.sortTime) {
//
//        } else if (id == R.id.sortCoin) {
//
//        } else if (id == R.id.sortGain) {
//
//        } else if (id == R.id.sortDefault) {
//
//        } else if (id == R.id.sortOther) {
//
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        emailid = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        cbLoginOffline = (CheckBox) view.findViewById(R.id.cbLoginOffline);
//        signUp = (TextView) view.findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        reLayout = (RelativeLayout) view.findViewById(R.id.root_layout);

        setupUI(reLayout);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            cbLoginOffline.setTextColor(csl);
            show_hide_password.setTextColor(csl);
//            signUp.setTextColor(csl);
        } catch (Exception e) {
        }

        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        emailid.setOnFocusChangeListener(ofcListener);
        password.setOnFocusChangeListener(ofcListener);

        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
        String getEmailId = pref.getString("USERNAME", "");
        String getPassword = pref.getString("PASSWORD", "");
        emailid.setText(getEmailId);
        password.setText(getPassword);
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
//        cbLoginOffline.setOnClickListener(this);
//        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if (checkValidation()) {
                    try {
                        final String getEmailId = emailid.getText().toString();
                        final String getPassword = password.getText().toString();
                        if (!cbLoginOffline.isChecked()) {
                            CCISDataService service = RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);
                            Call<List<UserProfile>> call = service.CheckLogin(getEmailId, getPassword);

                            call.enqueue(new CustomCallBack<List<UserProfile>>(getActivity()) {
                                @Override
                                public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                                    if (response == null || response.body() == null || response.errorBody() != null) {
                                        Toast.makeText(getActivity(), "Đăng nhập thất bại. Kiểm tra thông tin !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        List<UserProfile> pUserProfile = response.body();
//                                        Boolean postCheck = response.body().booleanValue();
//
                                        if (pUserProfile != null) {
                                            Log.e("CHECK PUT", pUserProfile.get(0).toString());
                                            Intent m = new Intent(getActivity(), MainActivity.class);
                                            startActivity(m);
                                            SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("USERNAME", getEmailId);
                                            editor.putString("PASSWORD", getPassword);
                                            editor.putInt("ONLINE", 1);
                                            editor.putInt("USERID", pUserProfile.get(0).getUserId());
                                            editor.putString("EMPLOYEECODE", pUserProfile.get(0).getEmployeeCode());
                                            editor.commit();

                                        } else {
                                            loginLayout.startAnimation(shakeAnimation);
                                            Toast.makeText(getActivity(), "Sai tên đăng nhập hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                    this.mProgressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                                    Log.e("USERDEVICE", t.getMessage() + "");
                                    Toast.makeText(getActivity(), "Đăng nhập thất bại. Kiểm tra lại kết nối !", Toast.LENGTH_SHORT).show();
                                    this.mProgressDialog.dismiss();
                                }

                            });
                        } else {
                            SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
                            String strUserSave = pref.getString("USERNAME", "");
                            String strPassSave = pref.getString("PASSWORD", "");
                            if (getEmailId.equals(strUserSave) && getPassword.equals(strPassSave)) {

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("ONLINE", 0);
                                editor.commit();

                                Intent m = new Intent(getActivity(), MainActivity.class);
                                startActivity(m);
                            } else {
                                Toast.makeText(getActivity(), "Đăng nhập thất bại. Kiểm tra lại thông tin đăng nhập hoặc đã từng đăng nhập thành công online hay chưa !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {

                    }
                }

                break;

//            case R.id.cbLoginOffline:
//
//                fragmentManager
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//                        .replace(R.id.frameContainer,
//                                new ForgotPassword_Fragment(),
//                                Utils.ForgotPassword_Fragment).commit();
//                break;
//            case R.id.createAccount:
//
//                // Replace signup frgament with animation
//                fragmentManager
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//                        .replace(R.id.frameContainer, new SignUp_Fragment(),
//                                Utils.SignUp_Fragment).commit();
//                break;
        }

    }

    // Check Validation before login
    private boolean checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(getActivity().getApplicationContext(), "Cần điền đầy đủ thông tin !!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
        // Check if email id is valid or not
//        else if (!m.find())
//            Toast.makeText(getActivity().getApplicationContext(), "Email không đúng định dạng !!!", Toast.LENGTH_LONG).show();
//            // Else do login and do your stuff
//        else
//            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
//                    .show();

    }
}