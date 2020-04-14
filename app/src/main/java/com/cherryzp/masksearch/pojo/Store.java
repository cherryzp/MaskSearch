package com.cherryzp.masksearch.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("remain_stat")
    @Expose
    private String remainStat;
    @SerializedName("stock_at")
    @Expose
    private String stockAt;
    @SerializedName("type")
    @Expose
    private String type;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemainStat() {
        return remainStat;
    }

    public void setRemainStat(String remainStat) {
        this.remainStat = remainStat;
    }

    public String getStockAt() {
        return stockAt;
    }

    public void setStockAt(String stockAt) {
        this.stockAt = stockAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Store{" +
                "addr='" + addr + '\'' +
                ", code='" + code + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", name='" + name + '\'' +
                ", remainStat='" + remainStat + '\'' +
                ", stockAt='" + stockAt + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}