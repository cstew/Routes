package com.cstewart.android.routes.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class RouteActivity extends RouteSingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, RouteActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return RouteMapFragment.newInstance();
    }
}
