package com.ss.data;

import com.google.gson.annotations.SerializedName;

public class Dolldata {

    @SerializedName("row")
    int row;

    @SerializedName("col")
    int col;

    @SerializedName("type")
    int type;

    @SerializedName("index")
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
