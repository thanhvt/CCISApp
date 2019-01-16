package com.es.printer;

import android.content.Context;
import android.util.Log;

import com.activeandroid.query.Select;
import com.es.ccisapp.R;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.SalesModel;
import com.es.utils.Utils;

import java.util.Calendar;
import java.util.List;

import static com.es.utils.Utils.formatNumber;


/**
 * This class is responsible to generate a static sales receipt and to print that receipt
 */
public class PrintReceipt {
//    public static Bill_TaxInvoice bill_taxInvoice;
//    public PrintReceipt(Bill_TaxInvoice bill_taxInvoice){
//        this.bill_taxInvoice = bill_taxInvoice;
//    }

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

    public static boolean printBillFromOrder(Context context, Bill_TaxInvoice bill_taxInvoice) {

        List<Mobile_Adjust_DB> lstDB = new Select().all().from(Mobile_Adjust_DB.class).where("CustomerID = ?", bill_taxInvoice.getCustomerId()).execute();
        if (lstDB != null && lstDB.size() > 0) {
            Mobile_Adjust_DB m = lstDB.get(0);
            bill_taxInvoice.setCustomerName(m.getCustomerName());
            bill_taxInvoice.setAddress_Pay(m.getCustomerAdd());
//            if (!m.getPrice().equals("")){
//                bill_taxInvoice.setSubTotal(m.getPrice());
//                Double d = Double.parseDouble(m.getPrice());
//                Double total = d * 1.01;
//            }

        }
        Log.e("PrintReceipt", bill_taxInvoice.toString());
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
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("CONG TY URENCO");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);
        Calendar cal = Calendar.getInstance();
        String time = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

        //BT_Write() method will initiate the printer to start printing.
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("BIEN NHAN THU TIEN DICH VU VSMT" +
                "\nLoai: KD" +
                "\nIn HĐ: " + bill_taxInvoice.getContractId() +
                "\nTen KH: " + bill_taxInvoice.getCustomerName() +
                "\nDia chi: " + bill_taxInvoice.getAddress_Pay() +
                "\nMa KH: " + bill_taxInvoice.getCustomerCode() +
                "\nKy: " +
                "\nTu: " +
                "\nDen: ");
//                +
//                "\nNgay: " + time +
//                "\nNhan vien: " + "Administrator" +
//                "\nNoi dung: Thu PVS thang " + bill_taxInvoice.getMonth() + "/" + bill_taxInvoice.getYear());

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();

//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//LEFT
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
//
//        //static sales record are generated
//        SalesModel.generatedMoneyReceipt();
//
//        for (int i = 0; i < StaticValue.arrayListSalesModel.size(); i++) {
//            SalesModel salesModel = StaticValue.arrayListSalesModel.get(i);
//            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(salesModel.getProductShortName());
//            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//            BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(" " + salesModel.getSalesAmount() + "x" + salesModel.getUnitSalesCost() +
//                    "=" + Utils.doubleFormatter(salesModel.getSalesAmount() * salesModel.getUnitSalesCost()) + "" + StaticValue.CURRENCY);
//            BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//
//            totalBill = totalBill + (salesModel.getUnitSalesCost() * salesModel.getSalesAmount());
//        }
//
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));


        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//RIGHT
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);    //50 * 0.125mm
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font

        totalVat = Double.parseDouble(Utils.doubleFormatter(totalBill * (StaticValue.VAT / 100)));
        netBill = totalBill + totalVat;

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Số tiền:" + formatNumber(Long.parseLong(bill_taxInvoice.getSubTotal()
                .substring(0, bill_taxInvoice.getSubTotal().indexOf(".")))) + " (VNĐ)");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Thue GTGT " + Double.toString(StaticValue.VAT) + "%: " + Utils.doubleFormatter(totalVat) + "" +
                StaticValue.CURRENCY);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));


        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetLineSpacing((byte) 30);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 2);//Right
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x9);
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("Tong tien:" + formatNumber(Long.parseLong(bill_taxInvoice.getTotal()
                .substring(0, bill_taxInvoice.getTotal().indexOf(".")))) + " (VNĐ)");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetFontEnlarge((byte) 0x00);//normal font
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(context.getResources().getString(R.string.print_line));

//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//left
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("VAT Reg. No:" + StaticValue.VAT_REGISTRATION_NUMBER);

//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 0);//left
//        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write(StaticValue.BRANCH_ADDRESS);

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//Center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\n\nNV thu");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//Center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\n\nSDT");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//Center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\n\nSDT CSKH");

        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.SetAlignMode((byte) 1);//Center
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.BT_Write("\n\nXin cam on");


        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        BluetoothPrinterActivity.BLUETOOTH_PRINTER.LF();
        return true;
    }
}
