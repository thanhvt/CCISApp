package com.es.ccisapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.Mobile_Adjust_Informations;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdjustInformationsFragment extends Fragment {

    @BindView(R.id.edTenKH)
    EditText edTenKH;
    @BindView(R.id.edDC)
    EditText edDC;
    @BindView(R.id.edSL)
    EditText edSL;
    @BindView(R.id.edSTT)
    EditText edSTT;
    @BindView(R.id.edDonGia)
    EditText edDonGia;
    @BindView(R.id.rdDC)
    RadioButton rdDC;
    @BindView(R.id.rdTT)
    RadioButton rdTT;
    @BindView(R.id.rdDCHD)
    RadioButton rdDCHD;
    @BindView(R.id.fab)
    FloatingActionButton fab;

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
            edDC.setText(taxInvoice.getTaxInvoiceAddress());
            edTenKH.setText(taxInvoice.getCustomerName());
            edSL.setText(taxInvoice.getAmount() + "");
            edDonGia.setText(taxInvoice.getSubTotal());
        }
        return rootView;
    }


    @OnClick(R.id.fab)
    public void btnFab() {
        btnAdjOffline();
    }
    @OnClick(R.id.btnAdjOffline)
    public void btnAdjOffline() {
        int ran = new Random().nextInt();
        List<Mobile_Adjust_DB> tmp = new Select().all().from(Mobile_Adjust_DB.class).where("AdjustID = ?", ran).execute();
        while (tmp.size() > 0) {
            ran = new Random().nextInt();
            tmp = new Select().all().from(Mobile_Adjust_DB.class).where("AdjustID = ?", ran).execute();
        }

        Mobile_Adjust_DB m = new Mobile_Adjust_DB();
        m.setAdjustID(ran + "");
        m.setAmout(edSL.getText().toString());
        m.setCustomerAdd(edDC.getText().toString());
        m.setCustomerID(taxInvoice.getCustomerId());
        m.setCustomerName(edTenKH.getText().toString());
        m.setEmployeeCode("2");
        m.setIndexSo(edSTT.getText().toString());
        m.setPrice(edDonGia.getText().toString());
        m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        m.save();

        Toasty.success(getActivity(), "Lưu thông tin offline thành công. Duyệt thông tin để đẩy dữ liệu lên Server !", Toasty.LENGTH_LONG, true).show();
        Log.e("Adjust_Informations", m.toString());
    }

    @OnClick(R.id.btnAdjInformation)
    public void btnAdjInformation() {
        Mobile_Adjust_Informations m = new Mobile_Adjust_Informations();
        m.setAmout(edSL.getText().toString());
        m.setCustomerAdd(edDC.getText().toString());
        m.setCustomerID(taxInvoice.getCustomerId());
        m.setCustomerName(edTenKH.getText().toString());
        m.setEmployeeCode("2");
        m.setIndex(edSTT.getText().toString());
        m.setPrice(edDonGia.getText().toString());
        m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        Log.e("Adjust_Informations", m.toString());
        insertData(m);
    }

    private void insertData(Mobile_Adjust_Informations devices) {
        try {
            CCISDataService service = RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);
            Call<Boolean> call = service.Post(devices);
            Log.wtf("URL Called", call.request().url() + "");
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    try {
                        Log.e(TAG, response.message());
                        Boolean postCheck = response.body().booleanValue();
                        Log.e("CHECK PUT", postCheck + "");
                        if (postCheck) {
                            Toasty.success(getActivity(), "Lưu thông tin khách hàng thành công !", Toasty.LENGTH_LONG, true).show();
                        } else {
                            Toasty.error(getActivity(), "Lưu thông tin khách hàng không thành công !", Toasty.LENGTH_LONG, true).show();
                        }
                    } catch (Exception e) {
                        Toasty.error(getActivity(), "Lưu thông tin khách hàng không thành công !", Toasty.LENGTH_LONG, true).show();
                        Log.e(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("USERDEVICE", t.getMessage() + "");
                }

            });
        } catch (Exception e) {

        }
    }
}
