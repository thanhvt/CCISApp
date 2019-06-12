package com.es.printer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.activeandroid.query.Select;
import com.es.ccisapp.R;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceDetail;
import com.es.model.Bill_TaxInvoiceDetail_DB;
import com.es.model.Concus_Customer_DB;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.SalesModel;
import com.es.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * This class is responsible to generate a static sales receipt and to print that receipt
 */
public class PrintReceipt {
    //    public static Bill_TaxInvoice bill_taxInvoice;
//    public PrintReceipt(Bill_TaxInvoice bill_taxInvoice){
//        this.bill_taxInvoice = bill_taxInvoice;
//    }
    static List<Bill_TaxInvoiceDetail> lstDetail;
    public static final String TAG = PrintReceipt.class.getName();

    public static boolean printTest(Context context) {
        Log.e("PrintReceipt", "Test");
        if (BluetoothPrinterActivity.BLUETOOTH_PRINTER.IsNoConnection()) {
            return false;
        }

        double totalBill = 0.00, netBill = 0.00, totalVat = 0.00;

        //LF = Line feed
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.Begin();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Company Name");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);
        Calendar cal = Calendar.getInstance();
        String time = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

        //BT_Write() method will initiate the printer to start printing.
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Công ty Điện Lực " + "Hoàn Kiếm" +
                "\nOrder No: " + "1245784256454" +
                "\nBill No: " + "554741254854" +
                "\nTrn. Date:" + time +
                "\nNhân viên:" + "Administrator");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//LEFT
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font

        //static sales record are generated
        SalesModel.generatedMoneyReceipt();

        for (int i = 0; i < StaticValue.arrayListSalesModel.size(); i++) {
            SalesModel salesModel = StaticValue.arrayListSalesModel.get(i);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(salesModel.getProductShortName());
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(" " + salesModel.getSalesAmount() + "x" + salesModel.getUnitSalesCost() +
                    "=" + Utils.doubleFormatter(salesModel.getSalesAmount() * salesModel.getUnitSalesCost()) + "" + StaticValue.CURRENCY);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

            totalBill = totalBill + (salesModel.getUnitSalesCost() * salesModel.getSalesAmount());
        }

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));


        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//RIGHT
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font

        totalVat = Double.parseDouble(Utils.doubleFormatter(totalBill * (StaticValue.VAT / 100)));
        netBill = totalBill + totalVat;

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Total Bill:" + Utils.doubleFormatter(totalBill) + "" + StaticValue.CURRENCY);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(Double.toString(StaticValue.VAT) + "% VAT:" + Utils.doubleFormatter(totalVat) + "" +
                StaticValue.CURRENCY);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));


        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//Right
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x9);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Net Bill:" + Utils.doubleFormatter(netBill) + "" + StaticValue.CURRENCY);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//left
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("VAT Reg. No:" + StaticValue.VAT_REGISTRATION_NUMBER);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//left
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(StaticValue.BRANCH_ADDRESS);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//Center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\n\nThank You\nPOWERED By ....");


        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        return true;
    }

    public static boolean printBillFromOrder(final Context context, final Bill_TaxInvoice bill_taxInvoice, int kieu) {

        lstDetail = new ArrayList<>();

        List<Bill_TaxInvoiceDetail_DB> lstDetailDB = new ArrayList<>();
        try {
            lstDetailDB = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", bill_taxInvoice.getTaxInvoiceId()).execute();
        } catch (Exception e) {

        }
        if (lstDetailDB.size() == 0) {

//            CCISDataService apiService =
//                    RetrofitInstance.getRetrofitInstance(context).create(CCISDataService.class);
//
//            Call<List<Bill_TaxInvoiceDetail>> call = apiService.getBill_TaxInvoiceDetail(bill_taxInvoice.getTaxInvoiceId());
//
//            call.enqueue(new CustomCallBack<List<Bill_TaxInvoiceDetail>>(context) {
//                @Override
//                public void onResponse(Call<List<Bill_TaxInvoiceDetail>> call, Response<List<Bill_TaxInvoiceDetail>> response) {
//                    lstDetail = response.body();
//                    Log.d(TAG, "Number of lstDetail received: " + lstDetail.get(0).toString());
//                    this.mProgressDialog.dismiss();
//                    printResult(context, bill_taxInvoice, lstDetail);
//                }
//
//                @Override
//                public void onFailure(Call<List<Bill_TaxInvoiceDetail>> call, Throwable t) {
//                    // Log error here since request failed
//                    Log.e(TAG, t.toString());
//                    if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
//                        Toast.makeText(context, "Không có dữ liệu chi tiết thu tiền. Đề nghị kiểm tra lại !", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "Gặp lỗi trong quá trình lấy dữ liệu !", Toast.LENGTH_LONG).show();
//                    }
//                    this.mProgressDialog.dismiss();
//                }
//            });
        } else {
            if (kieu == 3) {
                printKHMoi(context, bill_taxInvoice);
            } else {
                printResult(context, bill_taxInvoice, lstDetailDB);
            }

        }
        if (kieu == 1) {
//            List<Bill_TaxInvoiceModel> info = new Delete().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", bill_taxInvoice.getTaxInvoiceId()).execute();
//            info.get(0).setThuOffline(2);
//            info.get(0).save();
//            new Delete().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", bill_taxInvoice.getTaxInvoiceId()).execute();
        }

        return true;
    }

    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    public static boolean printKHMoi(Context context, Bill_TaxInvoice bill_taxInvoice) {
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String TEN_CTY = sharedPrefs.getString("TEN_CTY", "Chưa cấu hình");
            String TEN_CHINHANH = sharedPrefs.getString("TEN_CHINHANH", "Chưa cấu hình");
            String TEN_NVTHU = sharedPrefs.getString("TEN_NVTHU", "Chưa cấu hình");
            String SDT = sharedPrefs.getString("SDT", "Chưa cấu hình");
            String SDT_CSKH = sharedPrefs.getString("SDT_CSKH", "Chưa cấu hình");

            TEN_CTY = Utils.removeAccent(TEN_CTY);
            TEN_CHINHANH = Utils.removeAccent(TEN_CHINHANH);
            TEN_NVTHU = Utils.removeAccent(TEN_NVTHU);
            String term = "";
            String strMST = "";
            Mobile_Adjust_DB mThayDoi = new Mobile_Adjust_DB();
            List<Mobile_Adjust_DB> lstDB = new Select().all().from(Mobile_Adjust_DB.class).where("CustomerNew = ?", bill_taxInvoice.getCustomerId()).execute();
            List<Concus_Customer_DB> lstKH = new Select().all().from(Concus_Customer_DB.class).where("CustomerCode = ?", bill_taxInvoice.getCustomerCode()).execute();
            if (lstKH != null && lstKH.size() > 0) {
                strMST = lstKH.get(0).TaxCode;

            }
            if (lstDB != null && lstDB.size() > 0) {
                mThayDoi = lstDB.get(lstDB.size() - 1); // lstDB.get(0);

                strMST = mThayDoi.getTaxCode();

                bill_taxInvoice.setCustomerName(mThayDoi.getCustomerName());
                bill_taxInvoice.setAddress_Pay(mThayDoi.getCustomerAdd());
                bill_taxInvoice.setAmount(Double.parseDouble(mThayDoi.getAmout()));

                int mTerm = Utils.CalculateTotalPartialMonth(Utils.parseDate(mThayDoi.getEndDate()), Utils.parseDate(mThayDoi.getStartDate()));
                term = mTerm + "";

                BigDecimal vat = new BigDecimal(bill_taxInvoice.getTaxRatio());
                BigDecimal soLuong = new BigDecimal(mThayDoi.getAmout());
                BigDecimal donGia = new BigDecimal(mThayDoi.getPrice());
                BigDecimal soThang = new BigDecimal(term);

                BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
                ;
                dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
                BigDecimal tmpBig = vat.divide(new BigDecimal((100)));
                tmpBig = tmpBig.add(new BigDecimal(1));
                tmpBig = dTotal.divide(tmpBig, 0, RoundingMode.HALF_UP);
                BigDecimal dSub = tmpBig;

//                tmpBig = tmpBig.divide(new BigDecimal((1000)));
//                tmpBig = tmpBig.setScale(0, RoundingMode.HALF_UP);
//                tmpBig = tmpBig.multiply(new BigDecimal((1000)));
//
//                BigDecimal cotAn = tmpBig;
//                BigDecimal dTotal = cotAn.multiply(soLuong).multiply(soThang);
//
//                BigDecimal tmpBig2 = vat.divide(new BigDecimal((100)));
//                tmpBig2 = tmpBig2.add(new BigDecimal(1));
//                tmpBig2 = dTotal.divide(tmpBig2, 0, RoundingMode.HALF_UP);
//
//                BigDecimal dSub = tmpBig2;
                BigDecimal dVat = dTotal.subtract(dSub);

//                String vat = bill_taxInvoice.getTaxRatio();
//                BigDecimal a = new BigDecimal(mThayDoi.getAmout());
//                BigDecimal b = new BigDecimal(mThayDoi.getPrice());
//                BigDecimal c = new BigDecimal(term);
//                BigDecimal dSub = a.multiply(b).multiply(c);
//                dSub = dSub.setScale(2, RoundingMode.CEILING);
//                BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
//                dVat = dVat.setScale(2, RoundingMode.CEILING);
//                BigDecimal dTotal = dSub.add(dVat);
//                dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);


                bill_taxInvoice.setSubTotal(dSub + "");
                bill_taxInvoice.setTotal(dTotal + "");
                bill_taxInvoice.setVAT(dVat + "");

            }
            Log.e(TAG, bill_taxInvoice.toString());
            if (BluetoothPrinterActivity.BLUETOOTH_PRINTER.IsNoConnection()) {
                return false;
            }

            double totalBill = 0.00, netBill = 0.00, totalVat = 0.00;

            //LF = Line feed

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.Begin();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(TEN_CTY);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("CHI NHANH: " + TEN_CHINHANH);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);
            Calendar cal = Calendar.getInstance();
            String time = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("BIEN NHAN THU TIEN DICH VU VSMT");

//            String strThangHD = "Thang PH: " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
            String strThangHD = "Thang PH: " + bill_taxInvoice.getMonth() + "/" + bill_taxInvoice.getYear();
            String strLoai = "Loai: " + (bill_taxInvoice.getServiceTypeId() == 1 ? "SH" : bill_taxInvoice.getServiceTypeId() == 2 ? "KD" : "HD") + " " + strThangHD;
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(strLoai);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//CENTER
            //BT_Write() method will initiate the printer to start printing.
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(
                    "\nID HD: --- " +
                            "\nTen KH: " + Utils.removeAccent(bill_taxInvoice.getCustomerName()) +
                            "\nDia chi: " + Utils.removeAccent(bill_taxInvoice.getAddress_Pay()) +
                            "\nMa KH: --- " +
                            "\nMST: " + strMST +
                            "\nTu: " + (mThayDoi.getStartDate() != null ? mThayDoi.getStartDate() : "") +
                            "\nDen: " + (mThayDoi.getEndDate() != null ? mThayDoi.getEndDate() : ""));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//RIGHT
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Dv|SL |S.th|Don gia  |Thanh tien");
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("--|---|----|---------|----------");
//            for (Mobile_Adjust_DB de : mThayDoi) {


            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(bill_taxInvoice.getKIEU() + "|" +
                    inThat(3, (mThayDoi.getAmout() + "").length(), mThayDoi.getAmout() + "") + "|" +
                    inThat(4, (term + "").length(), term + "") + "|" +
                    inThat(9, (mThayDoi.getPrice() + "").length(), mThayDoi.getPrice() + "") + "|" +
                    inThat(10, (mThayDoi.getSubTotal() + "").length(), mThayDoi.getSubTotal() + ""));
//            }

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            String strXFix = "Thue GTGT " + bill_taxInvoice.getTaxRatio() + "%";
            String strVat = mThayDoi.getTax();
            int so0 = 32 - strXFix.length() - strVat.length();
            for (int i = 0; i < so0; i++) {
                strXFix += " ";
            }
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(strXFix + strVat);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            strXFix = "Tong tien";
            String strTienFormat = Utils.formatValue(mThayDoi.getTotal());
            Log.e(TAG, "format " + strTienFormat);
            strVat = mThayDoi.getTotal();
            so0 = 32 - strXFix.length() - strTienFormat.length();
            for (int i = 0; i < so0; i++) {
                strXFix += " ";
            }
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(strXFix + strTienFormat);
            Double d = Double.parseDouble(strVat);
            String tienChu = Utils.numberToString(d);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(tienChu);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//1: Center, 0: LEFT
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("NV thu: " + Utils.removeAccent(TEN_NVTHU));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("SDT: " + Utils.removeAccent(SDT));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("SDT CSKH: " + Utils.removeAccent(SDT_CSKH));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Da thu tien");

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Thong tin tra cuu truy cap tai: urenco.com.vn");
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        } catch (Exception e) {
            Log.e("Err in HD ", e.getMessage());
        }

        return true;
    }

    public static boolean printResult(Context context, Bill_TaxInvoice bill_taxInvoice, List<Bill_TaxInvoiceDetail_DB> lstDetail) {
        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String TEN_CTY = sharedPrefs.getString("TEN_CTY", "Chưa cấu hình");
            String TEN_CHINHANH = sharedPrefs.getString("TEN_CHINHANH", "Chưa cấu hình");
            String TEN_NVTHU = sharedPrefs.getString("TEN_NVTHU", "Chưa cấu hình");
            String SDT = sharedPrefs.getString("SDT", "Chưa cấu hình");
            String SDT_CSKH = sharedPrefs.getString("SDT_CSKH", "Chưa cấu hình");

            TEN_CTY = Utils.removeAccent(TEN_CTY);
            TEN_CHINHANH = Utils.removeAccent(TEN_CHINHANH);
            TEN_NVTHU = Utils.removeAccent(TEN_NVTHU);

            String strMST = "";

            Mobile_Adjust_DB mThayDoi = new Mobile_Adjust_DB();
            List<Mobile_Adjust_DB> lstDB = new Select().all().from(Mobile_Adjust_DB.class).where("TYPE != '3' and CustomerID = ?", bill_taxInvoice.getCustomerId()).execute();
            List<Concus_Customer_DB> lstKH = new Select().all().from(Concus_Customer_DB.class).where("CustomerCode = ?", bill_taxInvoice.getCustomerCode()).execute();
            if (lstKH != null && lstKH.size() > 0) {
                strMST = lstKH.get(0).TaxCode;
            }

            if (lstDB != null && lstDB.size() > 0) {
                mThayDoi = lstDB.get(lstDB.size() - 1);

                strMST = mThayDoi.getTaxCode();

                bill_taxInvoice.setCustomerName(mThayDoi.getCustomerName());
                bill_taxInvoice.setAddress_Pay(mThayDoi.getCustomerAdd());
                bill_taxInvoice.setAmount(Double.parseDouble(mThayDoi.getAmout()));

                BigDecimal vat = new BigDecimal(bill_taxInvoice.getTaxRatio());
                BigDecimal soLuong = new BigDecimal(mThayDoi.getAmout());
                BigDecimal donGia = new BigDecimal(mThayDoi.getPrice());
                BigDecimal soThang = new BigDecimal(lstDetail.get(0).getTerm());

                BigDecimal dTotal = donGia.multiply(soLuong).multiply(soThang);
                ;
                dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);
                BigDecimal tmpBig = vat.divide(new BigDecimal((100)));
                tmpBig = tmpBig.add(new BigDecimal(1));
                tmpBig = dTotal.divide(tmpBig, 0, RoundingMode.HALF_UP);
                BigDecimal dSub = tmpBig;

//                tmpBig = tmpBig.divide(new BigDecimal((1000)));
//                tmpBig = tmpBig.setScale(0, RoundingMode.HALF_UP);
//                tmpBig = tmpBig.multiply(new BigDecimal((1000)));
//
//                BigDecimal cotAn = tmpBig;
//                BigDecimal dTotal = cotAn.multiply(soLuong).multiply(soThang);
//
//                BigDecimal tmpBig2 = vat.divide(new BigDecimal((100)));
//                tmpBig2 = tmpBig2.add(new BigDecimal(1));
//                tmpBig2 = dTotal.divide(tmpBig2, 0, RoundingMode.HALF_UP);
//
//                BigDecimal dSub = tmpBig2;
                BigDecimal dVat = dTotal.subtract(dSub);


//                String vat = bill_taxInvoice.getTaxRatio();
//                BigDecimal a = new BigDecimal(mThayDoi.getAmout());
//                BigDecimal b = new BigDecimal(mThayDoi.getPrice());
//                BigDecimal c = new BigDecimal(lstDetail.get(0).getTerm());
//                BigDecimal dSub = a.multiply(b).multiply(c);
//                dSub = dSub.setScale(2, RoundingMode.CEILING);
//                BigDecimal dVat = dSub.multiply(new BigDecimal(vat)).divide(new BigDecimal(100));
//                dVat = dVat.setScale(2, RoundingMode.CEILING);
//                BigDecimal dTotal = dSub.add(dVat);
//                dTotal = dTotal.setScale(0, RoundingMode.HALF_UP);


                bill_taxInvoice.setSubTotal(dSub + "");
                bill_taxInvoice.setTotal(dTotal + "");
                bill_taxInvoice.setVAT(dVat + "");
                for (Bill_TaxInvoiceDetail_DB item : lstDetail) {
                    item.setAmount(Double.parseDouble(mThayDoi.getAmout()));
                    item.setTotal(dSub.doubleValue());
                    item.setPrice(donGia.doubleValue());
                }

            } else {
//                List<Bill_TaxInvoiceDetail_DB> tmp = new Select().all().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", taxInvoice.getTaxInvoiceId()).execute();
                mThayDoi.setStartDate(lstDetail.get(0).TuNgay);
                mThayDoi.setEndDate(lstDetail.get(0).DenNgay);
            }
            Log.e(TAG, bill_taxInvoice.toString());
            if (BluetoothPrinterActivity.BLUETOOTH_PRINTER.IsNoConnection()) {
                return false;
            }

            double totalBill = 0.00, netBill = 0.00, totalVat = 0.00;

            //LF = Line feed

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.Begin();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(TEN_CTY);
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(URLEncoder.encode(TEN_CTY, "UTF-8"));
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(EncodingUtils.getBytes(TEN_CTY, "ISO-8859-1"));
            // ZEMMMMMMMMMMMMMMMMMMMm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("CHI NHANH: " + TEN_CHINHANH);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);
            Calendar cal = Calendar.getInstance();
            String time = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("BIEN NHAN THU TIEN DICH VU VSMT");

//            String strThangHD = "Thang PH: " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
            String strThangHD = "Thang PH: " + bill_taxInvoice.getMonth() + "/" + bill_taxInvoice.getYear();
            String strLoai = "Loai: " + (bill_taxInvoice.getServiceTypeId() == 1 ? "SH" : bill_taxInvoice.getServiceTypeId() == 2 ? "KD" : "HD") + " " + strThangHD;
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//CENTER
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //30 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(strLoai);

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//CENTER
            //BT_Write() method will initiate the printer to start printing.
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(
                    "\nID HD: " + bill_taxInvoice.getContractId() +
                            "\nTen KH: " + Utils.removeAccent(bill_taxInvoice.getCustomerName()) +
                            "\nDia chi: " + Utils.removeAccent(bill_taxInvoice.getAddress_Pay()) +
                            "\nMa KH: " + bill_taxInvoice.getCustomerCode() +
                            "\nMST: " + strMST +
                            "\nTu: " + (mThayDoi.getStartDate() != null ? mThayDoi.getStartDate() : "") +
                            "\nDen: " + (mThayDoi.getEndDate() != null ? mThayDoi.getEndDate() : ""));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//RIGHT
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Dv|SL |S.th|Don gia  |Thanh tien");
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("--|---|----|---------|----------");
            for (Bill_TaxInvoiceDetail_DB de : lstDetail) {
                BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
                BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(bill_taxInvoice.getKIEU() + "|" +
                        inThat(3, (de.getAmount() + "").length(), de.getAmount() + "") + "|" +
                        inThat(4, (de.getTerm() + "").length(), de.getTerm() + "") + "|" +
                        inThat(9, (de.getPrice() + "").length(), de.getPrice() + "") + "|" +
                        inThat(10, (de.getTotal() + "").length(), de.getTotal() + ""));
            }

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            String strXFix = "Thue GTGT " + bill_taxInvoice.getTaxRatio() + "%";
            String strVat = bill_taxInvoice.getVAT();
            int so0 = 32 - strXFix.length() - strVat.length();
            for (int i = 0; i < so0; i++) {
                strXFix += " ";
            }
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(strXFix + strVat);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            strXFix = "Tong tien";
            String strTienFormat = Utils.formatValue(bill_taxInvoice.getTotal());
            Log.e(TAG, "format " + strTienFormat);
            strVat = bill_taxInvoice.getTotal();
            so0 = 32 - strXFix.length() - strTienFormat.length();
            for (int i = 0; i < so0; i++) {
                strXFix += " ";
            }
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(strXFix + strTienFormat);
            Double d = Double.parseDouble(strVat);
            String tienChu = Utils.numberToString(d);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(tienChu);
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Ho|3 |3    |180,180 |1,540,540");

            ////////////////////////////// TAM ZEM LAI //////////////////////////////////////////////
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//RIGHT
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
//
//        totalVat = Double.parseDouble(Utils.doubleFormatter(totalBill * (StaticValue.VAT / 100)));
//        netBill = totalBill + totalVat;
//
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("So tien:" + formatNumber(Long.parseLong(bill_taxInvoice.getSubTotal()
//                .substring(0, bill_taxInvoice.getSubTotal().indexOf(".")))) + " (VNĐ)");
//
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Thue GTGT " + Double.toString(StaticValue.VAT) + "%: " + Utils.doubleFormatter(totalVat) + "" +
//                StaticValue.CURRENCY);
//
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
//
//
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//Right
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x9);
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Tong tien:" + formatNumber(Long.parseLong(bill_taxInvoice.getTotal()
//                .substring(0, bill_taxInvoice.getTotal().indexOf(".")))) + " (VND)");

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//1: Center, 0: LEFT
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("NV thu: " + Utils.removeAccent(TEN_NVTHU));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("SDT: " + Utils.removeAccent(SDT));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("SDT CSKH: " + Utils.removeAccent(SDT_CSKH));

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Da thu tien");

            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Thong tin tra cuu truy cap tai: urenco.com.vn");

//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
////        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
////        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
////        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//Center
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\nXin cam on");
//
//
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        } catch (Exception e) {
            Log.e("Err in HD ", e.getMessage());
        }

        return true;
    }

    public static String inThat(int doDaiChoPhep, int doDaiThucTe, String strX) {
        if (doDaiThucTe < doDaiChoPhep) {
            int kc = doDaiChoPhep - doDaiThucTe;
            for (int i = 0; i < kc; i++) {
                strX += " ";
            }
        }
        return strX;
    }

}
