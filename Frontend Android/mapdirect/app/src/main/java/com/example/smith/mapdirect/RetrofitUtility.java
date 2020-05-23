package com.example.smith.mapdirect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtility {
    Retrofit retrofit;
    String BaseURL;


    public RetrofitUtility(String Url)
    {
        BaseURL=Url;
    }

    public Retrofit getRetrofit() {
        retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
