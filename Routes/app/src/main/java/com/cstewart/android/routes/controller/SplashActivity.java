package com.cstewart.android.routes.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNextActivity();
    }

    private void startNextActivity() {

        startActivity(FtueActivity.newIntent(this));

//        startActivity(RouteActivity.newIntent(this));
        finish();
    }

}
