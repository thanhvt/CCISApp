package com.es.ccisapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.es.model.Bill_TaxInvoice;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R.id.txtTenKH)
    TextView txtTenKH;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.txtVAT)
    TextView txtVAT;
    private Unbinder unbinder;
    // constant
    String TAG = "TaxInvoiceDetailFragment";

    public static final String EXTRA_DATA = "DATA_CONTENT";

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
            Bill_TaxInvoice taxInvoice =
                    (Bill_TaxInvoice) getArguments().getSerializable("TAX");
            Log.e(TAG, "taxInvoice: " + taxInvoice.toString());
            txtMaKH.setText(taxInvoice.getCustomerCode());
            txtTenKH.setText(taxInvoice.getCustomerName());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            txtVAT.setText(formatNumber(Long.parseLong(taxInvoice.getVAT().substring(0, taxInvoice.getVAT().indexOf(".")))));
            txtTotal.setText(formatNumber(Long.parseLong(taxInvoice.getTotal().substring(0, taxInvoice.getTotal().indexOf(".")))));
            txtSubTotal.setText(formatNumber(Long.parseLong(taxInvoice.getSubTotal().substring(0, taxInvoice.getSubTotal().indexOf(".")))));
            txtTaxCode.setText(taxInvoice.getTaxCode());
        }

        return rootView;
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
