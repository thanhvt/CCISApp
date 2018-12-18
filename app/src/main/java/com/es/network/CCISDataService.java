package com.es.network;

import com.es.model.Bill_TaxInvoice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CCISDataService {

    @GET("api/CCIS/getBill_TaxInvoice")
    Call<List<Bill_TaxInvoice>> getBill_TaxInvoice();

    @GET("ThuTien/{id}")
    Call<Integer> ThuTien(@Path("id") int id);
}
