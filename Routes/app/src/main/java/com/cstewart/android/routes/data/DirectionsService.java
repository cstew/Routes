package com.cstewart.android.routes.data;

import com.cstewart.android.routes.data.model.RouteContainer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface DirectionsService {

    @GET("/directions/json")
    public void getDirections(@Query("origin") String origin,
                              @Query("destination") String destination,
                              @Query("mode") String mode,
                              Callback<RouteContainer> callback);

}
