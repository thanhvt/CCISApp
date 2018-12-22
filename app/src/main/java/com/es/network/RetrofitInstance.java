package com.es.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    //    private static final String BASE_URL = "http://192.168.100.7:8022/";
    private static final String BASE_URL = "http://103.63.108.5:8084/";
    //http://103.63.108.5:8084/

    /**
     * Create an instance of Retrofit object
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
