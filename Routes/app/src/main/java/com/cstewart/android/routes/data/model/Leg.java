package com.cstewart.android.routes.data.model;

import com.google.gson.annotations.SerializedName;

public class Leg {

    @SerializedName("distance")
    private Distance mDistance;

    public Distance getDistance() {
        return mDistance;
    }

}
