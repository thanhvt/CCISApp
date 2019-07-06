package com.es.model;

import java.io.Serializable;
import java.util.Date;

public class Mobile_Adjust_Informations implements Serializable {
    private boolean Status;

    private String Index;

    private String Type;

    private String Price;

    private String CustomerID;

    private String CustomerAdd;

    private String DepartmentId;

    private String EmployeeCode;

    private String CustomerName;

    private String $id;

    private String Amout;

    private String AdjustID;

    private String FigureBookId;

    private Date StartDate;

    private Date EndDate;

    private String SubTotal;

    private String Tax;

    private String Total;

    private String CustomerNew;

    private int PriceId;

    private String TaxCode;
    private String PhoneNumber;
    private String Email;
    private String GiaSauThue;

    private String PaymentMethodsCode;

    // ---------------------
    private int Month;

    private int Year;

//    public Nullable<int> FigureBookId { get; set; }
//    public Nullable<System.DateTime> StartDate { get; set; }
//    public Nullable<System.DateTime> EndDate { get; set; }
//    public Nullable<decimal> SubTotal { get; set; }
//    public Nullable<decimal> Tax { get; set; }
//    public Nullable<decimal> Total { get; set; }
//    public Nullable<int> CustomerNew { get; set; }


    public Mobile_Adjust_Informations(boolean status, String index, String type, String price, String customerID, String customerAdd, String departmentId,
                                      String employeeCode, String customerName, String $id, String amout, String adjustID, String figureBookId,
                                      Date startDate, Date endDate, String subTotal, String tax, String total, String customerNew, int priceId,
                                      String taxCode,
                                      String phoneNumber,
                                      String email,
                                      String GiaSauThue,
                                      String PaymentMethodsCode) {
        Status = status;
        Index = index;
        Type = type;
        Price = price;
        CustomerID = customerID;
        CustomerAdd = customerAdd;
        DepartmentId = departmentId;
        EmployeeCode = employeeCode;
        CustomerName = customerName;
        this.$id = $id;
        Amout = amout;
        AdjustID = adjustID;
        FigureBookId = figureBookId;
        StartDate = startDate;
        EndDate = endDate;
        SubTotal = subTotal;
        Tax = tax;
        Total = total;
        CustomerNew = customerNew;
        PriceId = priceId;
        TaxCode = taxCode;
        PhoneNumber = phoneNumber;
        Email = email;
        this.GiaSauThue = GiaSauThue;
        this.PaymentMethodsCode = PaymentMethodsCode;
    }

    public String getPaymentMethodsCode() {
        return PaymentMethodsCode;
    }

    public void setPaymentMethodsCode(String paymentMethodsCode) {
        PaymentMethodsCode = paymentMethodsCode;
    }

    public String getGiaSauThue() {
        return GiaSauThue;
    }

    public void setGiaSauThue(String giaSauThue) {
        GiaSauThue = giaSauThue;
    }

    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(String taxCode) {
        TaxCode = taxCode;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getPriceId() {
        return PriceId;
    }

    public void setPriceId(int priceId) {
        PriceId = priceId;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getFigureBookId() {
        return FigureBookId;
    }

    public void setFigureBookId(String figureBookId) {
        FigureBookId = figureBookId;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String subTotal) {
        SubTotal = subTotal;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getCustomerNew() {
        return CustomerNew;
    }

    public void setCustomerNew(String customerNew) {
        CustomerNew = customerNew;
    }

    public Mobile_Adjust_Informations() {

    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String Index) {
        this.Index = Index;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public String getCustomerAdd() {
        return CustomerAdd;
    }

    public void setCustomerAdd(String CustomerAdd) {
        this.CustomerAdd = CustomerAdd;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String DepartmentId) {
        this.DepartmentId = DepartmentId;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String EmployeeCode) {
        this.EmployeeCode = EmployeeCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getAmout() {
        return Amout;
    }

    public void setAmout(String Amout) {
        this.Amout = Amout;
    }

    public String getAdjustID() {
        return AdjustID;
    }

    public void setAdjustID(String AdjustID) {
        this.AdjustID = AdjustID;
    }

    @Override
    public String toString() {
        return "Mobile_Adjust_Informations{" +
                "Status=" + Status +
                ", Index='" + Index + '\'' +
                ", Type='" + Type + '\'' +
                ", Price='" + Price + '\'' +
                ", CustomerID='" + CustomerID + '\'' +
                ", CustomerAdd='" + CustomerAdd + '\'' +
                ", DepartmentId='" + DepartmentId + '\'' +
                ", EmployeeCode='" + EmployeeCode + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", $id='" + $id + '\'' +
                ", Amout='" + Amout + '\'' +
                ", AdjustID='" + AdjustID + '\'' +
                ", FigureBookId='" + FigureBookId + '\'' +
                ", StartDate=" + StartDate +
                ", EndDate=" + EndDate +
                ", SubTotal='" + SubTotal + '\'' +
                ", Tax='" + Tax + '\'' +
                ", Total='" + Total + '\'' +
                ", CustomerNew='" + CustomerNew + '\'' +
                ", PriceId=" + PriceId +
                ", TaxCode='" + TaxCode + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", GiaSauThue='" + GiaSauThue + '\'' +
                ", PaymentMethodsCode='" + PaymentMethodsCode + '\'' +
                ", Month=" + Month +
                ", Year=" + Year +
                '}';
    }
}


