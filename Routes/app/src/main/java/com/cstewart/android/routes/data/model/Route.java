package com.cstewart.android.routes.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("overview_polyline")
    private Polyline mPolyline;

    @SerializedName("legs")
    private List<Leg> mLegs;

    public Polyline getPolyline() {
        return mPolyline;
    }
}
