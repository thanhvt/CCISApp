package com.es.ccisapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

public class CauHinhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh);

        getSupportFragmentManager().beginTransaction()
                // .replace(android.R.id.content, SettingsFragment())
                .replace(R.id.content, new SettingsFragment())
                .commit();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        String strUserName = sharedPrefs.getString("username", "NA");
//        boolean bAppUpdates = sharedPrefs.getBoolean("applicationUpdates",false);
//        String downloadType = sharedPrefs.getString("downloadType","1");

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            // Load the Preferences from the XML file
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
