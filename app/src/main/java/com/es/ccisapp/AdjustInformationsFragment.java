package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceDetail_DB;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.model.Concus_Customer_DB;
import com.es.model.DonGia_DB;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.Mobile_Adjust_Informations;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.printer.BluetoothPrinterActivity;
import com.es.utils.Utils;

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
    @BindView(R.id.rdDC)
    RadioButton rdDC;
    @BindView(R.id.rdTT)
    RadioButton rdTT;
    @BindView(R.id.rdDCHD)
    RadioButton rdDCHD;

    @BindView(R.id.edTuNgay)
    EditText edTuNgay;
    @BindView(R.id.edDenNgay)
    EditText edDenNgay;
    @BindView(R.id.spnDmucDonGia)
    Spinner spnDmucDonGia;

    @BindView(R.id.edMST)
    EditText edMST;

    @BindView(R.id.edPhone)
    EditText edPhone;

    @BindView(R.id.edEmail)
    EditText edEmail;

    @BindView(R.id.btnAdjOffline)
    Button btnAdjOffline;
    @BindView(R.id.btnAdjInformation)
    Button btnAdjInformation;


    // constant
    String TAG = "AdjustInformationsFragment";

    int priceId = -1;

    public static final String EXTRA_DATA = "DATA_CONTENT";
    Bill_TaxInvoice taxInvoice;
    private String content;
    boolean checkDayServer = false;

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
            list.add(item.getPriceId() + ". " + item.getDescription() + ": " + item.getPrice());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnDmucDonGia.setAdapter(adapter);
//        spnDmucDonGia.setTag();
        spnDmucDonGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    edDonGia.setText(spnDmucDonGia.getSelectedItem().toString().substring(spnDmucDonGia.getSelectedItem().toString().indexOf(": ") + 2));
                    priceId = Integer.parseInt(spnDmucDonGia.getSelectedItem().toString().substring(0, spnDmucDonGia.getSelectedItem().toString().indexOf(". ")));
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

            priceId = taxInvoice.getPriceId();
            edDC.setText(taxInvoice.getTaxInvoiceAddress());
            edTenKH.setText(taxInvoice.getCustomerName());
            edSL.setText(taxInvoice.getAmount() + "");

            edMST.setText(taxInvoice.getTaxCode());
            List<Concus_Customer_DB> customer_dbs = new Select().all().from(Concus_Customer_DB.class).where("CustomerCode = ?", taxInvoice.getCustomerCode()).execute();
            if (customer_dbs != null && customer_dbs.size() > 0) {
                edPhone.setText(customer_dbs.get(0).PhoneNumber);
                edEmail.setText(customer_dbs.get(0).Email);
            }

            //edDonGia.setText(taxInvoice.getSubTotal());
            List<DonGia_DB> info = new Select().all().from(DonGia_DB.class).execute();
            for (DonGia_DB d : info) {
                if (d.getPriceId() == taxInvoice.getPriceId()) {
                    edDonGia.setText(d.getPrice() + "");
                }
            }
//            edDonGia.setText(tmp.get(0).getPrice() + "");
            edSTT.setText(taxInvoice.getINDEX_THU());
            if (tmp.size() > 0) {
                edTuNgay.setText(tmp.get(0).TuNgay);
                edDenNgay.setText(tmp.get(0).DenNgay);
            }

            if (taxInvoice.isThuOffline() > 0) {
                btnAdjOffline.setEnabled(false);
                btnAdjInformation.setEnabled(false);
            }
        }
        return rootView;
    }


//    @OnClick(R.id.fab)
//    public void btnFab() {
//        btnAdjOffline();
//    }

    @OnClick(R.id.btnCopy)
    public void btnCopy() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.logo);

        builder.setMessage("Anh/Chị xác nhận sao chép thông tin KH " + taxInvoice.getCustomerName() + " và thu tiền, in hóa đơn KH mới: " + edTenKH.getText().toString() + " ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
                String strEmployeeCode = pref.getString("EMPLOYEECODE", "");

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
                m.setGiaSauThue(edDonGia.getText().toString());
                m.setType("3");
                m.setStatus(false);
                m.setDepartmentId(taxInvoice.getDepartmentId());
                m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString().replace(" ", "") : "");
                m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString().replace(" ", "") : "");
                m.setFigureBookId(taxInvoice.getFigureBookId() + "");
                m.setCustomerNew(taxInvoice.getCustomerId());
                m.setPriceId(priceId);
                m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
                m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
                m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");

                BigDecimal mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
                BigDecimal vat = new BigDecimal(taxInvoice.getTaxRatio());
                BigDecimal soLuong = new BigDecimal(edSL.getText().toString());
                BigDecimal donGia = new BigDecimal(edDonGia.getText().toString());
                BigDecimal soThang = (mTerm);


                //////////////////////////////////////////////
                BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
                dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);

                BigDecimal bDonGiaSauThue = new BigDecimal(edDonGia.getText().toString());
                BigDecimal tmpBig3 = vat.divide(new BigDecimal((100)));
                tmpBig3 = tmpBig3.add(new BigDecimal(1));
                tmpBig3 = bDonGiaSauThue.divide(tmpBig3, 3, RoundingMode.HALF_UP);
                m.setPrice(tmpBig3.toString());

                BigDecimal dSub = tmpBig3.multiply(soLuong).multiply(soThang);
                dSub = dSub.setScale(0, RoundingMode.HALF_UP);
                BigDecimal dVat = dTotal.subtract(dSub);
                //////////////////////////////////////////////

                m.setSubTotal(dSub + "");
                m.setTotal(dTotal + "");
                m.setTax(dVat + "");


                m.save();

                Toasty.success(getActivity(), "Đã sao chép thông tin offline KH " + taxInvoice.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
                Log.e("Adjust_Informations", m.toString());
                Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothPrinterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("TAX", taxInvoice);
                bundle.putInt("KIEU", 3);
                intent.putExtras(bundle);
                startActivity(intent);
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

    public void pushOffline() {
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
        m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString().replace(" ", "") : "");
        m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString().replace(" ", "") : "");
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setPriceId(priceId);
        m.setCustomerNew("-1");
        m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
        m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
        m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");
        m.setGiaSauThue(edDonGia.getText().toString());
        BigDecimal mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
        taxInvoiceDetailDbList.get(0).setTerm(mTerm.doubleValue());
        taxInvoiceDetailDbList.get(0).save();

        BigDecimal vat = new BigDecimal(taxInvoice.getTaxRatio());
        BigDecimal soLuong = new BigDecimal(edSL.getText().toString());
        BigDecimal donGia = new BigDecimal(edDonGia.getText().toString());
        BigDecimal soThang = (mTerm);

//        BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
//        dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
//        BigDecimal tmpBig = vat.divide(new BigDecimal((100)));
//        tmpBig = tmpBig.add(new BigDecimal(1));
//        tmpBig = dTotal.divide(tmpBig, 0, RoundingMode.HALF_UP);
//        BigDecimal dSub = tmpBig;
//
//        BigDecimal dVat = dTotal.subtract(dSub);
//        BigDecimal tmpBig3 = (soLuong).multiply(soThang);
//        tmpBig3 = dSub.divide(tmpBig3, 3, RoundingMode.HALF_UP);
//        m.setPrice(tmpBig3.toString());

        //////////////////////////////////////////////
        BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
        dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);

        BigDecimal bDonGiaSauThue = new BigDecimal(edDonGia.getText().toString());
        BigDecimal tmpBig3 = vat.divide(new BigDecimal((100)));
        tmpBig3 = tmpBig3.add(new BigDecimal(1));
        tmpBig3 = bDonGiaSauThue.divide(tmpBig3, 3, RoundingMode.HALF_UP);
        m.setPrice(tmpBig3.toString());

        BigDecimal dSub = tmpBig3.multiply(soLuong).multiply(soThang);
        dSub = dSub.setScale(0, RoundingMode.HALF_UP);
        BigDecimal dVat = dTotal.subtract(dSub);
        //////////////////////////////////////////////



        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");
        m.setTax(dVat + "");


        m.save();
        Log.e(TAG, dSub + " - " + dVat + " - " + dTotal + " - " + soLuong + " - " + soThang + " - " + m.getPrice() + " - " + tmpBig3.toString());
        Toasty.success(getActivity(), "Lưu thông tin offline thành công. Duyệt thông tin để đẩy dữ liệu lên Server !", Toasty.LENGTH_LONG, true).show();
        Log.e("Adjust_Informations", m.toString());

        List<Bill_TaxInvoiceModel> model = new Select().all().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();
        Bill_TaxInvoiceModel cu = model.get(0);
        cu.setCustomerName(m.getCustomerName());
        cu.setTaxInvoiceAddress(m.getCustomerAdd());
        cu.setINDEX_THU(m.getIndexSo());
        cu.setSubTotal(dSub + "");
        cu.setTotal(dTotal + "");
        cu.setVAT(dVat + "");
        cu.setPriceId(priceId);
        cu.save();

        btnAdjOffline.setEnabled(false);
        btnAdjInformation.setEnabled(false);
    }

    @OnClick(R.id.btnAdjOffline)
    public void btnAdjOffline() {
        if (checkDayServer) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.logo);

            builder.setMessage("Anh/Chị đã đẩy thông tin lên server. Anh chị muốn tiếp tục lưu offline ?");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    pushOffline();
                }
            });
            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.logo);

            builder.setMessage("Anh/Chị muốn thay đổi thông tin khách hàng và lưu trữ offline ?");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    pushOffline();
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
    }

    @OnClick(R.id.btnAdjInformation)
    public void btnAdjInformation() {

        List<Mobile_Adjust_DB> tmp = new Select().all().from(Mobile_Adjust_DB.class).where("IS_SUBMIT IS NULL AND CustomerID = '" + taxInvoice.getCustomerId() + "'").execute();
        if (tmp.size() > 0) {
            Toasty.warning(getActivity(), "KH này ĐÃ thực hiện thay đổi thông tin và lưu OFFLINE. Đề nghị Anh/Chị duyệt thông tin để đẩy lên server !", Toasty.LENGTH_LONG, true).show();
            return;
        }
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
        m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        m.setGiaSauThue(edDonGia.getText().toString());
        m.setStartDate(edTuNgay.getText() != null ? Utils.parseDate(edTuNgay.getText().toString().replace(" ", "")) : null);
        m.setEndDate(edDenNgay.getText() != null ? Utils.parseDate(edDenNgay.getText().toString().replace(" ", "")) : null);
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setPriceId(priceId);
        m.setCustomerNew("-1");
        m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
        m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
        m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");

        BigDecimal mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
        List<Bill_TaxInvoiceDetail_DB> taxInvoiceDetailDbList = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();


        BigDecimal vat = new BigDecimal(taxInvoice.getTaxRatio());
        BigDecimal soLuong = new BigDecimal(edSL.getText().toString());
        BigDecimal donGia = new BigDecimal(edDonGia.getText().toString());
        BigDecimal soThang = (mTerm);

//        BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
//        dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
//        BigDecimal tmpBig = vat.divide(new BigDecimal((100)));
//        tmpBig = tmpBig.add(new BigDecimal(1));
//        tmpBig = dTotal.divide(tmpBig, 0, RoundingMode.HALF_UP);
//        BigDecimal dSub = tmpBig;
//        BigDecimal dVat = dTotal.subtract(dSub);

        //////////////////////////////////////////////
        BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
        dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);

        BigDecimal bDonGiaSauThue = new BigDecimal(edDonGia.getText().toString());
        BigDecimal tmpBig3 = vat.divide(new BigDecimal((100)));
        tmpBig3 = tmpBig3.add(new BigDecimal(1));
        tmpBig3 = bDonGiaSauThue.divide(tmpBig3, 3, RoundingMode.HALF_UP);
        m.setPrice(tmpBig3.toString());

        BigDecimal dSub = tmpBig3.multiply(soLuong).multiply(soThang);
        dSub = dSub.setScale(0, RoundingMode.HALF_UP);
        BigDecimal dVat = dTotal.subtract(dSub);
        //////////////////////////////////////////////

        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");
        m.setTax(dVat + "");


        m.setMonth(Integer.parseInt(taxInvoice.getMonth()));
        m.setYear(Integer.parseInt(taxInvoice.getYear()));

        insertData(m);

        taxInvoiceDetailDbList.get(0).setTerm(mTerm.doubleValue());
        taxInvoiceDetailDbList.get(0).save();
        btnAdjOffline.setEnabled(false);
        btnAdjInformation.setEnabled(false);
        try {
            List<Bill_TaxInvoiceModel> model = new Select().all().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();
            Bill_TaxInvoiceModel cu = model.get(0);
            cu.setCustomerName(m.getCustomerName());
            cu.setTaxInvoiceAddress(m.getCustomerAdd());
            cu.setINDEX_THU(edSTT.getText().toString());
            cu.setSubTotal(dSub + "");
            cu.setTotal(dTotal + "");
            cu.setVAT(dVat + "");
            cu.setPriceId(priceId);
            cu.save();
        } catch (Exception e) {
            Log.e("ADJ", e.getMessage() + "");
        }
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
                            m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
                            m.setStatus(false);
                            m.setDepartmentId(taxInvoice.getDepartmentId());
                            m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString().replace(" ", "") : "");
                            m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString().replace(" ", "") : "");
                            m.setFigureBookId(taxInvoice.getFigureBookId() + "");
                            m.setPriceId(priceId);
                            m.setCustomerNew("-1");
                            m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
                            m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
                            m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");
                            m.setGiaSauThue(edDonGia.getText().toString());
                            BigDecimal mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
                            taxInvoiceDetailDbList.get(0).setTerm(mTerm.doubleValue());
                            taxInvoiceDetailDbList.get(0).save();

                            BigDecimal vat = new BigDecimal(taxInvoice.getTaxRatio());
                            BigDecimal soLuong = new BigDecimal(edSL.getText().toString());
                            BigDecimal donGia = new BigDecimal(edDonGia.getText().toString());
                            BigDecimal soThang = (mTerm);

//                            BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
//                            dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
//                            BigDecimal tmpBig = vat.divide(new BigDecimal((100)));
//                            tmpBig = tmpBig.add(new BigDecimal(1));
//                            tmpBig = dTotal.divide(tmpBig, 0, RoundingMode.HALF_UP);
//                            BigDecimal dSub = tmpBig;
//                            BigDecimal dVat = dTotal.subtract(dSub);
//                            BigDecimal tmpBig3 = (soLuong).multiply(soThang);
//                            tmpBig3 = dSub.divide(tmpBig3, 3, RoundingMode.HALF_UP);
//                            m.setPrice(tmpBig3.toString());
//                            Log.e(TAG, tmpBig3.toString());

                            //////////////////////////////////////////////
                            BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
                            dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);

                            BigDecimal bDonGiaSauThue = new BigDecimal(edDonGia.getText().toString());
                            BigDecimal tmpBig3 = vat.divide(new BigDecimal((100)));
                            tmpBig3 = tmpBig3.add(new BigDecimal(1));
                            tmpBig3 = bDonGiaSauThue.divide(tmpBig3, 3, RoundingMode.HALF_UP);
                            m.setPrice(tmpBig3.toString());

                            BigDecimal dSub = tmpBig3.multiply(soLuong).multiply(soThang);
                            dSub = dSub.setScale(0, RoundingMode.HALF_UP);
                            BigDecimal dVat = dTotal.subtract(dSub);
                            //////////////////////////////////////////////


                            m.setSubTotal(dSub + "");
                            m.setTotal(dTotal + "");
                            m.setTax(dVat + "");
                            m.setIS_SUBMIT("1");
                            m.save();

                            checkDayServer = true;
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
