package com.es.network;

import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CCISDataService {

    @GET("getBill_TaxInvoice")
    Call<List<Bill_TaxInvoice>> getBill_TaxInvoice();
}
