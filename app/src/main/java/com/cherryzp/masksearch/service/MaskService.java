package com.cherryzp.masksearch.service;

import com.cherryzp.masksearch.pojo.MaskLocation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MaskService {

    @GET("storesByGeo/json")
    Call<MaskLocation> doGetMaskLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("m") int distance);

}
