package com.cherryzp.masksearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cherryzp.masksearch.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

public class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

    private final View calloutBalloon;
    private LayoutInflater layoutInflater;

    private String plenty = "100개 이상";
    private String some = "30개 미만";
    private String few = "2개 이상 30개 미만";
    private String empty = "1개 이하";
    private String soldout = "판매중지";

    public CustomCalloutBalloonAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        calloutBalloon = layoutInflater.inflate(R.layout.callout_balloon_store, null);
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        ((TextView)calloutBalloon.findViewById(R.id.title_tv)).setText("상호명 : " + mapPOIItem.getItemName().split("##")[0]);
        ((TextView)calloutBalloon.findViewById(R.id.addr_tv)).setText("주소 : " + mapPOIItem.getItemName().split("##")[1]);
        ((TextView)calloutBalloon.findViewById(R.id.stock_tv)).setText("입고시간 : " + mapPOIItem.getItemName().split("##")[3]);
        ((TextView)calloutBalloon.findViewById(R.id.created_tv)).setText("마스크 확인시간 : " + mapPOIItem.getItemName().split("##")[4]);

        if (mapPOIItem.getItemName().split("##")[2].equals("plenty")){
            ((TextView)calloutBalloon.findViewById(R.id.remain_tv)).setText("재고상태 : " + plenty);

        } else if (mapPOIItem.getItemName().split("##")[2].equals("some")) {
            ((TextView)calloutBalloon.findViewById(R.id.remain_tv)).setText("재고상태 : " + some);

        } else if (mapPOIItem.getItemName().split("##")[2].equals("few")) {
            ((TextView)calloutBalloon.findViewById(R.id.remain_tv)).setText("재고상태 : " + few);

        } else if (mapPOIItem.getItemName().split("##")[2].equals("empty")) {
            ((TextView)calloutBalloon.findViewById(R.id.remain_tv)).setText("재고상태 : " + empty);

        } else if (mapPOIItem.getItemName().split("##")[2].equals("break")) {
            ((TextView)calloutBalloon.findViewById(R.id.remain_tv)).setText("재고상태 : " + soldout);

        } else {
            ((TextView)calloutBalloon.findViewById(R.id.remain_tv)).setText("재고상태 : " + "정보없음");

        }


        return calloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }

}
