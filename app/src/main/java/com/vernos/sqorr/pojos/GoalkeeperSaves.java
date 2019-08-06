package com.vernos.sqorr.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalkeeperSaves {

    @SerializedName("hide")
    @Expose
    private Boolean hide;
    @SerializedName("multiplier")
    @Expose
    private int multiplier;
    @SerializedName("fantasyPoints")
    @Expose
    private int fantasyPoints;
    @SerializedName("value")
    @Expose
    private int value;

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getFantasyPoints() {
        return fantasyPoints;
    }

    public void setFantasyPoints(int fantasyPoints) {
        this.fantasyPoints = fantasyPoints;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}