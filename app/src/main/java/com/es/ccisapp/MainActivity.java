package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceDetail;
import com.es.model.Bill_TaxInvoiceDetail_DB;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.model.Concus_Customer;
import com.es.model.Concus_Customer_DB;
import com.es.model.DonGia;
import com.es.model.DonGia_DB;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.Mobile_Adjust_Informations;
import com.es.model.SoGCS_User;
import com.es.model.SoGCS_User_DB;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;
import com.es.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
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

    private NewCustomerFragment buildFragment_NewCustomer() {
        NewCustomerFragment fragment = new NewCustomerFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("TAX", taxInvoice);
//        fragment.setArguments(bundle);
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
        final int id = item.getItemId();
        SharedPreferences pref = getSharedPreferences("LOGIN", 0);
        final int strUserID = pref.getInt("USERID", -1);
        if (id == R.id.nav_home) {
            switchFragment(buildFragment_CCIS(), "ABC");
        } else if (id == R.id.nav_down) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.logo);
            List<Bill_TaxInvoiceModel> info = new Select().all().from(Bill_TaxInvoiceModel.class).where("IsThuOffline = ?", true).execute();
            List<Mobile_Adjust_DB> adjThayDoi = new Select().all().from(Mobile_Adjust_DB.class).where("IS_SUBMIT IS NULL").execute();
            if (info.size() > 0) {
                builder.setMessage("Đã thu tiền " + info.size() + " khách hàng. Đề nghị đẩy dữ liệu lên Server trước khi lấy dữ liệu mới để đảm bảo an toàn thông tin !");
                builder.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            } else if (adjThayDoi.size() > 0) {
                builder.setMessage("Đã thay đổi thông tin " + adjThayDoi.size() + " khách hàng. Đề nghị Duyệt dữ liệu lên Server trước khi lấy dữ liệu mới để đảm bảo an toàn thông tin !");
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

                        call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(mContext, "Đang lấy số liệu chưa thu tiền từ Server") {
                            @Override
                            public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {

                                if (response != null && response.body() != null) {
                                    List<Bill_TaxInvoice> movies = response.body();
                                    if (movies.size() > 0) {
                                        List<Bill_TaxInvoiceModel> tmp = new Delete().from(Bill_TaxInvoiceModel.class).execute();
                                        new Delete().from(Bill_TaxInvoiceDetail_DB.class).execute();
                                        ActiveAndroid.beginTransaction();
                                        try {
                                            int stt = 0;
                                            String strIDs = "";
                                            for (Bill_TaxInvoice b : movies) {
                                                Bill_TaxInvoiceModel c = new Bill_TaxInvoiceModel(b.getTaxCode(), b.getCustomerCode(),
                                                        b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(),
                                                        b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                                                        b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                                                        b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked(), 0, b.getAmount(), b.getServiceTypeId(), b.getServiceName(),
                                                        b.getINDEX_THU(), b.getKIEU(), b.getPriceId());
                                                stt++;
                                                b.setSTT(stt);
                                                c.save();
                                                strIDs += b.getTaxInvoiceId() + ",";
                                            }
                                            getDetail(movies);
                                            getDetailConcus_Customer(movies);
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
                SharedPreferences preff = getSharedPreferences("LOGIN", 0);
                final int strUSERID = preff.getInt("USERID", -1);
                for (final Bill_TaxInvoiceModel b : tmp) {
                    if (b.isThuOffline() == 1) {
                        Log.e(TAG, b.toString());
                        Call<Integer> call = apiService.ThuTien(b.getTaxInvoiceId(), strUSERID);
                        call.enqueue(new CustomCallBack<Integer>(mContext) {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                this.mProgressDialog.dismiss();
                                Integer movies = response.body();
                                Log.d(TAG, "movies: " + movies);
                                if (movies == 1) {
                                    List<Bill_TaxInvoiceModel> info = new Delete().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
                                    Toasty.success(getApplicationContext(), "Thu tiền khách hàng " + b.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
                                    new Delete().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
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
                    } else if (b.isThuOffline() == 2) {
                        List<Bill_TaxInvoiceModel> info = new Delete().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
                        new Delete().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
                    }
                }
            }
        } else if (id == R.id.nav_duyet) {
            List<Mobile_Adjust_DB> tmp = new Select().all().from(Mobile_Adjust_DB.class).where("IS_SUBMIT IS NULL").execute();

            List<Mobile_Adjust_Informations> lstInsert = new ArrayList<>();
            if (tmp.size() > 0) {
                for (final Mobile_Adjust_DB mo : tmp) {
                    final Mobile_Adjust_Informations mobile = new Mobile_Adjust_Informations(mo.getStatus(), mo.getIndexSo(), mo.getType(), mo.getPrice(), mo.getCustomerID(), mo.getCustomerAdd(),
                            mo.getDepartmentId(), mo.getEmployeeCode(), mo.getCustomerName(),
                            "1", mo.getAmout(), mo.getAdjustID(), mo.getFigureBookId(), Utils.parseDate(mo.getStartDate()), Utils.parseDate(mo.getEndDate()),
                            mo.getSubTotal(), mo.getTax(), mo.getTotal(), "-1", mo.getPriceId(), mo.getTaxCode(), mo.getPhoneNumber(), mo.getEmail(), mo.getGiaSauThue(), mo.getPaymentMethodsCode());
                    if (mobile.getType().equals("3")) {
                        Calendar c = Calendar.getInstance();
                        mobile.setMonth(c.get(Calendar.MONTH) + 1);
                        mobile.setYear(c.get(Calendar.YEAR));
                    } else {
                        List<Bill_TaxInvoiceModel> info = new Select().from(Bill_TaxInvoiceModel.class).where("CustomerId = ?", mo.getCustomerID()).orderBy("TaxInvoiceId DESC").execute();
                        if (info.size() > 0) {
                            mobile.setMonth(Integer.parseInt(info.get(0).getMonth()));
                            mobile.setYear(Integer.parseInt(info.get(0).getYear()));
                        } else {
                            Calendar c = Calendar.getInstance();
                            mobile.setMonth(c.get(Calendar.MONTH) + 1);
                            mobile.setYear(c.get(Calendar.YEAR));
                        }
                    }
                    //, String amout, String adjustID, String figureBookId, String startDate, String endDate, String subTotal, String tax, String total, String customerNew) {
                    lstInsert.add(mobile);
                    try {
                        Call<String> call = apiService.Post(mobile);
                        Log.e(TAG, "lstInsert " + lstInsert.size() + "");
                        call.enqueue(new CustomCallBack<String>(mContext, "Đang đẩy thông tin ...") {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                try {
                                    Log.e(TAG, response.message());
                                    String postCheck = response.body();
                                    Log.e("CHECK PUT", postCheck + "");
                                    if (postCheck.equals("OK")) {
                                        Toasty.success(getApplicationContext(), "Đẩy thông tin KH " + mobile.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
                                        mo.setIS_SUBMIT("1");
                                        mo.save();
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
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("USERDEVICE", t.getMessage() + "");
                                Toasty.error(getApplicationContext(), "Đẩy thông tin thất bại. Kiểm tra lại kết nối !", Toasty.LENGTH_LONG, true).show();

                            }

                        });
                    } catch (Exception e) {

                        Log.e(TAG, e.getMessage());
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
            List<Mobile_Adjust_DB> adjThayDoi = new Select().all().from(Mobile_Adjust_DB.class).where("IS_SUBMIT IS NULL").execute();

            if (info.size() > 0) {
                builder.setMessage("Đã thu tiền " + info.size() + " khách hàng. Đề nghị đẩy dữ liệu lên Server trước khi lấy dữ liệu mới để đảm bảo an toàn thông tin !");
                builder.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            } else if (adjThayDoi.size() > 0) {
                builder.setMessage("Đã thay đổi thông tin " + adjThayDoi.size() + " khách hàng. Đề nghị Duyệt dữ liệu lên Server trước khi lấy dữ liệu mới để đảm bảo an toàn thông tin !");
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
                        new Delete().from(Bill_TaxInvoiceDetail_DB.class).execute();
                        new Delete().from(SoGCS_User_DB.class).execute();
                        new Delete().from(DonGia_DB.class).execute();
                        new Delete().from(Concus_Customer_DB.class).execute();
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
        } else if (id == R.id.nav_dongia) {
            Call<List<DonGia>> call = apiService.getDonGia();

            call.enqueue(new CustomCallBack<List<DonGia>>(mContext, "Đang lấy danh mục đơn giá từ Server") {
                @Override
                public void onResponse(Call<List<DonGia>> call, Response<List<DonGia>> response) {

                    if (response != null && response.body() != null) {
                        List<DonGia> movies = response.body();
                        if (movies.size() > 0) {
                            Log.e(TAG, movies.size() + " -- " + movies.get(0).toString());
                            new Delete().from(DonGia_DB.class).execute();
                            ActiveAndroid.beginTransaction();
                            try {

                                // int priceId, String occupationsGroupCode, String description, String time, double priceRound, double price
                                DonGia_DB x = new DonGia_DB(-1, "", "---", "", 0, 0);
                                x.save();

                                for (DonGia b : movies) {
                                    DonGia_DB c = new DonGia_DB(b.PriceId, b.OccupationsGroupCode, b.Description, b.Time, b.PriceRound, b.Price);
                                    c.save();
                                }

                                ActiveAndroid.setTransactionSuccessful();
                                Toasty.success(getApplicationContext(), "Lấy danh mục " + movies.size() + " đơn giá thành công !", Toasty.LENGTH_LONG, true).show();
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        } else {
                            Toasty.warning(getApplicationContext(), "Không có dữ liệu đơn giá !", Toasty.LENGTH_LONG, true).show();
                        }
                    } else {
                        Log.e(TAG, response.message());
                        Toasty.error(getApplicationContext(), "Không có dữ liệu hoặc gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                    }
                    this.mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<DonGia>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.getMessage());
                    if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
                        Toasty.error(getApplicationContext(), "Không có dữ liệu đơn giá. Đề nghị kiểm tra lại !", Toasty.LENGTH_LONG, true).show();
                    } else {
                        Toasty.error(getApplicationContext(), "Gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                    }
                    this.mProgressDialog.dismiss();
                }
            });
        } else if (id == R.id.nav_so) {
            Call<List<SoGCS_User>> call = apiService.getSo_User(strUserID);

            call.enqueue(new CustomCallBack<List<SoGCS_User>>(mContext, "Đang lấy danh mục sổ từ Server") {
                @Override
                public void onResponse(Call<List<SoGCS_User>> call, Response<List<SoGCS_User>> response) {

                    if (response != null && response.body() != null) {
                        List<SoGCS_User> movies = response.body();
                        if (movies.size() > 0) {
                            Log.e(TAG, movies.size() + " sổ");
                            new Delete().from(SoGCS_User_DB.class).execute();
                            ActiveAndroid.beginTransaction();
                            try {
                                int stt = 0;
                                String strIDs = "";
                                for (SoGCS_User b : movies) {
                                    SoGCS_User_DB c = new SoGCS_User_DB(b.EmployeeId, b.EmployeeCode, b.UserId, b.BookCode, b.BookName, b.FigureBookId);
                                    c.save();
                                }

                                ActiveAndroid.setTransactionSuccessful();
                                Toasty.success(getApplicationContext(), "Lấy danh mục " + movies.size() + " sổ thành công !", Toasty.LENGTH_LONG, true).show();
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        } else {
                            Toasty.warning(getApplicationContext(), "Không có dữ liệu sổ của user !", Toasty.LENGTH_LONG, true).show();
                        }
                    } else {
                        Log.e(TAG, response.message());
                        Toasty.error(getApplicationContext(), "Không có dữ liệu hoặc gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                    }
                    this.mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<SoGCS_User>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.getMessage());
                    if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
                        Toasty.error(getApplicationContext(), "Không có dữ liệu sổ của user. Đề nghị kiểm tra lại !", Toasty.LENGTH_LONG, true).show();
                    } else {
                        Toasty.error(getApplicationContext(), "Gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                    }
                    this.mProgressDialog.dismiss();
                }
            });
        } else if (id == R.id.nav_khmoi) {
            switchFragment(buildFragment_NewCustomer(), "ABC");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getDetail(List<Bill_TaxInvoice> movies) {
        int tmpLength = movies.size() * 6;
        int soLanQua = tmpLength / 200;
        String strID = "";
        if (soLanQua == 0) {
            for (Bill_TaxInvoice b : movies) {
                strID += b.getTaxInvoiceId() + ",";
            }
            getDetailSoLan(strID);
        } else {
            for (int i = 0; i < movies.size(); i++) {
                Bill_TaxInvoice item = movies.get(i);
                strID += item.getTaxInvoiceId() + ",";
                if (strID.length() > 200) {
                    getDetailSoLan(strID);
                    strID = "";
                }
                if (i == movies.size() - 1 && strID.length() <= 200) {
                    getDetailSoLan(strID);
                }
            }
        }
    }

    public void getDetailConcus_Customer(List<Bill_TaxInvoice> movies) {
        int tmpLength = movies.size() * 6;
        int soLanQua = tmpLength / 200;
        String strID = "";
        if (soLanQua == 0) {
            for (Bill_TaxInvoice b : movies) {
                strID += b.getCustomerCode() + ",";
            }
            getListConcus_Customer(strID);
        } else {
            for (int i = 0; i < movies.size(); i++) {
                Bill_TaxInvoice item = movies.get(i);
                strID += item.getCustomerCode() + ",";
                if (strID.length() > 200) {
                    getListConcus_Customer(strID);
                    strID = "";
                }
                if (i == movies.size() - 1 && strID.length() <= 200) {
                    getListConcus_Customer(strID);
                }
            }
        }
    }

    public void getListConcus_Customer(String strID) {
        Call<List<Concus_Customer>> call2 = apiService.getListConcus_Customer(strID);

        call2.enqueue(new CustomCallBack<List<Concus_Customer>>(mContext, "Đang lấy số liệu khách hàng từ Server") {
            @Override
            public void onResponse(Call<List<Concus_Customer>> call, Response<List<Concus_Customer>> response) {

                if (response != null && response.body() != null) {
                    List<Concus_Customer> movies = response.body();
                    if (movies.size() > 0) {
                        Log.e(TAG, "getListConcus_Customer " + movies.size());
                        try {

                            ActiveAndroid.beginTransaction();
                            try {
                                for (Concus_Customer b : movies) {
                                    List<Concus_Customer_DB> info = new Select().all().from(Concus_Customer_DB.class).where("CustomerCode = ?", b.CustomerCode).execute();
                                    if (info.size() == 0) {
                                        Concus_Customer_DB c = new Concus_Customer_DB(b.CustomerId, b.CustomerCode, b.DepartmentId, b.Name, b.Address, b.InvoiceAddress, b.Fax,
                                                b.Gender,
                                                b.Email, b.PhoneNumber, b.TaxCode, b.Ratio, b.BankAccount, b.BankName, b.Status, b.CreateUser, b.OccupationsGroupCode,
                                                b.PhoneCustomerCare,
                                                b.PaymentMethodsCode);
                                        c.save();
                                    }
                                }
                                ActiveAndroid.setTransactionSuccessful();
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        Toasty.warning(getApplicationContext(), "Không có dữ liệu chi tiết khách hàng !", Toasty.LENGTH_LONG, true).show();
                    }
                } else {
                    Log.e(TAG, response.message());
                    Toasty.error(getApplicationContext(), "Không có dữ liệu hoặc gặp lỗi trong quá trình lấy dữ liệu khách hàng !", Toasty.LENGTH_LONG, true).show();
                }
                this.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Concus_Customer>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.getMessage());
                if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
                    Toasty.error(getApplicationContext(), "Không có dữ liệu khách hàng. Đề nghị kiểm tra lại !", Toasty.LENGTH_LONG, true).show();
                } else {
                    Toasty.error(getApplicationContext(), "Gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
                }
                this.mProgressDialog.dismiss();
            }
        });
    }

    public void getDetailSoLan(String strID) {
        Call<List<Bill_TaxInvoiceDetail>> call2 = apiService.getList_Bill_TaxInvoiceDetail(strID);
        Log.wtf("URL Called", call2.request().url() + "");
        call2.enqueue(new CustomCallBack<List<Bill_TaxInvoiceDetail>>(mContext, "Đang lấy số liệu chưa thu tiền từ Server") {
            @Override
            public void onResponse(Call<List<Bill_TaxInvoiceDetail>> call, Response<List<Bill_TaxInvoiceDetail>> response) {

                if (response != null && response.body() != null) {
                    List<Bill_TaxInvoiceDetail> movies = response.body();
                    if (movies.size() > 0) {
                        try {

                            ActiveAndroid.beginTransaction();
                            try {
                                for (Bill_TaxInvoiceDetail b : movies) {
                                    List<Bill_TaxInvoiceDetail_DB> info = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceDetailId = ?", b.getTaxInvoiceDetailId()).execute();
                                    if (info.size() == 0) {
                                        Bill_TaxInvoiceDetail_DB c = new Bill_TaxInvoiceDetail_DB(b.TaxInvoiceDetailId, b.DepartmentId, b.Term, b.TaxInvoiceId, b.CustomerId, b.CustomerCode, b.ServiceTypeId, b.ServiceName,
                                                b.FigureBookId, b.Month, b.Year, b.Total, b.ContractDetailId, b.CreateUser, b.Amount, b.Price, b.TypeOfUnit, b.TuNgay, b.DenNgay);
                                        c.save();
                                        Log.e(TAG, b.getTuNgay() + " - " + b.getDenNgay() + " - " + b.getTerm());
                                    }
                                }
                                ActiveAndroid.setTransactionSuccessful();
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        Toasty.warning(getApplicationContext(), "Không có dữ liệu chi tiết chưa thu tiền !", Toasty.LENGTH_LONG, true).show();
                    }
                } else {
                    Log.e(TAG, response.message());
                    Toasty.error(getApplicationContext(), "Không có dữ liệu hoặc gặp lỗi trong quá trình lấy dữ liệu chi tiết !", Toasty.LENGTH_LONG, true).show();
                }
                this.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Bill_TaxInvoiceDetail>> call, Throwable t) {
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
