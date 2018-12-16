package com.es.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Bill_TaxInvoiceList {
    @SerializedName("taxinvoice_list")
    private ArrayList<Bill_TaxInvoice> taxInvoicesList;

    public ArrayList<Bill_TaxInvoice> getTaxInvoicesList() {
        return taxInvoicesList;
    }

    public void setTaxInvoicesList(ArrayList<Bill_TaxInvoice> taxInvoicesList) {
        this.taxInvoicesList = taxInvoicesList;
    }
}
