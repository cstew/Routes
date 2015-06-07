package com.cstewart.android.routes;

import com.cstewart.android.routes.controller.RouteMapFragment;
import com.cstewart.android.routes.controller.SplashActivity;
import com.cstewart.android.routes.data.DirectionsManager;
import com.cstewart.android.routes.data.Preferences;

import dagger.Component;

@Component(modules = RouteModule.class)
public interface RouteGraph {

    DirectionsManager directionsManager();
    Preferences preferences();

    public void inject(RouteMapFragment routeMapFragment);
    public void inject(SplashActivity splashActivity);
}
