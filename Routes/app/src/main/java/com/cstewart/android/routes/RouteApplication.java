package com.cstewart.android.routes;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class RouteApplication extends Application {

    private RouteGraph mRouteGraph;

    public static RouteApplication get(Context context) {
        return (RouteApplication) context.getApplicationContext();
    }

    public RouteGraph getRouteGraph() {
        return mRouteGraph;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        mRouteGraph = DaggerRouteGraph.builder()
                .routeModule(new RouteModule(this))
                .build();
    }
}
