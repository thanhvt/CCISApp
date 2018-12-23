package com.es.model;

import java.io.Serializable;

public class Mobile_Adjust_Informations implements Serializable {
    private String Status;

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
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
        return "ClassPojo [Status = " + Status + ", Index = " + Index + ", Type = " + Type + ", Price = " + Price + ", CustomerID = " + CustomerID + ", CustomerAdd = " + CustomerAdd + ", DepartmentId = " + DepartmentId + ", EmployeeCode = " + EmployeeCode + ", CustomerName = " + CustomerName + ", $id = " + $id + ", Amout = " + Amout + ", AdjustID = " + AdjustID + "]";
    }
}
