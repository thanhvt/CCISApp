package com.es.ccisapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.es.adapter.TaxInvoiceAdapter;
import com.es.model.Bill_TaxInvoice;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CCISFragment extends Fragment {
    public static final String EXTRA_DATA = "DATA_CONTENT";
    Bill_TaxInvoice taxInvoice;
    private String content;
    private static final String TAG = CCISFragment.class.getSimpleName() + " msg: ";
    TaxInvoiceAdapter taxInvoiceAdapter;
    CCISDataService apiService;
    RecyclerView recyclerView;

    private void retrieveExtras() {
        if (getArguments() != null) {
            content = getArguments().getString(EXTRA_DATA);
        }
    }

    public CCISFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieveExtras();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ccis, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService =
                RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);

        Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice();
        call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(getActivity()) {
            @Override
            public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {
                List<Bill_TaxInvoice> movies = response.body();
                for (Bill_TaxInvoice b : movies) {
                    b.setChecked(false);
                }
                Log.d(TAG, "Number of movies received: " + movies.get(0).toString());
                taxInvoiceAdapter = new TaxInvoiceAdapter(movies, R.layout.list_taxinvoice, getContext());
                recyclerView.setAdapter(taxInvoiceAdapter);
                taxInvoiceAdapter.notifyDataSetChanged();
                this.mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Bill_TaxInvoice>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                this.mProgressDialog.dismiss();
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_ccis, menu);
//        return true;
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
                        call.enqueue(new CustomCallBack<Integer>(getActivity()) {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                this.mProgressDialog.dismiss();
                                Integer movies = response.body();
                                Log.d(TAG, "movies: " + movies);
                                if (movies == 1) {
                                    Toast.makeText(getActivity(), "Thu tiền khách hàng " + b.getCustomerName() + " thành công !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Thu tiền khách hàng " + b.getCustomerName() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toast.LENGTH_SHORT).show();
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
