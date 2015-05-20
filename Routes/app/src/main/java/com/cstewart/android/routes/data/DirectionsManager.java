package com.cstewart.android.routes.data;

import com.cstewart.android.routes.data.model.Route;
import com.cstewart.android.routes.data.model.RouteContainer;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DirectionsManager {

    private static final String MODE_WALKING = "walking";

    private DirectionsService mDirectionsService;

    public DirectionsManager(DirectionsService directionsService) {
        mDirectionsService = directionsService;
    }

    public void getDirections(List<LatLng> points, final Callback<List<LatLng>> callback) {
        mDirectionsService.getDirections(getConvertedLatLng(points.get(0)), getConvertedLatLng(points.get(1)), MODE_WALKING, new Callback<RouteContainer>() {
            @Override
            public void success(RouteContainer routeContainer, Response response) {
                if (!routeContainer.isValid()) {
                    callback.failure(RetrofitError.unexpectedError("Unable to pull out routes", new IOException()));
                    return;
                }

                Route route = routeContainer.getRoutes().get(0);
                List<LatLng> points = route.getPolyline().getDecodedPolyline();
                if (points != null) {
                    callback.success(points, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    private static String getConvertedLatLng(LatLng latLng) {
        return latLng.latitude + ", " + latLng.longitude;
    }

}
