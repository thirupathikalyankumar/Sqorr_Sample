package com.vernos.sqorr.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Started {

    @SerializedName("hide")
    @Expose
    private Boolean hide;
    @SerializedName("value")
    @Expose
    private Integer value;

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


}
