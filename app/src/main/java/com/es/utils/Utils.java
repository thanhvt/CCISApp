package com.es.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.es.ccisapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    public static void startFragment(FragmentManager manager, Fragment fragment) {
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_body, fragment).commit();
    }

    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
    //Fragments Tags
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";

    public static String doubleFormatter(double number) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);

        NumberFormat formatter = new DecimalFormat("#0.00");

        String formattedNumber = formatter.format(number);

        return formattedNumber;

    }

    public static String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            String resp = formatter.format(number);
            resp = resp.replaceAll(",", ".");
            return resp;
        } catch (Exception e) {
            return "";
        }
    }
}
