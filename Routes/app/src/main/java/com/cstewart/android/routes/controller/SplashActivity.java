package com.cstewart.android.routes.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cstewart.android.routes.RouteApplication;
import com.cstewart.android.routes.data.Preferences;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    @Inject Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RouteApplication.get(this).getRouteGraph().inject(this);
        startNextActivity();
    }

    private void startNextActivity() {

        Intent routeIntent = RouteActivity.newIntent(this);

        if (mPreferences.hasCompletedFtue()) {
            startActivity(routeIntent);
        } else {
            Intent[] ftueIntents = {
                    routeIntent,
                    FtueActivity.newIntent(this)
            };
            startActivities(ftueIntents);
        }

        finish();
    }

}
