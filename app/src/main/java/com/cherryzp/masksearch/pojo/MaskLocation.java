package com.cherryzp.masksearch.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaskLocation {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("stores")
    @Expose
    private List<Store> stores = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    @Override
    public String toString() {
        return "MaskLocation{" +
                "count=" + count +
                ", stores=" + stores +
                '}';
    }
}