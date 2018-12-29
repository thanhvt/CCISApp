package com.es.ccisapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.es.utils.Utils;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RootActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    private TelephonyManager mTelephonyManager;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    String strDevice = "";
    @BindView(R.id.txtIMEI)
    TextView txtIMEI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        ActiveAndroid.initialize(getApplication());

        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }

        requestAppPermissions();
    }

    // Replace Login Fragment with animation
    protected void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.Login_Fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent m = new Intent(ScrollingActivity.this, SettingsPrefActivity.class);
//            m.putExtra("KEY", regId);
//            startActivity(m);
//            return true;
//        }
//        if (id == R.id.action_key) {
//            Intent m = new Intent(ScrollingActivity.this, TradeApiActivity.class);
//            startActivity(m);
//            return true;
//        }
//        if (id == R.id.action_about) {
//            Intent m = new Intent(ScrollingActivity.this, AboutActivity.class);
//            startActivity(m);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);
        Fragment ForgotPassword_Fragment = fragmentManager
                .findFragmentByTag(Utils.ForgotPassword_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else if (ForgotPassword_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        boolean a = hasReadPermissions();
        boolean b = hasWritePermissions();
        boolean c = hasIMEIPermissions();
        if (!a || !b || !c) {
            Toast.makeText(getApplicationContext(), "Ứng dụng chưa được cấp quyền đọc ghi dữ liệu. Kiểm tra lại !", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_PHONE_STATE,
                    }, 1);
        }
        if (c) {
            getDeviceImei();
        }
//        if (!hasReadPermissions()) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        }, 1);
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        }, 2);
//            }
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{
//                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                    }, 3);
//        }
//        if (!hasWritePermissions()) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        }, 4);
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        }, 5);
//            }
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    }, 6);
//        }
//        if (!hasIMEIPermissions()) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_PHONE_STATE)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                android.Manifest.permission.READ_PHONE_STATE,
//                        }, 7);
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                android.Manifest.permission.READ_PHONE_STATE,
//                        }, 8);
//            }
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{
//                            android.Manifest.permission.READ_PHONE_STATE,
//                    }, 9);
//        }
//
//        if (hasReadPermissions() && hasWritePermissions() && hasIMEIPermissions()) {
//            Toast.makeText(getApplicationContext(), "Ứng dụng chưa được cấp quyền đọc ghi dữ liệu. Kiểm tra lại !", Toast.LENGTH_LONG).show();
//            return;
//        }
    }

    private boolean hasIMEIPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("MissingPermission")
    private void getDeviceImei() {
        try {

            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                strDevice = mTelephonyManager.getDeviceId();
            }

            String deviceId = Settings.System.getString(getContentResolver(),
                    Settings.System.ANDROID_ID);
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            String serialNumber = (String) get.invoke(c, "sys.serialnumber", "Error");
            if (serialNumber.equals("Error")) {
                serialNumber = (String) get.invoke(c, "ril.serialnumber", "Error");
            }
            Log.e("msg", "DeviceImei " + strDevice + " " + deviceId + " " + serialNumber + " " + android.os.Build.SERIAL);
            txtIMEI.setText("IMEI: " + strDevice);
        } catch (Exception e) {
            Log.e("msg", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0) {
            if (requestCode == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceImei();
            }
        }
    }
}
