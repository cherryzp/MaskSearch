package com.cherryzp.masksearch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cherryzp.masksearch.R;
import com.cherryzp.masksearch.adapter.CustomCalloutBalloonAdapter;
import com.cherryzp.masksearch.network.NetRetrofit;
import com.cherryzp.masksearch.pojo.MaskLocation;
import com.cherryzp.masksearch.pojo.Store;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //맵 관련
    private MapView mapView;

    private ArrayList<Store> stores;

    //위치정보
    private Handler myLocationHandler = new Handler();
    private LocationManager lm;
    private CardView myLocationCardView;
    private CardView cameraLocationCardView;

    private double lat;
    private double lng;
    private int distance = 1000;

    private double cameraLat;
    private double cameraLng;

    //네비게이션 관련
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    //애드몹관련
    private AdView adView;

    //네비 메뉴 관련
    private LinearLayout maskPolicyLinearLayout;
    private LinearLayout purchaseInfoLinearLayout;
    private LinearLayout sendEmailLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findId();
        insertMap();
        setListener();
        checkGpsPermission();
        getMyLocation();
//        insertAdmob();
        setNavigation();
    }

    public void findId() {
        //지도
        myLocationCardView = findViewById(R.id.my_location_cv);
        cameraLocationCardView = findViewById(R.id.camera_location_cv);

        //네비게이션
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        //애드몹
        adView = findViewById(R.id.ad_view);

        //네비 메뉴
        maskPolicyLinearLayout = findViewById(R.id.mask_policy_ll);
        purchaseInfoLinearLayout = findViewById(R.id.purchase_info_ll);
        sendEmailLinearLayout = findViewById(R.id.send_email_ll);
    }

    public void setListener() {
        mapView.setMapViewEventListener(mapViewEventListener);
        myLocationCardView.setOnClickListener(locationListener);
        cameraLocationCardView.setOnClickListener(locationListener);

        //네비 메뉴
        maskPolicyLinearLayout.setOnClickListener(menuListener);
        purchaseInfoLinearLayout.setOnClickListener(menuListener);
        sendEmailLinearLayout.setOnClickListener(menuListener);
    }

    private void checkGpsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
    }

    private void searchLocationClickable(boolean clickable) {
        myLocationCardView.setClickable(clickable);
        cameraLocationCardView.setClickable(clickable);
    }

    // 맵 넣기
    public void insertMap() {
        mapView = new MapView(this);

        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter(this));
        mapViewContainer.addView(mapView);

    }

    public void insertAdmob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private void setNavigation() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    public Location getMyLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        } else {
            Log.d("current_location", "현재 위치 찾기 시작");
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            try {
                if (location != null) {
                    lng = location.getLongitude();
                    lat = location.getLatitude();

                    loadMaskLocation();
                } else {
                    Toast.makeText(MainActivity.this, "위치정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "위치정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        return location;

    }

    public void loadMaskLocation() {
        searchLocationClickable(false);
        Call<MaskLocation> maskLocationCall = NetRetrofit.getInstance().getMaskService().doGetMaskLocation(lat, lng, distance);

        Callback<MaskLocation> maskLocationCallback = new Callback<MaskLocation>() {
            @Override
            public void onResponse(Call<MaskLocation> call, Response<MaskLocation> response) {

                MaskLocation maskLocation = response.body();
                stores = (ArrayList<Store>) maskLocation.getStores();

                if (mapView.getPOIItems() != null ){
                    mapView.removeAllPOIItems();
                }
                mapView.addPOIItems(setStoreMap());
                mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lng), 3, true);

                searchLocationClickable(true);
            }

            @Override
            public void onFailure(Call<MaskLocation> call, Throwable t) {
                searchLocationClickable(true);
            }
        };

        maskLocationCall.enqueue(maskLocationCallback);
    }

    private MapPOIItem[] setStoreMap() {
        MapPOIItem[] mapPOIItems = new MapPOIItem[stores.size()];

        for (int i = 0; i < stores.size(); i++) {
            MapPOIItem marker = new MapPOIItem();

            marker.setItemName(stores.get(i).getName()
                    + "##" + stores.get(i).getAddr()
                    + "##" + stores.get(i).getRemainStat()
                    + "##" + stores.get(i).getStockAt()
                    + "##" + stores.get(i).getCreatedAt());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(stores.get(i).getLat(), stores.get(i).getLng()));

//            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
//            marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);


            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            if (stores.get(i).getRemainStat() != null) {
                if (stores.get(i).getRemainStat().equals("plenty")) {
                    marker.setCustomImageResourceId(R.drawable.ic_place_green_50px);
                    marker.setCustomImageAutoscale(false);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                } else if (stores.get(i).getRemainStat().equals("some")) {
                    marker.setCustomImageResourceId(R.drawable.ic_place_yellow_50px);
                    marker.setCustomImageAutoscale(false);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                } else if (stores.get(i).getRemainStat().equals("few")) {
                    marker.setCustomImageResourceId(R.drawable.ic_place_red_50px);
                    marker.setCustomImageAutoscale(false);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                } else if (stores.get(i).getRemainStat().equals("empty")) {
                    marker.setCustomImageResourceId(R.drawable.ic_place_grey_50px);
                    marker.setCustomImageAutoscale(false);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                } else if (stores.get(i).getRemainStat().equals("break")) {
                    marker.setCustomImageResourceId(R.drawable.ic_place_grey_50px);
                    marker.setCustomImageAutoscale(false);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                } else {
                    marker.setCustomImageResourceId(R.drawable.ic_place_grey_50px);
                    marker.setCustomImageAutoscale(false);
                    marker.setCustomImageAnchor(0.5f, 1.0f);
                }
            } else {
                marker.setCustomImageResourceId(R.drawable.ic_place_grey_50px);
                marker.setCustomImageAutoscale(false);
                marker.setCustomImageAnchor(0.5f, 1.0f);
            }


            mapPOIItems[i] = marker;
        }

        return mapPOIItems;
    }

    private View.OnClickListener locationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.my_location_cv:
                    getMyLocation();
                    break;

                case R.id.camera_location_cv:
                    lat = cameraLat;
                    lng = cameraLng;
                    loadMaskLocation();
                    break;
            }
        }
    };

    private View.OnClickListener menuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mask_policy_ll:
                    startActivity(new Intent(MainActivity.this, MaskPolicyActivity.class));
                    break;

                case R.id.purchase_info_ll:
                    startActivity(new Intent(MainActivity.this, PurchaseInfoActivity.class));
                    break;

                case R.id.send_email_ll:
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("plain/text");
                    String[] address = {"coronasearchhelp@gmail.com"};
                    email.putExtra(Intent.EXTRA_EMAIL, address);
                    email.putExtra(Intent.EXTRA_SUBJECT, "문의합니다.");
                    email.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(email);
                    break;
            }
        }
    };

    private MapView.MapViewEventListener mapViewEventListener = new MapView.MapViewEventListener() {
        @Override
        public void onMapViewInitialized(MapView mapView) {

        }

        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
            try {
                MapPoint.GeoCoordinate mapPointGeo = mapView.getMapCenterPoint().getMapPointGeoCoord();

                cameraLat = mapPointGeo.latitude;
                cameraLng = mapPointGeo.longitude;
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "위치정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {
            try {
                MapPoint.GeoCoordinate mapPointGeo = mapView.getMapCenterPoint().getMapPointGeoCoord();

                cameraLat = mapPointGeo.latitude;
                cameraLng = mapPointGeo.longitude;

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "위치정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    Location location = getMyLocation();
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    
                    loadMaskLocation();
                } catch (Exception e) {
                    Toast.makeText(this, "위치정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "위치정보에 접근을 허용하셔야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }

//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setMessage("앱을 종료하시겠습니까?").setNegativeButton("아니오", null).setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();

    }

}
