package com.cstewart.android.routes.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteContainer {

    @SerializedName("status")
    private String mStatus;

    @SerializedName("routes")
    private List<Route> mRoutes;

    public String getStatus() {
        return mStatus;
    }

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public boolean isValid() {
        return mRoutes != null && mRoutes.size() > 0;
    }
}
