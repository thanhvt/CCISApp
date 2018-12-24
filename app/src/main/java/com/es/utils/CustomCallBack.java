package com.es.utils;

import android.app.ProgressDialog;
import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallBack<T> implements Callback<T> {

    public ProgressDialog mProgressDialog;
    Context context;

    public CustomCallBack(Context context) {
        this.context = context;
        mProgressDialog = new ProgressDialog(context);
//        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }
}
