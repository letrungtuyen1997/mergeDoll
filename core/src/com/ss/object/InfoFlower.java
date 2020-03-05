package com.ss.object;

import com.google.gson.annotations.SerializedName;

public class InfoFlower {
    @SerializedName("name")
    public String name;

    @SerializedName("lv")
    public int lv;

    @SerializedName("Exp")
    public int Exp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getExp() {
        return Exp;
    }

    public void setExp(int exp) {
        Exp = exp;
    }

    public long getPrice() {
        return Price;
    }

    public void setPrice(long price) {
        Price = price;
    }

    @SerializedName("Price")
    public long Price;
}
