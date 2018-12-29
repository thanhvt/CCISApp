package com.es.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Bill_TaxInvoiceModel")
public class Bill_TaxInvoiceModel extends Model implements Serializable {
    @Column(name = "TaxCode")
    private String TaxCode;

    @Column(name = "CustomerCode")
    private String CustomerCode;

    @Column(name = "BankName")
    private String BankName;

    @Column(name = "Month")
    private String Month;

    @Column(name = "SerialNumber")
    private String SerialNumber;

    @Column(name = "Year")
    private String Year;

    @Column(name = "CustomerId")
    private String CustomerId;

    @Column(name = "DepartmentId")
    private String DepartmentId;

    @Column(name = "TaxInvoiceAddress")
    private String TaxInvoiceAddress;

    @Column(name = "TaxInvoiceId")
    private int TaxInvoiceId;

    @Column(name = "IdDevice")
    private int IdDevice;

    @Column(name = "ContractId")
    private String ContractId;

    @Column(name = "FigureBookId")
    private int FigureBookId;

    @Column(name = "SerialCode")
    private String SerialCode;

    @Column(name = "CustomerName")
    private String CustomerName;

    @Column(name = "CustomerCode_Pay")
    private String CustomerCode_Pay;

    @Column(name = "SubTotal")
    private String SubTotal;

    @Column(name = "Address_Pay")
    private String Address_Pay;

    @Column(name = "BankAccount")
    private String BankAccount;

    @Column(name = "VAT")
    private String VAT;

    @Column(name = "TaxRatio")
    private String TaxRatio;

    @Column(name = "CustomerId_Pay")
    private String CustomerId_Pay;

    @Column(name = "BillType")
    private String BillType;

    @Column(name = "CustomerName_Pay")
    private String CustomerName_Pay;

    @Column(name = "Total")
    private String Total;

    @Column(name = "IsChecked")
    private boolean IsChecked;

    public Bill_TaxInvoiceModel() {
    }

    public Bill_TaxInvoiceModel(String taxCode, String customerCode, String bankName, String month, String serialNumber, String year, String customerId, String departmentId, String taxInvoiceAddress, int taxInvoiceId, int idDevice, String contractId, int figureBookId, String serialCode, String customerName, String customerCode_Pay, String subTotal, String address_Pay, String bankAccount, String VAT, String taxRatio, String customerId_Pay, String billType, String customerName_Pay, String total, boolean isChecked) {
        TaxCode = taxCode;
        CustomerCode = customerCode;
        BankName = bankName;
        Month = month;
        SerialNumber = serialNumber;
        Year = year;
        CustomerId = customerId;
        DepartmentId = departmentId;
        TaxInvoiceAddress = taxInvoiceAddress;
        TaxInvoiceId = taxInvoiceId;
        IdDevice = idDevice;
        ContractId = contractId;
        FigureBookId = figureBookId;
        SerialCode = serialCode;
        CustomerName = customerName;
        CustomerCode_Pay = customerCode_Pay;
        SubTotal = subTotal;
        Address_Pay = address_Pay;
        BankAccount = bankAccount;
        this.VAT = VAT;
        TaxRatio = taxRatio;
        CustomerId_Pay = customerId_Pay;
        BillType = billType;
        CustomerName_Pay = customerName_Pay;
        Total = total;
        IsChecked = isChecked;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }

    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(String TaxCode) {
        this.TaxCode = TaxCode;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String CustomerCode) {
        this.CustomerCode = CustomerCode;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String SerialNumber) {
        this.SerialNumber = SerialNumber;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String DepartmentId) {
        this.DepartmentId = DepartmentId;
    }

    public String getTaxInvoiceAddress() {
        return TaxInvoiceAddress;
    }

    public void setTaxInvoiceAddress(String TaxInvoiceAddress) {
        this.TaxInvoiceAddress = TaxInvoiceAddress;
    }

    public int getTaxInvoiceId() {
        return TaxInvoiceId;
    }

    public void setTaxInvoiceId(int TaxInvoiceId) {
        this.TaxInvoiceId = TaxInvoiceId;
    }

    public int getIdDevice() {
        return IdDevice;
    }

    public void setIdDevice(int IdDevice) {
        this.IdDevice = IdDevice;
    }

    public String getContractId() {
        return ContractId;
    }

    public void setContractId(String ContractId) {
        this.ContractId = ContractId;
    }

    public int getFigureBookId() {
        return FigureBookId;
    }

    public void setFigureBookId(int FigureBookId) {
        this.FigureBookId = FigureBookId;
    }

    public String getSerialCode() {
        return SerialCode;
    }

    public void setSerialCode(String SerialCode) {
        this.SerialCode = SerialCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerCode_Pay() {
        return CustomerCode_Pay;
    }

    public void setCustomerCode_Pay(String CustomerCode_Pay) {
        this.CustomerCode_Pay = CustomerCode_Pay;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String SubTotal) {
        this.SubTotal = SubTotal;
    }

    public String getAddress_Pay() {
        return Address_Pay;
    }

    public void setAddress_Pay(String Address_Pay) {
        this.Address_Pay = Address_Pay;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String BankAccount) {
        this.BankAccount = BankAccount;
    }

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public String getTaxRatio() {
        return TaxRatio;
    }

    public void setTaxRatio(String TaxRatio) {
        this.TaxRatio = TaxRatio;
    }

    public String getCustomerId_Pay() {
        return CustomerId_Pay;
    }

    public void setCustomerId_Pay(String CustomerId_Pay) {
        this.CustomerId_Pay = CustomerId_Pay;
    }

    public String getBillType() {
        return BillType;
    }

    public void setBillType(String BillType) {
        this.BillType = BillType;
    }

    public String getCustomerName_Pay() {
        return CustomerName_Pay;
    }

    public void setCustomerName_Pay(String CustomerName_Pay) {
        this.CustomerName_Pay = CustomerName_Pay;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    @Override
    public String toString() {
        return "ClassPojo [TaxCode = " + TaxCode + ", CustomerCode = " + CustomerCode + ", BankName = " + BankName + ", Month = " + Month + ", SerialNumber = " + SerialNumber + ", Year = " + Year + ", CustomerId = " + CustomerId + ", DepartmentId = " + DepartmentId + ", TaxInvoiceAddress = " + TaxInvoiceAddress + ", TaxInvoiceId = " + TaxInvoiceId + ", IdDevice = " + IdDevice + ", ContractId = " + ContractId + ", FigureBookId = " + FigureBookId + ", SerialCode = " + SerialCode + ", CustomerName = " + CustomerName + ", CustomerCode_Pay = " + CustomerCode_Pay + ", SubTotal = " + SubTotal + ", Address_Pay = " + Address_Pay + ", BankAccount = " + BankAccount + ", VAT = " + VAT + ", TaxRatio = " + TaxRatio + ", CustomerId_Pay = " + CustomerId_Pay + ", BillType = " + BillType + ", CustomerName_Pay = " + CustomerName_Pay + ", Total = " + Total + "]";
    }
}
