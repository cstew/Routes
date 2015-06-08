package com.cstewart.android.routes.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cstewart.android.routes.R;

public class FtueActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    public static Intent newIntent(Context context) {
        return new Intent(context, FtueActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftue);

        mViewPager = (ViewPager) findViewById(R.id.activity_ftue_viewpager);
        mViewPager.setAdapter(new FtueAdapter());
    }

    private class FtueAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }

}
