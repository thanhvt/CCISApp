package com.es.ccisapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceDetail_DB;
import com.es.model.DonGia_DB;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.Mobile_Adjust_Informations;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    //    @BindView(R.id.rdDC)
//    RadioButton rdDC;
//    @BindView(R.id.rdTT)
//    RadioButton rdTT;
//    @BindView(R.id.rdDCHD)
//    RadioButton rdDCHD;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.edTuNgay)
    EditText edTuNgay;
    @BindView(R.id.edDenNgay)
    EditText edDenNgay;
    @BindView(R.id.spnDmucDonGia)
    Spinner spnDmucDonGia;

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

        // DonGia{PriceId=1, OccupationsGroupCode='HDAN', Description='HDAN - Hộ dân', Time='KT', PriceRound=6000.0, Price=5454.55}
        List<String> list = new ArrayList<>();
        List<DonGia_DB> info = new Select().all().from(DonGia_DB.class).execute();
        for (DonGia_DB item : info) {
            list.add(item.getDescription() + ": " + item.getPrice());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnDmucDonGia.setAdapter(adapter);
        spnDmucDonGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    edDonGia.setText(spnDmucDonGia.getSelectedItem().toString().substring(spnDmucDonGia.getSelectedItem().toString().indexOf(": ") + 2));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            List<Bill_TaxInvoiceDetail_DB> tmp = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();
            Log.e(TAG, "Bill_TaxInvoiceDetail_DB: " + tmp.size());
            Log.e(TAG, "taxInvoice: " + taxInvoice.toString());
            edDC.setText(taxInvoice.getTaxInvoiceAddress());
            edTenKH.setText(taxInvoice.getCustomerName());
            edSL.setText(taxInvoice.getAmount() + "");
            edDonGia.setText(taxInvoice.getSubTotal());
            if (tmp.size() > 0) {
                edTuNgay.setText(tmp.get(0).TuNgay);
                edDenNgay.setText(tmp.get(0).DenNgay);
            }
        }
        return rootView;
    }


    @OnClick(R.id.fab)
    public void btnFab() {
        btnAdjOffline();
    }

    @OnClick(R.id.btnCopy)
    public void btnCopy() {
        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
        String strEmployeeCode = pref.getString("EMPLOYEECODE", "");

        List<Bill_TaxInvoiceDetail_DB> taxInvoiceDetailDbList = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();

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
        m.setEmployeeCode(strEmployeeCode);
        m.setIndexSo(edSTT.getText().toString());
        m.setPrice(edDonGia.getText().toString());
        m.setType("0");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString() : "");
        m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString() : "");
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setTax(taxInvoice.getTaxRatio());
        m.setCustomerNew(taxInvoice.getCustomerId());

        String vat = taxInvoice.getTaxRatio();
        BigDecimal a = new BigDecimal(edSL.getText().toString());
        BigDecimal b = new BigDecimal(edDonGia.getText().toString());
        BigDecimal c = new BigDecimal(taxInvoiceDetailDbList.get(0).getTerm());
        BigDecimal dSub = a.multiply(b).multiply(c);
        dSub = dSub.setScale(2, RoundingMode.CEILING);
        BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
        dVat = dVat.setScale(2, RoundingMode.CEILING);
        BigDecimal dTotal = dSub.add(dVat);
        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");

        m.save();

        Toasty.success(getActivity(), "Đã sao chép thông tin offline KH " + taxInvoice.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
        Log.e("Adjust_Informations", m.toString());
    }

    @OnClick(R.id.btnAdjOffline)
    public void btnAdjOffline() {
        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
        String strEmployeeCode = pref.getString("EMPLOYEECODE", "");

        List<Bill_TaxInvoiceDetail_DB> taxInvoiceDetailDbList = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();

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
        m.setEmployeeCode(strEmployeeCode);
        m.setIndexSo(edSTT.getText().toString());
        m.setPrice(edDonGia.getText().toString());
        m.setType("0");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString() : "");
        m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString() : "");
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setTax(taxInvoice.getTaxRatio());
        m.setCustomerNew("-1");

        String vat = taxInvoice.getTaxRatio();
        BigDecimal a = new BigDecimal(edSL.getText().toString());
        BigDecimal b = new BigDecimal(edDonGia.getText().toString());
        BigDecimal c = new BigDecimal(taxInvoiceDetailDbList.get(0).getTerm());
        BigDecimal dSub = a.multiply(b).multiply(c);
        dSub = dSub.setScale(2, RoundingMode.CEILING);
        BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
        dVat = dVat.setScale(2, RoundingMode.CEILING);
        BigDecimal dTotal = dSub.add(dVat);
        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");

        m.save();

        Toasty.success(getActivity(), "Lưu thông tin offline thành công. Duyệt thông tin để đẩy dữ liệu lên Server !", Toasty.LENGTH_LONG, true).show();
        Log.e("Adjust_Informations", m.toString());
    }

    @OnClick(R.id.btnAdjInformation)
    public void btnAdjInformation() {
        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
        String strEmployeeCode = pref.getString("EMPLOYEECODE", "");
        Mobile_Adjust_Informations m = new Mobile_Adjust_Informations();
        m.setAmout(edSL.getText().toString());
        m.setCustomerAdd(edDC.getText().toString());
        m.setCustomerID(taxInvoice.getCustomerId());
        m.setCustomerName(edTenKH.getText().toString());
        m.setEmployeeCode(strEmployeeCode);
        m.setIndex(edSTT.getText().toString());
        m.setPrice(edDonGia.getText().toString());
        m.setType("0");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());

        m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString() : "");
        m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString() : "");
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setTax(taxInvoice.getTaxRatio());
        m.setCustomerNew("-1");

        List<Bill_TaxInvoiceDetail_DB> taxInvoiceDetailDbList = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();

        String vat = taxInvoice.getTaxRatio();
        BigDecimal a = new BigDecimal(edSL.getText().toString());
        BigDecimal b = new BigDecimal(edDonGia.getText().toString());
        BigDecimal c = new BigDecimal(taxInvoiceDetailDbList.get(0).getTerm());
        BigDecimal dSub = a.multiply(b).multiply(c);
        dSub = dSub.setScale(2, RoundingMode.CEILING);
        BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
        dVat = dVat.setScale(2, RoundingMode.CEILING);
        BigDecimal dTotal = dSub.add(dVat);
        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");

        Log.e("Adjust_Informations", m.toString());
        insertData(m);
    }

    private void insertData(Mobile_Adjust_Informations devices) {
        try {
            CCISDataService service = RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);
            Call<String> call = service.Post(devices);
            Log.wtf("URL Called", call.request().url() + "");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        Log.e(TAG, response.message());
                        String postCheck = response.body().toString();
                        Log.e("CHECK PUT", postCheck + "");
                        if (postCheck.equals("OK")) {
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
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("USERDEVICE", t.getMessage() + "");
                }

            });
        } catch (Exception e) {

        }
    }
}
