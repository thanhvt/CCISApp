package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.es.adapter.TaxInvoiceAdapter;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;
import com.es.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    List<Bill_TaxInvoice> lstTaxInvoiceData;
    @BindView(R.id.empty_view)
    TextView txtEmpty;


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
        lstTaxInvoiceData = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Bill_TaxInvoiceModel> lstDB = new Select().all().from(Bill_TaxInvoiceModel.class).execute();
        Log.e(TAG, lstDB.size() + "");
        if (lstDB.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            for (Bill_TaxInvoiceModel b : lstDB) {
                b.setChecked(false);
                lstTaxInvoiceData.add(new Bill_TaxInvoice(b.getTaxCode(), b.getCustomerCode(),
                        b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(), "1",
                        b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                        b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                        b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked()));
            }
            taxInvoiceAdapter = new TaxInvoiceAdapter(lstTaxInvoiceData, R.layout.list_taxinvoice, getContext());
            recyclerView.setAdapter(taxInvoiceAdapter);
            taxInvoiceAdapter.notifyDataSetChanged();
        }

        apiService =
                RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

    public void getDataTaxInvoice(int trangThai) {

        Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice(trangThai);
        call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(getActivity()) {
            @Override
            public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {
                try {
                    lstTaxInvoiceData = response.body();
                    if (lstTaxInvoiceData.size() > 0) {
                        for (Bill_TaxInvoice b : lstTaxInvoiceData) {
                            b.setChecked(false);
                        }
                        Log.e(TAG, "Bill_TaxInvoice[0] received: " + lstTaxInvoiceData.get(0).toString());
                        taxInvoiceAdapter = new TaxInvoiceAdapter(lstTaxInvoiceData, R.layout.list_taxinvoice, getContext());
                        recyclerView.setAdapter(taxInvoiceAdapter);
                        taxInvoiceAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    this.mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Bill_TaxInvoice>> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getActivity(), "Không có dữ liệu hoặc xảy ra lỗi quá trình lấy dữ liệu ! ", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
                this.mProgressDialog.dismiss();
            }
        });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Anh/Chị muốn thu tiền theo lô ?");
                builder.setIcon(R.drawable.logo);
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

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
                                            Toast.makeText(getActivity(), "Thu tiền khách hàng " + b.getCustomerName() + " thành công !", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Thu tiền khách hàng " + b.getCustomerName() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toast.LENGTH_LONG).show();
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
                });
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

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
        if (id == R.id.action_all) {
            if (!Utils.isOnline(getContext())) {
                Toast.makeText(getActivity(), "Yêu cầu kết nối mạng để thực hiện !", Toast.LENGTH_LONG).show();
            } else {
                getDataTaxInvoice(-1);
            }
        }
        if (id == R.id.action_dathu) {
            if (!Utils.isOnline(getContext())) {
                Toast.makeText(getActivity(), "Yêu cầu kết nối mạng để thực hiện !", Toast.LENGTH_LONG).show();
            } else {
                getDataTaxInvoice(1);
            }
        }
        if (id == R.id.action_chuathu) {
            if (!Utils.isOnline(getContext())) {
                Toast.makeText(getActivity(), "Yêu cầu kết nối mạng để thực hiện !", Toast.LENGTH_LONG).show();
            } else {
                getDataTaxInvoice(0);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}