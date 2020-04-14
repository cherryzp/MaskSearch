package com.cherryzp.masksearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.cherryzp.masksearch.R;
import com.cherryzp.masksearch.network.NetRetrofit;
import com.cherryzp.masksearch.pojo.MaskLocation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity {

    private double lat = 37.5950184;
    private double lng = 127.0169544;
    private int distance = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

//        getHashKey();
//        loadMaskLocation();

        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);

    }

    //KeyHash 탐색
    private void getHashKey () {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    //메인 액티비티로 넘기기
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            finish();

        }
    };
//
//    public void loadMaskLocation() {
//        Call<MaskLocation> maskLocationCall = NetRetrofit.getInstance().getMaskService().doGetMaskLocation(lat, lng, distance);
//
//        Callback<MaskLocation> maskLocationCallback = new Callback<MaskLocation>() {
//            @Override
//            public void onResponse(Call<MaskLocation> call, Response<MaskLocation> response) {
//
//                Handler handler = new Handler();
//
//                handler.postDelayed(runnable, 2000);
//            }
//
//            @Override
//            public void onFailure(Call<MaskLocation> call, Throwable t) {
//
//            }
//        };
//
//        maskLocationCall.enqueue(maskLocationCallback);
//    }
}
