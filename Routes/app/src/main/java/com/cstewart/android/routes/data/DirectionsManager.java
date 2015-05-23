package com.cstewart.android.routes.data;

import com.cstewart.android.routes.data.model.Leg;
import com.cstewart.android.routes.data.model.Route;
import com.cstewart.android.routes.data.model.RouteOverview;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import rx.Observable;

public class DirectionsManager {

    private static final String MODE_WALKING = "walking";

    private DirectionsService mDirectionsService;

    public DirectionsManager(DirectionsService directionsService) {
        mDirectionsService = directionsService;
    }

    public Observable<RouteOverview> getDirections(List<LatLng> points) {

        int lastPosition = points.size() - 1;
        String origin = getConvertedLatLng(points.get(0));
        String destination = getConvertedLatLng(points.get(lastPosition));
        String waypoints = null;
        if (points.size() > 2) {
            waypoints = getConvertedWaypoints(points.subList(1, lastPosition));
        }

        return mDirectionsService
                .getDirections(origin, destination, waypoints, MODE_WALKING)
                .filter(routeContainer -> routeContainer.isValid())
                .map(routeContainer -> {
                    Route route = routeContainer.getRoutes().get(0);
                    Leg leg = route.getLegs().get(0);

                    RouteOverview routeOverview = new RouteOverview();
                    routeOverview.setPoints(route.getPolyline().getDecodedPolyline());
                    routeOverview.setDistance(leg.getDistance());

                    return routeOverview;
                });
    }

    private static String getConvertedLatLng(LatLng latLng) {
        return latLng.latitude + ", " + latLng.longitude;
    }

    private String getConvertedWaypoints(List<LatLng> points) {

        StringBuilder stringBuilder = new StringBuilder();
        for (LatLng latLng : points) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("|");
            }
            stringBuilder.append("via:");
            stringBuilder.append(getConvertedLatLng(latLng));
        }

        return stringBuilder.toString();
    }

}
