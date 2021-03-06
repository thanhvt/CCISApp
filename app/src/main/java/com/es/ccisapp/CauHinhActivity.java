package com.es.ccisapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

public class CauHinhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh);

        getSupportFragmentManager().beginTransaction()
                // .replace(android.R.id.content, SettingsFragment())
                .replace(R.id.content, new SettingsFragment())
                .commit();

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String IP_SERVICE = sharedPrefs.getString("IP_SERVICE", "0");
        Log.e("CauHinhActivity", IP_SERVICE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            // Load the Preferences from the XML file
            addPreferencesFromResource(R.xml.pref_general);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

            String IP_SERVICE = sharedPrefs.getString("IP_SERVICE", "0");
            EditTextPreference etp = (EditTextPreference) findPreference("IP_SERVICE");
            etp.setSummary(IP_SERVICE);

            String TEN_CTY = sharedPrefs.getString("TEN_CTY", "");
            EditTextPreference etpTEN_CTY = (EditTextPreference) findPreference("TEN_CTY");
            etpTEN_CTY.setSummary(TEN_CTY);

            String TEN_CHINHANH = sharedPrefs.getString("TEN_CHINHANH", "");
            EditTextPreference etpTEN_CHINHANH = (EditTextPreference) findPreference("TEN_CHINHANH");
            etpTEN_CHINHANH.setSummary(TEN_CHINHANH);

            String TEN_NVTHU = sharedPrefs.getString("TEN_NVTHU", "");
            EditTextPreference etpTEN_NVTHU = (EditTextPreference) findPreference("TEN_NVTHU");
            etpTEN_NVTHU.setSummary(TEN_NVTHU);

            String SDT = sharedPrefs.getString("SDT", "");
            EditTextPreference etpSDT = (EditTextPreference) findPreference("SDT");
            etpSDT.setSummary(SDT);

            String SDT_CSKH = sharedPrefs.getString("SDT_CSKH", "");
            EditTextPreference etpSDT_CSKH = (EditTextPreference) findPreference("SDT_CSKH");
            etpSDT_CSKH.setSummary(SDT_CSKH);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals("IP_SERVICE")) {
                EditTextPreference etp = (EditTextPreference) findPreference("IP_SERVICE");
                etp.setSummary(sharedPreferences.getString("IP_SERVICE", ""));
            }
            if (key.equals("TEN_CTY")) {
                EditTextPreference etp = (EditTextPreference) findPreference("TEN_CTY");
                etp.setSummary(sharedPreferences.getString("TEN_CTY", ""));
            }
            if (key.equals("TEN_CHINHANH")) {
                EditTextPreference etp = (EditTextPreference) findPreference("TEN_CHINHANH");
                etp.setSummary(sharedPreferences.getString("TEN_CHINHANH", "Chưa xác định"));
            }
            if (key.equals("TEN_NVTHU")) {
                EditTextPreference etp = (EditTextPreference) findPreference("TEN_CHINHANH");
                etp.setSummary(sharedPreferences.getString("TEN_CHINHANH", "Chưa xác định"));
            }
            if (key.equals("SDT")) {
                EditTextPreference etp = (EditTextPreference) findPreference("SDT");
                etp.setSummary(sharedPreferences.getString("SDT", "Chưa xác định"));
            }
            if (key.equals("SDT_CSKH")) {
                EditTextPreference etp = (EditTextPreference) findPreference("SDT_CSKH");
                etp.setSummary(sharedPreferences.getString("SDT_CSKH", "Chưa xác định"));
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(getContext())
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
