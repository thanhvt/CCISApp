package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.Mobile_Adjust_Informations;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CCISDataService apiService;
    Context mContext = this;
    public static final String TAG = "MainActivity msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switchFragment(buildFragment_CCIS(), "ABC");
        apiService =
                RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(CCISDataService.class);
    }

    private CCISFragment buildFragment_CCIS() {
        CCISFragment fragment = new CCISFragment();
        return fragment;
    }

    private void switchFragment(Fragment pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_body, pos, tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ccis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_down) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Chương trình sẽ xóa dữ liệu chưa thu tiền cũ đang có trên máy điện thoại này để lấy dữ liệu mới. Anh/Chị muốn thực hiện ?");
            builder.setIcon(R.drawable.logo);
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice(0);
                    call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(mContext, "Đang lấy số liệu chưa thu tiền từ Server") {
                        @Override
                        public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {
                            List<Bill_TaxInvoice> movies = response.body();
                            if (movies.size() > 0) {
                                List<Bill_TaxInvoiceModel> tmp = new Delete().from(Bill_TaxInvoiceModel.class).execute();
                                ActiveAndroid.beginTransaction();
                                try {
                                    for (Bill_TaxInvoice b : movies) {
                                        Bill_TaxInvoiceModel c = new Bill_TaxInvoiceModel(b.getTaxCode(), b.getCustomerCode(),
                                                b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(),
                                                b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                                                b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                                                b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked());
                                        c.save();
                                    }
                                    Toast.makeText(getApplicationContext(), "Lấy số liệu chưa thu tiền từ Server thành công !", Toast.LENGTH_LONG).show();
                                    switchFragment(buildFragment_CCIS(), "ABC");
                                    ActiveAndroid.setTransactionSuccessful();
                                } finally {
                                    ActiveAndroid.endTransaction();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Không có dữ liệu chưa thu tiền !", Toast.LENGTH_LONG).show();
                            }
                            this.mProgressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<List<Bill_TaxInvoice>> call, Throwable t) {
                            // Log error here since request failed
                            Toast.makeText(getApplicationContext(), "Lỗi trong quá trình lấy dữ liệu !", Toast.LENGTH_LONG).show();
                            this.mProgressDialog.dismiss();
                        }
                    });
                }
            });
            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getApplicationContext(), "Hủy thao tác lấy dữ liệu chưa thu tiền từ Server !", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_up) {

        } else if (id == R.id.nav_duyet) {
            List<Mobile_Adjust_DB> tmp = new Select().all().from(Mobile_Adjust_DB.class).execute();
            Log.e(TAG, tmp.size() + "");
            List<Mobile_Adjust_Informations> lstInsert = new ArrayList<>();
            if (tmp.size() > 0) {
                for (Mobile_Adjust_DB mo : tmp) {
                    lstInsert.add(new Mobile_Adjust_Informations(mo.getStatus(), mo.getIndexSo(), mo.getType(), mo.getPrice(), mo.getCustomerID(), mo.getCustomerAdd(),
                            mo.getDepartmentId(), mo.getEmployeeCode(), mo.getCustomerName(), "1", mo.getAmout(), mo.getAdjustID()));
                }
                try {
                    Call<Boolean> call = apiService.Post_List(lstInsert);
                    Log.wtf("URL Called", call.request().url() + "");
                    call.enqueue(new CustomCallBack<Boolean>(mContext, "Đang đẩy thông tin ...") {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            try {
                                Log.e(TAG, response.message());
                                Boolean postCheck = response.body().booleanValue();
                                Log.e("CHECK PUT", postCheck + "");
                                if (postCheck) {
                                    Toast.makeText(getApplicationContext(), "Đẩy thông tin thay đổi thành công !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Đẩy thông tin thay đổi không thành công !", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Đẩy thông tin thay đổi không thành công !", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.getMessage());
                            } finally {
                                this.mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.e("USERDEVICE", t.getMessage() + "");
                        }

                    });
                } catch (Exception e) {

                }
            } else {

            }
        } else if (id == R.id.nav_manage) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Anh/Chị muốn xóa dữ liệu CCIS trên máy điện thoại này ?");
            builder.setIcon(R.drawable.logo);
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    List<Bill_TaxInvoiceModel> bill = new Delete().from(Bill_TaxInvoiceModel.class).execute();
                    List<Mobile_Adjust_DB> info = new Delete().from(Mobile_Adjust_DB.class).execute();
                    if (bill == null && info == null) {
                        Toast.makeText(getApplicationContext(), "Xóa dữ liệu CCIS trên máy điện thoại thành công !", Toast.LENGTH_LONG).show();
                        switchFragment(buildFragment_CCIS(), "ABC");
                    } else {
                        Toast.makeText(getApplicationContext(), "Xóa dữ liệu CCIS trên máy điện thoại thất bại. Thử lại sau !", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}