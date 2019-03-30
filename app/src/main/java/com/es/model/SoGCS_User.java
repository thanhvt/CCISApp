package com.es.model;

import java.io.Serializable;

public class SoGCS_User implements Serializable {
    /**
     * p.EmployeeId,
     * p.EmployeeCode,
     * p.Status,
     * p.UserId,
     * d.BookCode,
     * d.BookName
     */
    public int EmployeeId;
    public String EmployeeCode;
    public int UserId;
    public String BookCode;
    public String BookName;

    public SoGCS_User() {
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

    public SoGCS_User(int employeeId, String employeeCode, int userId, String bookCode, String bookName) {
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
