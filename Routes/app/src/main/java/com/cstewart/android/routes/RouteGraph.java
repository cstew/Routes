package com.cstewart.android.routes;

import com.cstewart.android.routes.controller.RouteMapFragment;
import com.cstewart.android.routes.data.DirectionsService;

import dagger.Component;

@Component(modules = RouteModule.class)
public interface RouteGraph {

    DirectionsService directionsService();

    public void inject(RouteMapFragment routeMapFragment);
}
