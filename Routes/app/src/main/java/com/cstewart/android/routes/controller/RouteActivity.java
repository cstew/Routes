package com.cstewart.android.routes.controller;

import android.support.v4.app.Fragment;

public class RouteActivity extends RouteSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return RouteMapFragment.newInstance();
    }
}