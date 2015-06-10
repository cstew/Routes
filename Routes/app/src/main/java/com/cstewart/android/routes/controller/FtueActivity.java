package com.cstewart.android.routes.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cstewart.android.routes.R;
import com.cstewart.android.routes.RouteApplication;
import com.cstewart.android.routes.data.Preferences;
import com.cstewart.android.routes.view.FtueView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FtueActivity extends AppCompatActivity {

    @Inject Preferences mPreferences;

    private Button mNextButton;
    private ViewPager mViewPager;

    public static Intent newIntent(Context context) {
        return new Intent(context, FtueActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouteApplication.get(this).getRouteGraph().inject(this);
        setContentView(R.layout.activity_ftue);

        List<FtueItem> ftueItems = new ArrayList<>();
        ftueItems.add(new FtueItem(R.string.ftue_item_long_press, R.drawable.ftue_point));
        ftueItems.add(new FtueItem(R.string.ftue_item_points_connect, R.drawable.ftue_line));
        ftueItems.add(new FtueItem(R.string.ftue_item_undo, R.drawable.ic_action_undo));

        mNextButton = (Button) findViewById(R.id.activity_ftue_next);
        mNextButton.setOnClickListener(mOnNextClick);

        mViewPager = (ViewPager) findViewById(R.id.activity_ftue_viewpager);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setAdapter(new FtueAdapter(this, ftueItems));

        mPreferences.setHasCompletedFtue(true);
    }

    private boolean isLastPage(int position) {
        int max = mViewPager.getAdapter().getCount();
        return (position + 1) >= max;
    }

    private class FtueAdapter extends PagerAdapter {

        private Context mContext;
        private List<FtueItem> mFtueItems;

        public FtueAdapter(Context context, List<FtueItem> ftueItems) {
            mContext = context;
            mFtueItems = ftueItems;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FtueItem ftueItem = mFtueItems.get(position);
            FtueView ftueView = new FtueView(mContext);
            ftueView.setFtueItem(ftueItem);
            container.addView(ftueView);
            return ftueView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mFtueItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public static class FtueItem {

        private int mMessageResId;
        private int mImageResId;

        public FtueItem(int messageResId) {
            this(messageResId, 0);
        }

        public FtueItem(@StringRes int messageResId, @DrawableRes int imageResId) {
            mMessageResId = messageResId;
            mImageResId = imageResId;
        }

        public int getMessageResId() {
            return mMessageResId;
        }

        public int getImageResId() {
            return mImageResId;
        }
    }

    private View.OnClickListener mOnNextClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLastPage(mViewPager.getCurrentItem())) {
                finish();
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (isLastPage(position)) {
                mNextButton.setText(R.string.done);
            } else {
                mNextButton.setText(R.string.next);
            }
        }

    };

}
