package com.es.model;

import java.io.Serializable;

public class Concus_Customer implements Serializable {

    public int CustomerId;
    public String CustomerCode;
    public int DepartmentId;
    public String Name;
    public String Address;
    public String InvoiceAddress;
    public String Fax;
    public int Gender;
    public String Email;
    public String PhoneNumber;
    public String TaxCode;
    public float Ratio;
    public String BankAccount;
    public String BankName;
    public int Status;
    public int CreateUser;
    public String OccupationsGroupCode;
    public String PhoneCustomerCare;
    public String PaymentMethodsCode;

    public Concus_Customer() {
    }

    public Concus_Customer(int customerId, String customerCode, int departmentId, String name, String address, String invoiceAddress, String fax, int gender, String email, String phoneNumber, String taxCode, float ratio, String bankAccount, String bankName, int status, int createUser, String occupationsGroupCode, String phoneCustomerCare, String paymentMethodsCode) {
        CustomerId = customerId;
        CustomerCode = customerCode;
        DepartmentId = departmentId;
        Name = name;
        Address = address;
        InvoiceAddress = invoiceAddress;
        Fax = fax;
        Gender = gender;
        Email = email;
        PhoneNumber = phoneNumber;
        TaxCode = taxCode;
        Ratio = ratio;
        BankAccount = bankAccount;
        BankName = bankName;
        Status = status;
        CreateUser = createUser;
        OccupationsGroupCode = occupationsGroupCode;
        PhoneCustomerCare = phoneCustomerCare;
        PaymentMethodsCode = paymentMethodsCode;
    }

    @Override
    public String toString() {
        return "Concus_Customer{" +
                "CustomerId=" + CustomerId +
                ", CustomerCode='" + CustomerCode + '\'' +
                ", DepartmentId=" + DepartmentId +
                ", Name='" + Name + '\'' +
                ", Address='" + Address + '\'' +
                ", InvoiceAddress='" + InvoiceAddress + '\'' +
                ", Fax='" + Fax + '\'' +
                ", Gender=" + Gender +
                ", Email='" + Email + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", TaxCode='" + TaxCode + '\'' +
                ", Ratio=" + Ratio +
                ", BankAccount='" + BankAccount + '\'' +
                ", BankName='" + BankName + '\'' +
                ", Status=" + Status +
                ", CreateUser=" + CreateUser +
                ", OccupationsGroupCode='" + OccupationsGroupCode + '\'' +
                ", PhoneCustomerCare='" + PhoneCustomerCare + '\'' +
                ", PaymentMethodsCode='" + PaymentMethodsCode + '\'' +
                '}';
    }
}
