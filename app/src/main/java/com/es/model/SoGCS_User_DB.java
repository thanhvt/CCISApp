package com.es.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "SoGCS_User_DB")
public class SoGCS_User_DB extends Model implements Serializable {
    @Column(name = "EmployeeId")
    public int EmployeeId;
    @Column(name = "EmployeeCode")
    public String EmployeeCode;
    @Column(name = "UserId")
    public int UserId;
    @Column(name = "BookCode")
    public String BookCode;
    @Column(name = "BookName")
    public String BookName;


    public SoGCS_User_DB() {
    }

    @Override
    public String toString() {
        return "SoGCS_User{" +
                "EmployeeId=" + EmployeeId +
                ", EmployeeCode='" + EmployeeCode + '\'' +
                ", UserId=" + UserId +
                ", BookCode='" + BookCode + '\'' +
                ", BookName='" + BookName + '\'' +
                '}';
    }

    public SoGCS_User_DB(int employeeId, String employeeCode, int userId, String bookCode, String bookName) {
        EmployeeId = employeeId;
        EmployeeCode = employeeCode;
        UserId = userId;
        BookCode = bookCode;
        BookName = bookName;
    }

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
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

    public String getBookCode() {
        return BookCode;
    }

    public void setBookCode(String bookCode) {
        BookCode = bookCode;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }
}
