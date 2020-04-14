package com.cherryzp.masksearch.network;

import com.cherryzp.masksearch.service.MaskService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {

    private Retrofit retrofit;
    private MaskService maskService;
    public static NetRetrofit netRetrofit = new NetRetrofit();

    public NetRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl("https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/").addConverterFactory(GsonConverterFactory.create()).build();

        maskService = retrofit.create(MaskService.class);
    }

    public static NetRetrofit getInstance() {
        return netRetrofit;
    }

    public MaskService getMaskService() {
        return maskService;
    }


}
