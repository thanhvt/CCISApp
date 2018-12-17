package com.es.model;

import java.io.Serializable;

public class Bill_TaxInvoice implements Serializable {
    private String TaxCode;

    private String CustomerCode;

    private String BankName;

    private String Month;

    private String SerialNumber;

    private String Year;

    private String CustomerId;

    private String DepartmentId;

    private String $id;

    private String TaxInvoiceAddress;

    private String TaxInvoiceId;

    private int IdDevice;

    private String ContractId;

    private int FigureBookId;

    private String SerialCode;

    private String CustomerName;

    private String CustomerCode_Pay;

    private String SubTotal;

    private String Address_Pay;

    private String BankAccount;

    private String VAT;

    private String TaxRatio;

    private String CustomerId_Pay;

    private String BillType;

    private String CustomerName_Pay;

    private String Total;

    public String getTaxCode ()
    {
        return TaxCode;
    }

    public void setTaxCode (String TaxCode)
    {
        this.TaxCode = TaxCode;
    }

    public String getCustomerCode ()
    {
        return CustomerCode;
    }

    public void setCustomerCode (String CustomerCode)
    {
        this.CustomerCode = CustomerCode;
    }

    public String getBankName ()
    {
        return BankName;
    }

    public void setBankName (String BankName)
    {
        this.BankName = BankName;
    }

    public String getMonth ()
    {
        return Month;
    }

    public void setMonth (String Month)
    {
        this.Month = Month;
    }

    public String getSerialNumber ()
    {
        return SerialNumber;
    }

    public void setSerialNumber (String SerialNumber)
    {
        this.SerialNumber = SerialNumber;
    }

    public String getYear ()
    {
        return Year;
    }

    public void setYear (String Year)
    {
        this.Year = Year;
    }

    public String getCustomerId ()
    {
        return CustomerId;
    }

    public void setCustomerId (String CustomerId)
    {
        this.CustomerId = CustomerId;
    }

    public String getDepartmentId ()
    {
        return DepartmentId;
    }

    public void setDepartmentId (String DepartmentId)
    {
        this.DepartmentId = DepartmentId;
    }

    public String get$id ()
    {
        return $id;
    }

    public void set$id (String $id)
    {
        this.$id = $id;
    }

    public String getTaxInvoiceAddress ()
    {
        return TaxInvoiceAddress;
    }

    public void setTaxInvoiceAddress (String TaxInvoiceAddress)
    {
        this.TaxInvoiceAddress = TaxInvoiceAddress;
    }

    public String getTaxInvoiceId ()
    {
        return TaxInvoiceId;
    }

    public void setTaxInvoiceId (String TaxInvoiceId)
    {
        this.TaxInvoiceId = TaxInvoiceId;
    }

    public int getIdDevice ()
    {
        return IdDevice;
    }

    public void setIdDevice (int IdDevice)
    {
        this.IdDevice = IdDevice;
    }

    public String getContractId ()
    {
        return ContractId;
    }

    public void setContractId (String ContractId)
    {
        this.ContractId = ContractId;
    }

    public int getFigureBookId ()
    {
        return FigureBookId;
    }

    public void setFigureBookId (int FigureBookId)
    {
        this.FigureBookId = FigureBookId;
    }

    public String getSerialCode ()
    {
        return SerialCode;
    }

    public void setSerialCode (String SerialCode)
    {
        this.SerialCode = SerialCode;
    }

    public String getCustomerName ()
    {
        return CustomerName;
    }

    public void setCustomerName (String CustomerName)
    {
        this.CustomerName = CustomerName;
    }

    public String getCustomerCode_Pay ()
    {
        return CustomerCode_Pay;
    }

    public void setCustomerCode_Pay (String CustomerCode_Pay)
    {
        this.CustomerCode_Pay = CustomerCode_Pay;
    }

    public String getSubTotal ()
    {
        return SubTotal;
    }

    public void setSubTotal (String SubTotal)
    {
        this.SubTotal = SubTotal;
    }

    public String getAddress_Pay ()
    {
        return Address_Pay;
    }

    public void setAddress_Pay (String Address_Pay)
    {
        this.Address_Pay = Address_Pay;
    }

    public String getBankAccount ()
    {
        return BankAccount;
    }

    public void setBankAccount (String BankAccount)
    {
        this.BankAccount = BankAccount;
    }

    public String getVAT ()
    {
        return VAT;
    }

    public void setVAT (String VAT)
    {
        this.VAT = VAT;
    }

    public String getTaxRatio ()
    {
        return TaxRatio;
    }

    public void setTaxRatio (String TaxRatio)
    {
        this.TaxRatio = TaxRatio;
    }

    public String getCustomerId_Pay ()
    {
        return CustomerId_Pay;
    }

    public void setCustomerId_Pay (String CustomerId_Pay)
    {
        this.CustomerId_Pay = CustomerId_Pay;
    }

    public String getBillType ()
    {
        return BillType;
    }

    public void setBillType (String BillType)
    {
        this.BillType = BillType;
    }

    public String getCustomerName_Pay ()
    {
        return CustomerName_Pay;
    }

    public void setCustomerName_Pay (String CustomerName_Pay)
    {
        this.CustomerName_Pay = CustomerName_Pay;
    }

    public String getTotal ()
    {
        return Total;
    }

    public void setTotal (String Total)
    {
        this.Total = Total;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TaxCode = "+TaxCode+", CustomerCode = "+CustomerCode+", BankName = "+BankName+", Month = "+Month+", SerialNumber = "+SerialNumber+", Year = "+Year+", CustomerId = "+CustomerId+", DepartmentId = "+DepartmentId+", $id = "+$id+", TaxInvoiceAddress = "+TaxInvoiceAddress+", TaxInvoiceId = "+TaxInvoiceId+", IdDevice = "+IdDevice+", ContractId = "+ContractId+", FigureBookId = "+FigureBookId+", SerialCode = "+SerialCode+", CustomerName = "+CustomerName+", CustomerCode_Pay = "+CustomerCode_Pay+", SubTotal = "+SubTotal+", Address_Pay = "+Address_Pay+", BankAccount = "+BankAccount+", VAT = "+VAT+", TaxRatio = "+TaxRatio+", CustomerId_Pay = "+CustomerId_Pay+", BillType = "+BillType+", CustomerName_Pay = "+CustomerName_Pay+", Total = "+Total+"]";
    }
}
