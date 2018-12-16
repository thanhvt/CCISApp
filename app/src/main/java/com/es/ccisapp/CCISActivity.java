package com.es.ccisapp;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.es.adapter.TaxInvoiceAdapter;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceList;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CCISActivity extends AppCompatActivity {
    private static final String TAG = CCISActivity.class.getSimpleName() + " msg: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccis);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CCISDataService apiService =
                RetrofitInstance.getRetrofitInstance().create(CCISDataService.class);

        Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice();
        call.enqueue(new Callback<List<Bill_TaxInvoice>>() {
            @Override
            public void onResponse(Call<List<Bill_TaxInvoice>>call, Response<List<Bill_TaxInvoice>> response) {
                List<Bill_TaxInvoice> movies = response.body();
                Log.d(TAG, "Number of movies received: " + movies.get(0).toString());
                recyclerView.setAdapter(new TaxInvoiceAdapter(movies, R.layout.list_taxinvoice, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<List<Bill_TaxInvoice>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
