package com.example.smith.officeapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtility {
    Retrofit retrofit;
    String URL;

    public RetrofitUtility(String URL) {
        this.URL = URL;
    }

    public RequestInterface getRetrofitInterface()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        return requestInterface;
    }

}
