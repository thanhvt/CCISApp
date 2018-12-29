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

    public Mobile_Adjust_DB() {
    }

    public Mobile_Adjust_DB(boolean status, String indexSo, String type, String price, String customerID, String customerAdd, String departmentId, String employeeCode, String customerName, String amout, String adjustID) {
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
        return "ClassPojo [Status = " + Status + ", IndexSo = " + IndexSo + ", Type = " + Type + ", Price = " + Price + ", CustomerID = " + CustomerID + ", CustomerAdd = " + CustomerAdd + ", DepartmentId = " + DepartmentId + ", EmployeeCode = " + EmployeeCode + ", CustomerName = " + CustomerName + ", Amout = " + Amout + ", AdjustID = " + AdjustID + "]";
    }
}