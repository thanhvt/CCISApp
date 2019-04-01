package com.es.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Bill_TaxInvoiceDetail_DB")
public class Bill_TaxInvoiceDetail_DB extends Model implements Serializable {
    @Column(name = "TaxInvoiceDetailId")
    public double TaxInvoiceDetailId;
    @Column(name = "DepartmentId")
    public int DepartmentId;
    @Column(name = "TaxInvoiceId")
    public double TaxInvoiceId;
    @Column(name = "CustomerId")
    public int CustomerId;
    @Column(name = "CustomerCode")
    public String CustomerCode;
    @Column(name = "ServiceTypeId")
    public int ServiceTypeId;
    @Column(name = "ServiceName")
    public String ServiceName;
    @Column(name = "FigureBookId")
    public int FigureBookId;
    @Column(name = "Month")
    public int Month;
    @Column(name = "Year")
    public int Year;
    @Column(name = "Total")
    public double Total;
    @Column(name = "ContractDetailId")
    public int ContractDetailId;
    @Column(name = "CreateUser")
    public int CreateUser;
    @Column(name = "Amount")
    public double Amount;
    @Column(name = "Price")
    public double Price;
    @Column(name = "TypeOfUnit")
    public String TypeOfUnit;
    @Column(name = "Term")
    public int Term;
    @Column(name = "TuNgay")
    public String TuNgay;
    @Column(name = "DenNgay")
    public String DenNgay;

    @Override
    public String toString() {
        return "Bill_TaxInvoiceDetail_DB{" +
                "TaxInvoiceDetailId=" + TaxInvoiceDetailId +
                ", DepartmentId=" + DepartmentId +
                ", TaxInvoiceId=" + TaxInvoiceId +
                ", CustomerId=" + CustomerId +
                ", CustomerCode='" + CustomerCode + '\'' +
                ", ServiceTypeId=" + ServiceTypeId +
                ", ServiceName='" + ServiceName + '\'' +
                ", FigureBookId=" + FigureBookId +
                ", Month=" + Month +
                ", Year=" + Year +
                ", Total=" + Total +
                ", ContractDetailId=" + ContractDetailId +
                ", CreateUser=" + CreateUser +
                ", Amount=" + Amount +
                ", Price=" + Price +
                ", TypeOfUnit='" + TypeOfUnit + '\'' +
                ", Term=" + Term +
                '}';
    }

    public Bill_TaxInvoiceDetail_DB() {
    }

    public double getTaxInvoiceDetailId() {
        return TaxInvoiceDetailId;
    }

    public void setTaxInvoiceDetailId(double taxInvoiceDetailId) {
        TaxInvoiceDetailId = taxInvoiceDetailId;
    }

    public int getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(int departmentId) {
        DepartmentId = departmentId;
    }

    public double getTaxInvoiceId() {
        return TaxInvoiceId;
    }

    public void setTaxInvoiceId(double taxInvoiceId) {
        TaxInvoiceId = taxInvoiceId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public int getServiceTypeId() {
        return ServiceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        ServiceTypeId = serviceTypeId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public int getFigureBookId() {
        return FigureBookId;
    }

    public void setFigureBookId(int figureBookId) {
        FigureBookId = figureBookId;
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

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public int getContractDetailId() {
        return ContractDetailId;
    }

    public void setContractDetailId(int contractDetailId) {
        ContractDetailId = contractDetailId;
    }

    public int getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(int createUser) {
        CreateUser = createUser;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getTypeOfUnit() {
        return TypeOfUnit;
    }

    public void setTypeOfUnit(String typeOfUnit) {
        TypeOfUnit = typeOfUnit;
    }

    public int getTerm() {
        return Term;
    }

    public void setTerm(int term) {
        Term = term;
    }

    public Bill_TaxInvoiceDetail_DB(double taxInvoiceDetailId, int departmentId, int term, double taxInvoiceId, int customerId, String customerCode, int serviceTypeId, String serviceName, int figureBookId, int month, int year, double total, int contractDetailId, int createUser, double amount, double price, String typeOfUnit) {
        TaxInvoiceDetailId = taxInvoiceDetailId;
        DepartmentId = departmentId;
        TaxInvoiceId = taxInvoiceId;
        CustomerId = customerId;
        CustomerCode = customerCode;
        ServiceTypeId = serviceTypeId;
        ServiceName = serviceName;
        FigureBookId = figureBookId;
        Month = month;
        Year = year;
        Total = total;
        ContractDetailId = contractDetailId;
        CreateUser = createUser;
        Amount = amount;
        Price = price;
        TypeOfUnit = typeOfUnit;
        Term = term;
    }

    public Bill_TaxInvoiceDetail_DB(double taxInvoiceDetailId, int departmentId, int term, double taxInvoiceId, int customerId, String customerCode, int serviceTypeId, String serviceName, int figureBookId, int month, int year, double total, int contractDetailId, int createUser, double amount, double price, String typeOfUnit, String tuNgay, String denNgay) {
        TaxInvoiceDetailId = taxInvoiceDetailId;
        DepartmentId = departmentId;
        TaxInvoiceId = taxInvoiceId;
        CustomerId = customerId;
        CustomerCode = customerCode;
        ServiceTypeId = serviceTypeId;
        ServiceName = serviceName;
        FigureBookId = figureBookId;
        Month = month;
        Year = year;
        Total = total;
        ContractDetailId = contractDetailId;
        CreateUser = createUser;
        Amount = amount;
        Price = price;
        TypeOfUnit = typeOfUnit;
        Term = term;
        TuNgay = tuNgay;
        DenNgay = denNgay;
    }
}
