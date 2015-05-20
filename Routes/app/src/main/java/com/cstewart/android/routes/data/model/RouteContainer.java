package com.cstewart.android.routes.data.model;

import com.google.gson.annotations.SerializedName;

public class RouteContainer {

    @SerializedName("status")
    private String mStatus;

    public String getStatus() {
        return mStatus;
    }
}
