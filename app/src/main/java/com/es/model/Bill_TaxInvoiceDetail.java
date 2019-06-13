package com.es.model;

import java.io.Serializable;

public class Bill_TaxInvoiceDetail implements Serializable {

    public double TaxInvoiceDetailId;
    public int DepartmentId;
    public double TaxInvoiceId;
    public int CustomerId;
    public String CustomerCode;
    public int ServiceTypeId;
    public String ServiceName;
    public int FigureBookId;
    public int Month;
    public int Year;
    public double Total;
    public int ContractDetailId;
    public int CreateUser;
    public double Amount;
    public double Price;
    public String TypeOfUnit;
    public double Term;
    public String TuNgay;
    public String DenNgay;

    public String getTuNgay() {
        return TuNgay;
    }

    public void setTuNgay(String tuNgay) {
        TuNgay = tuNgay;
    }

    public String getDenNgay() {
        return DenNgay;
    }

    public void setDenNgay(String denNgay) {
        DenNgay = denNgay;
    }

    @Override
    public String toString() {
        return "Bill_TaxInvoiceDetail{" +
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
                ", Term=" + Term +
                ", TypeOfUnit='" + TypeOfUnit + '\'' +
                '}';
    }

    public Bill_TaxInvoiceDetail() {
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

    public double getTerm() {
        return Term;
    }

    public void setTerm(double term) {
        Term = term;
    }

    public Bill_TaxInvoiceDetail(double taxInvoiceDetailId, int departmentId, double taxInvoiceId, int customerId, String customerCode, int serviceTypeId, String serviceName, int figureBookId, int month, int year, double total, int contractDetailId, int createUser, double amount, double price, String typeOfUnit, double term, String tuNgay, String denNgay) {
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

    /*[
    "Amount":3.0000,
    "ContractDetailId":0,
    "CustomerCode":"CDn3D001483",
    "CustomerId":272589,
    "DepartmentId":6,
    "FigureBookId":6063,
    "Month":1,
    "Price":6000.0000,
    "ServiceName":"HDAN - Hộ dân",
    "ServiceTypeId":1,
    "TaxInvoiceDetailId":10725.0,
    "TaxInvoiceId":13685.0,
    "Total":18000.00,
    "TypeOfUnit":"Nhân khẩu",
    "Year":2019}]
    */
}
