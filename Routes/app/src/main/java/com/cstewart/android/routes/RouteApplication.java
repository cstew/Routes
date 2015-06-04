package com.cstewart.android.routes;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
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
        Fabric.with(this, new Crashlytics());

        Timber.plant(new Timber.DebugTree());

        mRouteGraph = DaggerRouteGraph.builder()
                .routeModule(new RouteModule(this))
                .build();
    }
}
