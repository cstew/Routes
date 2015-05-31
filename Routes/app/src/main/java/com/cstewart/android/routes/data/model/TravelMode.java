package com.cstewart.android.routes.data.model;

public enum TravelMode {

    WALKING("walking"),
    BICYCLING("bicycling");

    private String mServerName;

    private TravelMode(String serverName) {
        mServerName = serverName;
    }

    public String getServerName() {
        return mServerName;
    }
}
