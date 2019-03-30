package com.es.network;

import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceDetail;
import com.es.model.Mobile_Adjust_Informations;
import com.es.model.SoGCS_User;
import com.es.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CCISDataService {

    @GET("getUserProfile/{userid}")
    Call<List<UserProfile>> getUserProfile(@Path("userid") String userid);

    @GET("Bill_TaxInvoiceDetail/{id}")
    Call<List<Bill_TaxInvoiceDetail>> getBill_TaxInvoiceDetail(@Path("id") long id);

    @GET("getList_Bill_TaxInvoiceDetail/{id}")
    Call<List<Bill_TaxInvoiceDetail>> getList_Bill_TaxInvoiceDetail(@Path("id") String id);

    @GET("getBill_TaxInvoice/{status}/{userid}")
    Call<List<Bill_TaxInvoice>> getBill_TaxInvoice(@Path("status") int status, @Path("userid") int userid);

    @GET("ThuTien/{id}")
    Call<Integer> ThuTien(@Path("id") int id);

    @GET("ThuTienLo/{ids}")
    Call<Integer> ThuTienLo(@Path("ids") String ids);

    @GET("Mobile_Adjust_Informations/{id}")
    Call<List<Mobile_Adjust_Informations>> getMobile_Adjust_Informations(@Path("id") int id);

    @POST("Mobile_Adjust_Informations/")
    Call<Boolean> Post(@Body Mobile_Adjust_Informations devices);

    @POST("Mobile_Adjust_Informations_List/")
    Call<String> Post_List(@Body List<Mobile_Adjust_Informations> devices);

    @GET("Account/{username}/{pass}")
    Call<List<UserProfile>> CheckLogin(@Path("username") String username, @Path("pass") String pass);

    @GET("getSo_User/{userid}")
    Call<List<SoGCS_User>> getSo_User(@Path("userid") int userid);
}
