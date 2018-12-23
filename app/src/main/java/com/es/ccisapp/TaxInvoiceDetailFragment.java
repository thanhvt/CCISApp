package com.es.ccisapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.es.model.Bill_TaxInvoice;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.printer.BluetoothPrinterActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaxInvoiceDetailFragment extends Fragment {

    @BindView(R.id.txtMaKH)
    TextView txtMaKH;
    @BindView(R.id.txtSubTotal)
    TextView txtSubTotal;
    @BindView(R.id.txtTaxCode)
    TextView txtTaxCode;
    @BindView(R.id.edTenKH)
    TextView txtTenKH;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.txtVAT)
    TextView txtVAT;
    private Unbinder unbinder;
    // constant
    String TAG = "TaxInvoiceDetailFragment";

    public static final String EXTRA_DATA = "DATA_CONTENT";
    Bill_TaxInvoice taxInvoice;
    private String content;

    public static TaxInvoiceDetailFragment newInstance(String data) {
        TaxInvoiceDetailFragment fragment = new TaxInvoiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TaxInvoiceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_taxinvoicedetail, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            content = getArguments().getString(EXTRA_DATA);
            taxInvoice =
                    (Bill_TaxInvoice) getArguments().getSerializable("TAX");
            Log.e(TAG, "taxInvoice: " + taxInvoice.toString());
            txtMaKH.setText(taxInvoice.getCustomerCode());
            txtTenKH.setText(taxInvoice.getCustomerName());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            txtVAT.setText(formatNumber(Long.parseLong(taxInvoice.getVAT().substring(0, taxInvoice.getVAT().indexOf(".")))) + " (VNĐ)");
            txtTotal.setText(formatNumber(Long.parseLong(taxInvoice.getTotal().substring(0, taxInvoice.getTotal().indexOf(".")))) + " (VNĐ)");
            txtSubTotal.setText(formatNumber(Long.parseLong(taxInvoice.getSubTotal().substring(0, taxInvoice.getSubTotal().indexOf(".")))) + " (VNĐ)");
            txtTaxCode.setText(taxInvoice.getTaxCode());
        }

        return rootView;
    }

    @OnClick(R.id.btnThuTien)
    public void btnThuTien() {
        CCISDataService apiService =
                RetrofitInstance.getRetrofitInstance(getActivity().getApplicationContext()).create(CCISDataService.class);
        Call<Integer> call = apiService.ThuTien((taxInvoice.getTaxInvoiceId()));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer movies = response.body();
                Log.d(TAG, "movies: " + movies);
                if (movies == 1) {
                    Toast.makeText(getActivity(), "Thu tiền khách hàng " + txtTenKH.getText() + " thành công !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Thu tiền khách hàng " + txtTenKH.getText() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @OnClick(R.id.btnInHD)
    public void btnInHD() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothPrinterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnGuiSMS)
    public void btnGuiSMS() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Thông báo tiền điện ");
//        sendIntent.putExtra("address", "0376340xxx");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(Intent.createChooser(sendIntent, "SMS:"));
    }

    public String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            String resp = formatter.format(number);
            resp = resp.replaceAll(",", ".");
            return resp;
        } catch (Exception e) {
            return "";
        }
    }
}
