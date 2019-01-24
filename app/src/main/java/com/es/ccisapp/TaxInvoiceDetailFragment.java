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

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.model.Mobile_Adjust_DB;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.printer.BluetoothPrinterActivity;
import com.es.utils.CustomCallBack;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaxInvoiceDetailFragment extends Fragment {

    @BindView(R.id.txtMaKH)
    TextView txtMaKH;
    @BindView(R.id.txtSubTotal)
    TextView txtSubTotal;
    @BindView(R.id.txtDiaChi)
    TextView txtDiaChi;
    @BindView(R.id.edTenKH)
    TextView txtTenKH;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.txtVAT)
    TextView txtVAT;
    @BindView(R.id.txtTinhTrangThu)
    TextView txtTinhTrangThu;
    @BindView(R.id.txtKy)
    TextView txtKy;
    @BindView(R.id.txtSoNhanKhau)
    TextView txtSoNhanKhau;
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
            try {
                List<Mobile_Adjust_DB> lstDB = new Select().all().from(Mobile_Adjust_DB.class).where("CustomerID = ?", taxInvoice.getCustomerId()).execute();
                if (lstDB != null && lstDB.size() > 0) {
                    Mobile_Adjust_DB m = lstDB.get(lstDB.size() - 1);
                    taxInvoice.setCustomerName(m.getCustomerName());
                    taxInvoice.setAddress_Pay(m.getCustomerAdd());
                    taxInvoice.setAmount(Double.parseDouble(lstDB.get(0).getAmout()));
                    taxInvoice.setSubTotal(lstDB.get(0).getPrice());
//                    if (!m.getPrice().equals("")){
//                        bill_taxInvoice.setSubTotal(m.getPrice());
//                        Double d = Double.parseDouble(m.getPrice());
//                        Double total = d * 1.01;
//                    }

                }
            } catch (Exception e) {

            }

            Log.e(TAG, "taxInvoice: " + taxInvoice.toString());
            txtMaKH.setText(taxInvoice.getCustomerCode());
            txtTenKH.setText(taxInvoice.getCustomerName());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            txtVAT.setText(formatNumber(Long.parseLong(taxInvoice.getVAT().substring(0, taxInvoice.getVAT().indexOf(".")))) + " (VNĐ)");
            txtTotal.setText(formatNumber(Long.parseLong(taxInvoice.getTotal().substring(0, taxInvoice.getTotal().indexOf(".")))) + " (VNĐ)");
            txtSubTotal.setText(formatNumber(Long.parseLong(taxInvoice.getSubTotal().substring(0, taxInvoice.getSubTotal().indexOf(".")))) + " (VNĐ)");
            txtDiaChi.setText(taxInvoice.getTaxInvoiceAddress());
            txtTinhTrangThu.setText(taxInvoice.isThuOffline() ? "Đã thu offline" : "Chưa thu");
            txtKy.setText(taxInvoice.getMonth() + "/" + taxInvoice.getYear());
            txtSoNhanKhau.setText(taxInvoice.getAmount() + "");
        }

        return rootView;
    }

    @OnClick(R.id.btnThuOff)
    public void btnThuOff() {
        Bill_TaxInvoiceModel details = new Select().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).executeSingle();
        if (details != null) {
            details.setThuOffline(true);
            details.save();
            Log.e(TAG + " update ", "1");
        } else {
            Bill_TaxInvoiceModel c = new Bill_TaxInvoiceModel(taxInvoice.getTaxCode(), taxInvoice.getCustomerCode(),
                    taxInvoice.getBankName(), taxInvoice.getMonth(), taxInvoice.getSerialNumber(), taxInvoice.getYear(), taxInvoice.getCustomerId(), taxInvoice.getDepartmentId(),
                    taxInvoice.getTaxInvoiceAddress(), taxInvoice.getTaxInvoiceId(), taxInvoice.getIdDevice(), taxInvoice.getContractId(), taxInvoice.getFigureBookId(), taxInvoice.getSerialCode(),
                    taxInvoice.getCustomerName(), taxInvoice.getCustomerCode_Pay(), taxInvoice.getSubTotal(), taxInvoice.getAddress_Pay(), taxInvoice.getBankAccount(), taxInvoice.getVAT(),
                    taxInvoice.getTaxRatio(), taxInvoice.getCustomerId_Pay(), taxInvoice.getBillType(), taxInvoice.getCustomerName_Pay(), taxInvoice.getTotal(), taxInvoice.isChecked(), true, taxInvoice.getAmount(), taxInvoice.getServiceTypeId(), taxInvoice.getServiceName());
            c.save();
            Log.e(TAG + " insert ", "2");
        }
        Toast.makeText(getActivity(), "Thu tiền offline khách hàng " + taxInvoice.getCustomerName() + " thành công !", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnThuTien)
    public void btnThuTien() {
        CCISDataService apiService =
                RetrofitInstance.getRetrofitInstance(getActivity().getApplicationContext()).create(CCISDataService.class);
        Call<Integer> call = apiService.ThuTien((taxInvoice.getTaxInvoiceId()));
        call.enqueue(new CustomCallBack<Integer>(getActivity()) {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                this.mProgressDialog.dismiss();
                Integer movies = response.body();
                Log.d(TAG, "movies: " + movies);
                if (movies == 1) {
                    List<Bill_TaxInvoiceModel> info = new Delete().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();
                    Toasty.success(getActivity(), "Thu tiền khách hàng " + txtTenKH.getText() + " thành công !", Toasty.LENGTH_LONG, true).show();
                } else {
                    Toasty.error(getActivity(), "Thu tiền khách hàng " + txtTenKH.getText() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toasty.LENGTH_LONG, true).show();
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

    @OnClick(R.id.btnInHD)
    public void btnInHD() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothPrinterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TAX", taxInvoice);
        intent.putExtras(bundle);
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
