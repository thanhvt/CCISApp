package com.es.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Concus_Customer_DB")
public class Concus_Customer_DB extends Model implements Serializable {
    @Column(name = "CustomerId")
    public int CustomerId;

    @Column(name = "CustomerCode")
    public String CustomerCode;

    @Column(name = "DepartmentId")
    public int DepartmentId;

    @Column(name = "Name")
    public String Name;

    @Column(name = "Address")
    public String Address;

    @Column(name = "InvoiceAddress")
    public String InvoiceAddress;

    @Column(name = "Fax")
    public String Fax;

    @Column(name = "Gender")
    public int Gender;

    @Column(name = "Email")
    public String Email;

    @Column(name = "PhoneNumber")
    public String PhoneNumber;

    @Column(name = "TaxCode")
    public String TaxCode;

    @Column(name = "Ratio")
    public float Ratio;

    @Column(name = "BankAccount")
    public String BankAccount;

    @Column(name = "BankName")
    public String BankName;

    @Column(name = "Status")
    public int Status;

    @Column(name = "CreateUser")
    public int CreateUser;

    @Column(name = "OccupationsGroupCode")
    public String OccupationsGroupCode;

    @Column(name = "PhoneCustomerCare")
    public String PhoneCustomerCare;

    @Column(name = "PaymentMethodsCode")
    public String PaymentMethodsCode;

    public Concus_Customer_DB() {
    }

    public Concus_Customer_DB(int customerId, String customerCode, int departmentId, String name, String address, String invoiceAddress, String fax, int gender, String email, String phoneNumber, String taxCode, float ratio, String bankAccount, String bankName, int status, int createUser, String occupationsGroupCode, String phoneCustomerCare, String paymentMethodsCode) {
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
}
