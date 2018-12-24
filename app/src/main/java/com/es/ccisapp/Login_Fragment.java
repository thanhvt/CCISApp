package com.es.ccisapp;


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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;
import com.es.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class Login_Fragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;

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
            Toast.makeText(getActivity().getApplicationContext(), "Cấu hình địa chỉ IP service", Toast.LENGTH_LONG).show();
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
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        signUp = (TextView) view.findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }

        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
        String getEmailId = pref.getString("USERNAME", "");
        String getPassword = pref.getString("PASSWORD", "");
        emailid.setText(getEmailId);
        password.setText(getPassword);
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if (checkValidation()) {
                    try {
                        final String getEmailId = emailid.getText().toString();
                        final String getPassword = password.getText().toString();
                        CCISDataService service = RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);
                        Call<Boolean> call = service.CheckLogin(getEmailId, getPassword);
                        Log.wtf("URL Called", call.request().url() + "");
                        call.enqueue(new CustomCallBack<Boolean>(getActivity()) {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                Boolean postCheck = response.body().booleanValue();
                                Log.e("CHECK PUT", postCheck + "");
                                if (postCheck) {
                                    Intent m = new Intent(getActivity(), CCISActivity.class);
                                    startActivity(m);
                                    SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("USERNAME", getEmailId);
                                    editor.putString("PASSWORD", getPassword);
                                    editor.commit();

                                } else {
                                    loginLayout.startAnimation(shakeAnimation);
                                    Toast.makeText(getActivity(), "Sai tên đăng nhập hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                                }

                                this.mProgressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("USERDEVICE", t.getMessage() + "");
                                this.mProgressDialog.dismiss();
                            }

                        });
                    } catch (Exception e) {

                    }
                }

                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
                break;
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