package com.es.ccisapp;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdjustInformationsFragment extends Fragment {

    @BindView(R.id.txtTenKH)
    TextView txtTenKH;

    @BindView(R.id.txtDC)
    TextView txtDC;
    // constant
    String TAG = "AdjustInformationsFragment";

    public static final String EXTRA_DATA = "DATA_CONTENT";
    Bill_TaxInvoice taxInvoice;
    private String content;

    public static AdjustInformationsFragment newInstance(String data) {
        AdjustInformationsFragment fragment = new AdjustInformationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void retrieveExtras() {
        if (getArguments() != null) {
            content = getArguments().getString(EXTRA_DATA);
        }
    }

    public AdjustInformationsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_adjustinformations, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            content = getArguments().getString(EXTRA_DATA);
            taxInvoice =
                    (Bill_TaxInvoice) getArguments().getSerializable("TAX");
            Log.e(TAG, "taxInvoice: " + taxInvoice.toString());
            txtDC.setText(taxInvoice.getTaxInvoiceAddress());
            txtTenKH.setText(taxInvoice.getCustomerName());

        }
        return rootView;
    }

    @OnClick(R.id.btnAdjInformation)
    public void btnAdjInformation() {
        Toast.makeText(getActivity(), "Lưu thông tin khách hàng thành công !", Toast.LENGTH_SHORT).show();
//        CCISDataService apiService =
//                RetrofitInstance.getRetrofitInstance().create(CCISDataService.class);
//        Call<Integer> call = apiService.ThuTien((taxInvoice.getTaxInvoiceId()));
//        call.enqueue(new Callback<Integer>() {
//            @Override
//            public void onResponse(Call<Integer>call, Response<Integer> response) {
//                Integer movies = response.body();
//                Log.d(TAG, "movies: " + movies);
//                Toast.makeText(getActivity(), "Thu tiền khách hàng " + txtTenKH.getText() + " thành công !", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Integer>call, Throwable t) {
//                // Log error here since request failed
//                Log.e(TAG, t.toString());
//            }
//        });

    }
}
