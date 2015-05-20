package com.cstewart.android.routes;

import com.cstewart.android.routes.controller.RouteMapFragment;
import com.cstewart.android.routes.data.DirectionsManager;

import dagger.Component;

@Component(modules = RouteModule.class)
public interface RouteGraph {

    DirectionsManager directionsManager();

    public void inject(RouteMapFragment routeMapFragment);
}
