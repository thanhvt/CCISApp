package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceDetail_DB;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.model.Mobile_Adjust_DB;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.printer.BluetoothPrinterActivity;
import com.es.utils.CustomCallBack;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    //    @BindView(R.id.txtKy)
//    TextView txtKy;
    @BindView(R.id.txtSoNhanKhau)
    TextView txtSoNhanKhau;
    @BindView(R.id.txtTuDen)
    TextView txtTuDen;
    @BindView(R.id.btnInHD)
    Button btnInHD;

    @BindView(R.id.btnThuOff)
    Button btnThuOff;

    @BindView(R.id.btnThuTien)
    Button btnThuTien;


    private Unbinder unbinder;
    // constant
    String TAG = "TaxInvoiceDetailFragment";
    int kieu = 0;
    public static final String EXTRA_DATA = "DATA_CONTENT";
    Bill_TaxInvoice taxInvoice;
    private String content;
//    List<Bill_TaxInvoiceDetail> lstDetail = new ArrayList<>();

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
                List<Bill_TaxInvoiceDetail_DB> tmp = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();
                Log.e(TAG, "Bill_TaxInvoiceDetail_DB: " + tmp.size());

                List<Mobile_Adjust_DB> lstDB = new Select().all().from(Mobile_Adjust_DB.class).where("TYPE != '3' and CustomerID = ?", taxInvoice.getCustomerId()).execute();
                if (lstDB != null && lstDB.size() > 0) {
                    Mobile_Adjust_DB m = lstDB.get(lstDB.size() - 1);
                    taxInvoice.setCustomerName(m.getCustomerName());
                    taxInvoice.setAddress_Pay(m.getCustomerAdd());
                    taxInvoice.setAmount(Double.parseDouble(m.getAmout()));
                    taxInvoice.setSubTotal(m.getPrice());
//                    if (!m.getPrice().equals("")){
//                        bill_taxInvoice.setSubTotal(m.getPrice());
//                        Double d = Double.parseDouble(m.getPrice());
//                        Double total = d * 1.01;
//                    }
                    String vat = taxInvoice.getTaxRatio();

                    // Double dSub = Double.parseDouble(lstDB.get(lstDB.size() - 1).getPrice());
                    //Double dVat = dSub * Double.parseDouble(vat) / 100;
                    //Double dTotal = dSub + dVat;


                    BigDecimal a = new BigDecimal(taxInvoice.getAmount());
                    BigDecimal b = new BigDecimal(m.getPrice());
                    BigDecimal c = new BigDecimal(tmp.get(0).getTerm());
                    BigDecimal dSub = a.multiply(b).multiply(c);
                    dSub = dSub.setScale(2, RoundingMode.CEILING);
                    BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
                    dVat = dVat.setScale(2, RoundingMode.CEILING);
                    BigDecimal dTotal = dSub.add(dVat);
                    m.setSubTotal(dSub + "");
                    m.setTotal(dTotal + "");

//                    txtVAT.setText(formatNumber(Math.round(dVat)) + " VNĐ");
//                    txtSubTotal.setText(formatNumber(Math.round(dSub)) + " VNĐ");
//                    txtTotal.setText(formatNumber(Math.round(dTotal)) + " VNĐ");

                    txtVAT.setText(formatNumber(dVat.longValue()) + " VNĐ");
                    txtSubTotal.setText(formatNumber(dSub.longValue()) + " VNĐ");
                    txtTotal.setText(formatNumber(dTotal.longValue()) + " VNĐ");

                    txtTuDen.setText(m.getStartDate() + " - " + m.getEndDate());

                    btnInHD.setText("IN HÓA ĐƠN (đã điều chỉnh t.ttin)");
                } else {
                    txtVAT.setText(formatNumber(Long.parseLong(taxInvoice.getVAT().substring(0, taxInvoice.getVAT().indexOf(".")))) + " VNĐ");
                    if (taxInvoice.getTotal().indexOf(".") != -1) {
                        txtTotal.setText(formatNumber(Long.parseLong(taxInvoice.getTotal().substring(0, taxInvoice.getTotal().indexOf(".")))) + " VNĐ");
                    } else {
                        txtTotal.setText(formatNumber(Long.parseLong(taxInvoice.getTotal())) + " VNĐ");
                    }

                    txtSubTotal.setText(formatNumber(Long.parseLong(taxInvoice.getSubTotal().substring(0, taxInvoice.getSubTotal().indexOf(".")))) + " VNĐ");
                    if (tmp.size() > 0) {
                        txtTuDen.setText(tmp.get(0).TuNgay + " - " + tmp.get(0).DenNgay);
                    }
                }

                txtMaKH.setText(taxInvoice.getCustomerCode());
                txtTenKH.setText(taxInvoice.getCustomerName());
                txtDiaChi.setText(taxInvoice.getTaxInvoiceAddress());
                txtTinhTrangThu.setText(taxInvoice.isThuOffline() == 1 ? "Đã thu offline" : taxInvoice.isThuOffline() == 2 ? "Đã thu online" : "Chưa thu");
//                txtKy.setText(taxInvoice.getMonth() + "/" + taxInvoice.getYear());
                txtSoNhanKhau.setText(taxInvoice.getAmount() + "");

                if (taxInvoice.isThuOffline() > 0) {
                    btnThuOff.setEnabled(false);
                    btnThuTien.setEnabled(false);
                }
                if (taxInvoice.isThuOffline() == 2) {
                    btnThuOff.setEnabled(false);
                }
//                CCISDataService apiService =
//                        RetrofitInstance.getRetrofitInstance(getActivity()).create(CCISDataService.class);
//
//                Call<List<Bill_TaxInvoiceDetail>> call = apiService.getBill_TaxInvoiceDetail(taxInvoice.getTaxInvoiceId());
//                call.enqueue(new CustomCallBack<List<Bill_TaxInvoiceDetail>>(getActivity()) {
//                    @Override
//                    public void onResponse(Call<List<Bill_TaxInvoiceDetail>> call, Response<List<Bill_TaxInvoiceDetail>> response) {
//                        lstDetail = response.body();
//                        Log.d(TAG, "Number lstDetail received: " + lstDetail.get(0).toString());
//                        this.mProgressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Bill_TaxInvoiceDetail>> call, Throwable t) {
//                        // Log error here since request failed
//                        Log.e(TAG, t.toString());
//                        if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
//                            Toast.makeText(getActivity(), "Không có dữ liệu chi tiết thu tiền. Đề nghị kiểm tra lại !", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(getActivity(), "Gặp lỗi trong quá trình lấy dữ liệu !", Toast.LENGTH_LONG).show();
//                        }
//                        this.mProgressDialog.dismiss();
//                    }
//                });
            } catch (Exception e) {

            }

        }

        return rootView;
    }

    @OnClick(R.id.btnThuOff)
    public void btnThuOff() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.logo);

        builder.setMessage("Anh/Chị xác nhận thu tiền khách hàng " + taxInvoice.getCustomerName() + ". Kiểm tra lại kỹ thông tin trước khi xác nhận.");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Bill_TaxInvoiceModel details = new Select().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).executeSingle();
                if (details != null) {
                    details.setThuOffline(1);
                    details.save();
                    Log.e(TAG + " update ", "1");
                } else {
                    Bill_TaxInvoiceModel c = new Bill_TaxInvoiceModel(taxInvoice.getTaxCode(), taxInvoice.getCustomerCode(),
                            taxInvoice.getBankName(), taxInvoice.getMonth(), taxInvoice.getSerialNumber(), taxInvoice.getYear(), taxInvoice.getCustomerId(), taxInvoice.getDepartmentId(),
                            taxInvoice.getTaxInvoiceAddress(), taxInvoice.getTaxInvoiceId(), taxInvoice.getIdDevice(), taxInvoice.getContractId(), taxInvoice.getFigureBookId(), taxInvoice.getSerialCode(),
                            taxInvoice.getCustomerName(), taxInvoice.getCustomerCode_Pay(), taxInvoice.getSubTotal(), taxInvoice.getAddress_Pay(), taxInvoice.getBankAccount(), taxInvoice.getVAT(),
                            taxInvoice.getTaxRatio(), taxInvoice.getCustomerId_Pay(), taxInvoice.getBillType(), taxInvoice.getCustomerName_Pay(), taxInvoice.getTotal(), taxInvoice.isChecked(), 1, taxInvoice.getAmount(), taxInvoice.getServiceTypeId(), taxInvoice.getServiceName(),
                            taxInvoice.getINDEX_THU(), taxInvoice.getKIEU(), taxInvoice.getPriceId());
                    c.save();
                    Log.e(TAG + " insert ", "2");
                }
                txtTinhTrangThu.setText("Đã thu offline");
                btnThuOff.setEnabled(false);
                btnThuTien.setEnabled(false);
                Toasty.success(getActivity(), "Thu tiền offline khách hàng " + txtTenKH.getText() + " thành công. Đề nghị in biên nhận !", Toasty.LENGTH_LONG, true).show();
                kieu = 0;
                btnInHD();
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

    @OnClick(R.id.btnThuTien)
    public void btnThuTien() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.logo);

        builder.setMessage("Anh/Chị xác nhận thu tiền khách hàng " + taxInvoice.getCustomerName() + ". Kiểm tra lại kỹ thông tin trước khi đồng ý.");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
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

                            Bill_TaxInvoiceModel details = new Select().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).executeSingle();
                            if (details != null) {
                                details.setThuOffline(2);
                                details.save();
                                Log.e(TAG + " update ", "1");
                            }

                            txtTinhTrangThu.setText("Đã thu online");
                            btnThuOff.setEnabled(false);
                            btnThuTien.setEnabled(false);
                            Toasty.success(getActivity(), "Đã thu tiền và đẩy dữ liệu khách hàng " + txtTenKH.getText() + " lên server thành công. Đề nghị in biên nhận !", Toasty.LENGTH_LONG, true).show();
                            kieu = 1;
                            btnInHD();

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
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @OnClick(R.id.btnInHD)
    public void btnInHD() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothPrinterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TAX", taxInvoice);
        bundle.putInt("KIEU", kieu);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    @OnClick(R.id.btnGuiSMS)
//    public void btnGuiSMS() {
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("sms_body", "Thông báo tiền điện ");
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        startActivity(Intent.createChooser(sendIntent, "SMS:"));
//    }

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
