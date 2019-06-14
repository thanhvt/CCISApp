package com.es.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Mobile_Adjust_DB")
public class Mobile_Adjust_DB extends Model implements Serializable {
    @Column(name = "Status")
    private boolean Status;

    @Column(name = "IndexSo")
    private String IndexSo;

    @Column(name = "Type")
    private String Type;

    @Column(name = "Price")
    private String Price;

    @Column(name = "CustomerID")
    private String CustomerID;

    @Column(name = "CustomerAdd")
    private String CustomerAdd;

    @Column(name = "DepartmentId")
    private String DepartmentId;

    @Column(name = "EmployeeCode")
    private String EmployeeCode;

    @Column(name = "CustomerName")
    private String CustomerName;

    @Column(name = "Amout")
    private String Amout;

    @Column(name = "AdjustID")
    private String AdjustID;

    @Column(name = "StartDate")
    private String StartDate;

    @Column(name = "EndDate")
    private String EndDate;

    @Column(name = "FigureBookId")
    private String FigureBookId;

    @Column(name = "SubTotal")
    private String SubTotal;

    @Column(name = "Tax")
    private String Tax;

    @Column(name = "Total")
    private String Total;

    @Column(name = "CustomerNew")
    private String CustomerNew;

    @Column(name = "IS_SUBMIT")
    private String IS_SUBMIT;

    @Column(name = "PriceId")
    private int PriceId;

    @Column(name = "TaxCode")
    private String TaxCode;

    @Column(name = "PhoneNumber")
    private String PhoneNumber;

    @Column(name = "Email")
    private String Email;

    @Column(name = "GiaSauThue")
    private String GiaSauThue;

    public Mobile_Adjust_DB() {
    }

    public Mobile_Adjust_DB(boolean status, String indexSo, String type, String price, String customerID, String customerAdd, String departmentId,
                            String employeeCode, String customerName, String amout, String adjustID, String startDate, String endDate, String figureBookId,
                            String subTotal, String tax, String total, String customerNew, String IS_SUBMIT, int PriceId, String taxCode,
                            String phoneNumber,
                            String email,
                            String GiaSauThue) {
        Status = status;
        IndexSo = indexSo;
        Type = type;
        Price = price;
        CustomerID = customerID;
        CustomerAdd = customerAdd;
        DepartmentId = departmentId;
        EmployeeCode = employeeCode;
        CustomerName = customerName;
        Amout = amout;
        AdjustID = adjustID;
        StartDate = startDate;
        EndDate = endDate;
        FigureBookId = figureBookId;
        SubTotal = subTotal;
        Tax = tax;
        Total = total;
        CustomerNew = customerNew;
        this.IS_SUBMIT = IS_SUBMIT;
        this.PriceId = PriceId;
        TaxCode = taxCode;
        PhoneNumber = phoneNumber;
        Email = email;
        this.GiaSauThue = GiaSauThue;
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
        this.PriceId = priceId;
    }

    public String getIS_SUBMIT() {
        return IS_SUBMIT;
    }

    public void setIS_SUBMIT(String IS_SUBMIT) {
        this.IS_SUBMIT = IS_SUBMIT;
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

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public String getIndexSo() {
        return IndexSo;
    }

    public void setIndexSo(String Index) {
        this.IndexSo = Index;
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
        return "Mobile_Adjust_DB{" +
                "Status=" + Status +
                ", IndexSo='" + IndexSo + '\'' +
                ", Type='" + Type + '\'' +
                ", Price='" + Price + '\'' +
                ", CustomerID='" + CustomerID + '\'' +
                ", CustomerAdd='" + CustomerAdd + '\'' +
                ", DepartmentId='" + DepartmentId + '\'' +
                ", EmployeeCode='" + EmployeeCode + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", Amout='" + Amout + '\'' +
                ", AdjustID='" + AdjustID + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", FigureBookId='" + FigureBookId + '\'' +
                ", SubTotal='" + SubTotal + '\'' +
                ", Tax='" + Tax + '\'' +
                ", Total='" + Total + '\'' +
                ", CustomerNew='" + CustomerNew + '\'' +
                ", IS_SUBMIT='" + IS_SUBMIT + '\'' +
                ", PriceId=" + PriceId +
                ", TaxCode='" + TaxCode + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", GiaSauThue='" + GiaSauThue + '\'' +
                '}';
    }
}
