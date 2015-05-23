package com.cstewart.android.routes.data;

import com.cstewart.android.routes.data.model.RouteContainer;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface DirectionsService {

    @GET("/directions/json")
    Observable<RouteContainer> getDirections(@Query("origin") String origin,
                              @Query("destination") String destination,
                              @Query("waypoints") String waypoints,
                              @Query("mode") String mode);

}
