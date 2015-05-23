package com.cstewart.android.routes.data.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RouteOverview {

    private List<LatLng> mPoints;

    private Distance mDistance;

    public List<LatLng> getPoints() {
        return mPoints;
    }

    public void setPoints(List<LatLng> points) {
        mPoints = points;
    }

    public Distance getDistance() {
        return mDistance;
    }

    public void setDistance(Distance distance) {
        mDistance = distance;
    }
}
