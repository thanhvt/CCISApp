package com.es.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    //    private static final String BASE_URL = "http://192.168.100.7:8022/";
    private static final String BASE_URL = "http://14.248.85.241:88/";
    //http://103.63.108.5:8084/

    /**
     * Create an instance of Retrofit object
     */
    public static Retrofit getRetrofitInstance(Context mContext) {
        String root = BASE_URL;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String IP_SERVICE = sharedPrefs.getString("IP_SERVICE", "0");
        String def = BASE_URL;
        if (!IP_SERVICE.equals("0")) {
            def = "http://" + IP_SERVICE + "/";
        }
        if (retrofit == null || !def.equals(root)) {

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(def)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

//            retrofit = new retrofit2.Retrofit.Builder()
//                    .baseUrl(def)
//                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create())).build();

        }
        return retrofit;
    }
}
