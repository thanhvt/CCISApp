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
            edDonGia.setText(tmp.get(0).getPrice() + "");
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
                m.setPrice(edDonGia.getText().toString());
                m.setType("3");
                m.setStatus(false);
                m.setDepartmentId(taxInvoice.getDepartmentId());
                m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString() : "");
                m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString() : "");
                m.setFigureBookId(taxInvoice.getFigureBookId() + "");
                m.setCustomerNew(taxInvoice.getCustomerId());
                m.setPriceId(priceId);
                m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
                m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
                m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");

                String vat = taxInvoice.getTaxRatio();
                BigDecimal a = new BigDecimal(edSL.getText().toString());
                BigDecimal b = new BigDecimal(edDonGia.getText().toString());
                int mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));

                BigDecimal c = new BigDecimal(mTerm);
                Log.e(TAG + " tinh toan", a + " - " + b + " - " + c);
                BigDecimal dSub = a.multiply(b).multiply(c);
                Log.e(TAG + " tinh dSub", dSub + "");
                dSub = dSub.setScale(2, RoundingMode.CEILING);
                BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
                dVat = dVat.setScale(2, RoundingMode.CEILING);
                BigDecimal dTotal = dSub.add(dVat);
                dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
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
        m.setPrice(edDonGia.getText().toString());
        m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
        m.setStatus(false);
        m.setDepartmentId(taxInvoice.getDepartmentId());
        m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString() : "");
        m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString() : "");
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setPriceId(priceId);
        m.setCustomerNew("-1");
        m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
        m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
        m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");

        int mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
        taxInvoiceDetailDbList.get(0).setTerm(mTerm);
        taxInvoiceDetailDbList.get(0).save();

        String vat = taxInvoice.getTaxRatio();
        BigDecimal a = new BigDecimal(edSL.getText().toString());
        BigDecimal b = new BigDecimal(edDonGia.getText().toString());
        BigDecimal c = new BigDecimal(mTerm);
        BigDecimal dSub = a.multiply(b).multiply(c);
        dSub = dSub.setScale(2, RoundingMode.CEILING);
        BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
        dVat = dVat.setScale(2, RoundingMode.CEILING);
        BigDecimal dTotal = dSub.add(dVat);
        dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");

        m.setTax(dVat + "");
        m.save();

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

        m.setStartDate(edTuNgay.getText() != null ? Utils.parseDate(edTuNgay.getText().toString()) : null);
        m.setEndDate(edDenNgay.getText() != null ? Utils.parseDate(edDenNgay.getText().toString()) : null);
        m.setFigureBookId(taxInvoice.getFigureBookId() + "");
        m.setPriceId(priceId);
        m.setCustomerNew("-1");
        m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
        m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
        m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");

        int mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
        List<Bill_TaxInvoiceDetail_DB> taxInvoiceDetailDbList = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();

        String vat = taxInvoice.getTaxRatio();
        BigDecimal a = new BigDecimal(edSL.getText().toString());
        BigDecimal b = new BigDecimal(edDonGia.getText().toString());
        BigDecimal c = new BigDecimal(mTerm);
        BigDecimal dSub = a.multiply(b).multiply(c);
        dSub = dSub.setScale(2, RoundingMode.CEILING);
        BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
        dVat = dVat.setScale(2, RoundingMode.CEILING);
        BigDecimal dTotal = dSub.add(dVat);
        dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
        m.setSubTotal(dSub + "");
        m.setTotal(dTotal + "");

        m.setTax(dVat + "");
        Log.e("Adjust_Informations", m.toString());

        m.setMonth(Integer.parseInt(taxInvoice.getMonth()));
        m.setYear(Integer.parseInt(taxInvoice.getYear()));

        insertData(m);

        taxInvoiceDetailDbList.get(0).setTerm(mTerm);
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
                            m.setPrice(edDonGia.getText().toString());
                            m.setType(rdTT.isChecked() ? "0" : rdDC.isChecked() ? "1" : "2");
                            m.setStatus(false);
                            m.setDepartmentId(taxInvoice.getDepartmentId());
                            m.setStartDate(edTuNgay.getText() != null ? edTuNgay.getText().toString() : "");
                            m.setEndDate(edDenNgay.getText() != null ? edDenNgay.getText().toString() : "");
                            m.setFigureBookId(taxInvoice.getFigureBookId() + "");
                            m.setPriceId(priceId);
                            m.setCustomerNew("-1");
                            m.setTaxCode(edMST.getText() != null ? edMST.getText().toString() : "");
                            m.setPhoneNumber(edPhone.getText() != null ? edPhone.getText().toString() : "");
                            m.setEmail(edEmail.getText() != null ? edEmail.getText().toString() : "");

                            int mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(edDenNgay.getText().toString()), Utils.parseDate(edTuNgay.getText().toString()));
                            taxInvoiceDetailDbList.get(0).setTerm(mTerm);
                            taxInvoiceDetailDbList.get(0).save();

                            String vat = taxInvoice.getTaxRatio();
                            BigDecimal a = new BigDecimal(edSL.getText().toString());
                            BigDecimal b = new BigDecimal(edDonGia.getText().toString());
                            BigDecimal c = new BigDecimal(mTerm);
                            BigDecimal dSub = a.multiply(b).multiply(c);
                            dSub = dSub.setScale(2, RoundingMode.CEILING);
                            BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
                            dVat = dVat.setScale(2, RoundingMode.CEILING);
                            BigDecimal dTotal = dSub.add(dVat);
                            dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
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
