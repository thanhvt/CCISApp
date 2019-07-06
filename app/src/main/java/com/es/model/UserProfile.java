package com.es.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private int UserId;
    private String UserName;
    private String FullName;
    private String Email;
    private String PhoneNumber;
    private String SkypeID;
    private String CompanyName;
    private String Address;
    private String GenderId;
    private String DepartmentId;
    private String EmployeeCode;
    private String AccountNumber;
    private String BankName;

    public UserProfile() {
    }

    public UserProfile(int userId, String userName, String fullName, String email, String phoneNumber, String skypeID, String companyName, String address, String genderId, String departmentId) {
        UserId = userId;
        UserName = userName;
        FullName = fullName;
        Email = email;
        PhoneNumber = phoneNumber;
        SkypeID = skypeID;
        CompanyName = companyName;
        Address = address;
        GenderId = genderId;
        DepartmentId = departmentId;
    }


    @Override
    public String toString() {
        return "UserProfile{" +
                "UserId='" + UserId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", FullName='" + FullName + '\'' +
                ", Email='" + Email + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", SkypeID='" + SkypeID + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", Address='" + Address + '\'' +
                ", GenderId='" + GenderId + '\'' +
                ", DepartmentId='" + DepartmentId + '\'' +
                '}';
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getSkypeID() {
        return SkypeID;
    }

    public void setSkypeID(String skypeID) {
        SkypeID = skypeID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGenderId() {
        return GenderId;
    }

    public void setGenderId(String genderId) {
        GenderId = genderId;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String departmentId) {
        DepartmentId = departmentId;
    }
}
