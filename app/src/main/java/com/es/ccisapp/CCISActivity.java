package com.es.ccisapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.es.adapter.TaxInvoiceAdapter;
import com.es.model.Bill_TaxInvoice;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CCISActivity extends AppCompatActivity {
    private static final String TAG = CCISActivity.class.getSimpleName() + " msg: ";
    TaxInvoiceAdapter taxInvoiceAdapter;
    CCISDataService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccis);

        SharedPreferences pref = getSharedPreferences("LOGIN", 0);
        int strUserID = pref.getInt("USERID", -1);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService =
                RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(CCISDataService.class);

        Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice(1, strUserID);
        call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(this) {
            @Override
            public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {
                List<Bill_TaxInvoice> movies = response.body();
                for (Bill_TaxInvoice b : movies) {
                    b.setChecked(false);
                }
                Log.d(TAG, "Number of movies received: " + movies.get(0).toString());
                taxInvoiceAdapter = new TaxInvoiceAdapter(movies, R.layout.list_taxinvoice, getApplicationContext());
                recyclerView.setAdapter(taxInvoiceAdapter);
                taxInvoiceAdapter.notifyDataSetChanged();
                this.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Bill_TaxInvoice>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
                    Toast.makeText(getApplicationContext(), "Không có dữ liệu chưa thu tiền. Đề nghị kiểm tra lại !", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Gặp lỗi trong quá trình lấy dữ liệu !", Toast.LENGTH_LONG).show();
                }
                this.mProgressDialog.dismiss();
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        if (id == R.id.action_thutien) {
            if (taxInvoiceAdapter != null) {
                String strID = "";
                List<Bill_TaxInvoice> stList = taxInvoiceAdapter.getLstTaxInvoice();
                for (final Bill_TaxInvoice b : stList) {
                    if (b.isChecked()) {
                        Log.e(TAG, b.toString());
                        Call<Integer> call = apiService.ThuTien((b.getTaxInvoiceId()));
                        call.enqueue(new CustomCallBack<Integer>(this) {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                this.mProgressDialog.dismiss();
                                Integer movies = response.body();
                                Log.d(TAG, "movies: " + movies);
                                if (movies == 1) {
                                    Toast.makeText(getApplicationContext(), "Thu tiền khách hàng " + b.getCustomerName() + " thành công !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Thu tiền khách hàng " + b.getCustomerName() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toast.LENGTH_SHORT).show();
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
            return true;
        }
        if (id == R.id.action_daochon) {
            if (taxInvoiceAdapter != null) {
                String strID = "";
                List<Bill_TaxInvoice> stList = taxInvoiceAdapter.getLstTaxInvoice();
                for (Bill_TaxInvoice b : stList) {
                    if (b.isChecked()) {
                        b.setChecked(false);
                    } else
                        b.setChecked(true);
                }
                taxInvoiceAdapter.notifyDataSetChanged();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
