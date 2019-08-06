package com.vernos.sqorr.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpponentTeamScore {
    @SerializedName("hide")
    @Expose
    private Boolean hide;
    @SerializedName("value")
    @Expose
    private int value;

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}