package com.ss.object;

import com.google.gson.annotations.SerializedName;

public class InfoDoll {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public long price;

    @SerializedName("info")
    public String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
