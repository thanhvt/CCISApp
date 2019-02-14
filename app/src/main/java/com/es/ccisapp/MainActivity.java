package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CCISDataService apiService;
    Context mContext = this;
    public static final String TAG = "MainActivity msg";
    MenuItem nav_itemUp;
    MenuItem nav_itemDuyet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences pref = getSharedPreferences("LOGIN", 0);
        int onl = pref.getInt("ONLINE", 0);
        getSupportActionBar().setTitle(Html.fromHtml("<small>CCIS " + (onl == 0 ? "offline" : "online") + "</small>"));
        toolbar.setTitleTextColor(Color.parseColor(onl != 0 ? "#FFF900" : "#A9A9A9"));

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
        Menu menuNav = navigationView.getMenu();
        nav_itemUp = menuNav.findItem(R.id.nav_up);
        List<Bill_TaxInvoiceModel> tmp = new Select().all().from(Bill_TaxInvoiceModel.class).execute();
        if (tmp.size() == 0) {
            nav_itemUp.setEnabled(false);
        }
        nav_itemDuyet = menuNav.findItem(R.id.nav_duyet);
        List<Mobile_Adjust_DB> adjThayDoi = new Select().all().from(Mobile_Adjust_DB.class).execute();
        if (adjThayDoi.size() == 0) {
            nav_itemDuyet.setEnabled(false);
        }

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
            SharedPreferences pref = getSharedPreferences("LOGIN", 0);
            final int strUserID = pref.getInt("USERID", -1);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.logo);
            List<Bill_TaxInvoiceModel> info = new Select().all().from(Bill_TaxInvoiceModel.class).where("IsThuOffline = ?", true).execute();
            if (info.size() > 0) {
                builder.setMessage("Đã thu tiền " + info.size() + " khách hàng. Đề nghị đẩy dữ liệu lên Server trước khi lấy dữ liệu mới để đảm bảo an toàn thông tin !");
                builder.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            } else {
                builder.setMessage("Chương trình sẽ xóa dữ liệu chưa thu tiền cũ đang có trên máy điện thoại này để lấy dữ liệu mới. Anh/Chị muốn thực hiện ?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice(0, strUserID);
                        Log.wtf("URL Called", call.request().url() + "");
                        call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(mContext, "Đang lấy số liệu chưa thu tiền từ Server") {
                            @Override
                            public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {

                                if (response != null && response.body() != null) {
                                    List<Bill_TaxInvoice> movies = response.body();
                                    if (movies.size() > 0) {
                                        List<Bill_TaxInvoiceModel> tmp = new Delete().from(Bill_TaxInvoiceModel.class).execute();
                                        ActiveAndroid.beginTransaction();
                                        try {
                                            int stt = 0;
                                            for (Bill_TaxInvoice b : movies) {
                                                Bill_TaxInvoiceModel c = new Bill_TaxInvoiceModel(b.getTaxCode(), b.getCustomerCode(),
                                                        b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(),
                                                        b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                                                        b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                                                        b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked(), false, b.getAmount(), b.getServiceTypeId(), b.getServiceName());
                                                stt++;
                                                b.setSTT(stt);
                                                c.save();
                                            }
                                            Toasty.success(getApplicationContext(), "Lấy số liệu chưa thu tiền từ Server thành công !", Toasty.LENGTH_LONG, true).show();
                                            nav_itemUp.setEnabled(true);
                                            switchFragment(buildFragment_CCIS(), "ABC");
                                            ActiveAndroid.setTransactionSuccessful();
                                        } finally {
                                            ActiveAndroid.endTransaction();
                                        }
                                    } else {
                                        Toasty.warning(getApplicationContext(), "Không có dữ liệu chưa thu tiền !", Toasty.LENGTH_LONG, true).show();
                                    }
                                } else {
                                    Log.e(TAG, response.message());
                                    Toasty.error(getApplicationContext(), "Không có dữ liệu hoặc gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                                }
                                this.mProgressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<List<Bill_TaxInvoice>> call, Throwable t) {
                                // Log error here since request failed
                                Log.e(TAG, t.getMessage());
                                if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
                                    Toasty.error(getApplicationContext(), "Không có dữ liệu chưa thu tiền. Đề nghị kiểm tra lại !", Toasty.LENGTH_LONG, true).show();
                                } else {
                                    Toasty.error(getApplicationContext(), "Gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                                }
                                this.mProgressDialog.dismiss();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toasty.info(getApplicationContext(), "Hủy thao tác lấy dữ liệu chưa thu tiền từ Server !", Toasty.LENGTH_LONG, true).show();
                        dialog.dismiss();
                    }
                });
            }
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_up) {
            List<Bill_TaxInvoiceModel> tmp = new Select().all().from(Bill_TaxInvoiceModel.class).execute();
            if (tmp.size() == 0) {
                Toasty.error(getApplicationContext(), "Chưa có thu tiền của KH. Chưa thể đẩy !!", Toasty.LENGTH_LONG, true).show();
            } else {
                for (final Bill_TaxInvoiceModel b : tmp) {
                    if (b.isThuOffline()) {
                        Log.e(TAG, b.toString());
                        Call<Integer> call = apiService.ThuTien((b.getTaxInvoiceId()));
                        call.enqueue(new CustomCallBack<Integer>(mContext) {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                this.mProgressDialog.dismiss();
                                Integer movies = response.body();
                                Log.d(TAG, "movies: " + movies);
                                if (movies == 1) {
                                    List<Bill_TaxInvoiceModel> info = new Delete().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
                                    Toasty.success(getApplicationContext(), "Thu tiền khách hàng " + b.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
                                } else {
                                    Toasty.error(getApplicationContext(), "Thu tiền khách hàng " + b.getCustomerName() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toasty.LENGTH_LONG, true).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                this.mProgressDialog.dismiss();
                                // Log error here since request failed
                                Log.e(TAG, t.toString());
                            }
                        });
                    }
                }
            }
        } else if (id == R.id.nav_duyet) {
            List<Mobile_Adjust_DB> tmp = new Select().all().from(Mobile_Adjust_DB.class).execute();

            List<Mobile_Adjust_Informations> lstInsert = new ArrayList<>();
            if (tmp.size() > 0) {
                for (Mobile_Adjust_DB mo : tmp) {
                    final Mobile_Adjust_Informations mobile = new Mobile_Adjust_Informations(mo.getStatus(), mo.getIndexSo(), mo.getType(), mo.getPrice(), mo.getCustomerID(), mo.getCustomerAdd(),
                            mo.getDepartmentId(), mo.getEmployeeCode(), mo.getCustomerName(), "1", mo.getAmout(), mo.getAdjustID());
                    lstInsert.add(mobile);
                    try {
                        Call<Boolean> call = apiService.Post(mobile);
                        Log.e(TAG, "lstInsert " + lstInsert.size() + "");
                        call.enqueue(new CustomCallBack<Boolean>(mContext, "Đang đẩy thông tin ...") {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                try {
                                    Log.e(TAG, response.message());
                                    Boolean postCheck = response.body();
                                    Log.e("CHECK PUT", postCheck + "");
                                    if (postCheck) {
                                        Toasty.success(getApplicationContext(), "Đẩy thông tin KH " + mobile.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
                                        List<Mobile_Adjust_DB> info = new Delete().from(Mobile_Adjust_DB.class).where("AdjustID = ?", mobile.getAdjustID()).execute();
                                    } else {
                                        Toasty.error(getApplicationContext(), "Đẩy thông tin KH " + mobile.getCustomerName() + " không thành công !", Toasty.LENGTH_LONG, true).show();
                                    }
                                } catch (Exception e) {
                                    Toasty.error(getApplicationContext(), "Đẩy thông tin KH " + mobile.getCustomerName() + " lỗi !", Toasty.LENGTH_LONG, true).show();

                                    Log.e(TAG, e.getMessage());
                                } finally {
                                    this.mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("USERDEVICE", t.getMessage() + "");
                                Toasty.error(getApplicationContext(), "Đẩy thông tin thất bại. Kiểm tra lại kết nối !", Toasty.LENGTH_LONG, true).show();

                            }

                        });
                    } catch (Exception e) {

                    }
                }
            } else {
                Toasty.warning(getApplicationContext(), "Không có thông tin cần duyệt đẩy Server !", Toasty.LENGTH_LONG, true).show();
            }
        } else if (id == R.id.nav_manage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.logo);
            List<Bill_TaxInvoiceModel> info = new Select().all().from(Bill_TaxInvoiceModel.class).where("IsThuOffline = ?", true).execute();
            if (info.size() > 0) {
                builder.setMessage("Đã thu tiền " + info.size() + " khách hàng. Đề nghị đẩy dữ liệu lên Server trước khi lấy dữ liệu mới để đảm bảo an toàn thông tin !");
                builder.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            } else {
                builder.setMessage("Anh/Chị muốn xóa dữ liệu CCIS trên máy điện thoại này ?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        List<Bill_TaxInvoiceModel> bill = new Delete().from(Bill_TaxInvoiceModel.class).execute();
                        List<Mobile_Adjust_DB> info = new Delete().from(Mobile_Adjust_DB.class).execute();
                        if (bill == null && info == null) {
                            Toasty.success(getApplicationContext(), "Xóa dữ liệu CCIS trên máy điện thoại thành công !", Toasty.LENGTH_LONG, true).show();
                            switchFragment(buildFragment_CCIS(), "ABC");
                        } else {
                            Toasty.error(getApplicationContext(), "Xóa dữ liệu CCIS trên máy điện thoại thất bại. Thử lại sau !", Toasty.LENGTH_LONG, true).show();
                        }
                    }
                });
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }

            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.nav_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Anh/Chị muốn đăng xuất khỏi tài khoản này ?");
            builder.setIcon(R.drawable.logo);
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                    SharedPreferences pref = getSharedPreferences("LOGIN", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("PASSWORD", "");
                    editor.putInt("ONLINE", 0);
                    editor.commit();

                    Intent m = new Intent(MainActivity.this, RootActivity.class);
                    startActivity(m);
                }
            });
            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Mobile_Adjust_DB> adjThayDoi = new Select().all().from(Mobile_Adjust_DB.class).execute();
        if (adjThayDoi.size() == 0) {
            nav_itemDuyet.setEnabled(false);
        } else {
            nav_itemDuyet.setEnabled(true);
        }
    }
}
