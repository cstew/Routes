package com.cstewart.android.routes.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cstewart.android.routes.R;

public class FtueActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, FtueActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftue);
    }

}
