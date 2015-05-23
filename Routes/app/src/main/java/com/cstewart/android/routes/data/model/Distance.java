package com.cstewart.android.routes.data.model;

import com.google.gson.annotations.SerializedName;

public class Distance {

    @SerializedName("text")
    private String mText;

    @SerializedName("value")
    private int mValueMeters;

    public String getText() {
        return mText;
    }

    public int getValueMeters() {
        return mValueMeters;
    }

}
