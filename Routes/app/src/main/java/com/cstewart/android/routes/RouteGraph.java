package com.cstewart.android.routes;

import android.content.Context;

import com.cstewart.android.routes.controller.RouteMapFragment;

import dagger.Component;

@Component(modules = RouteModule.class)
public interface RouteGraph {
    Context appContext();

    public void inject(RouteMapFragment routeMapFragment);
}
