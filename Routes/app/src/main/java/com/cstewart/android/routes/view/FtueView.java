package com.cstewart.android.routes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cstewart.android.routes.R;
import com.cstewart.android.routes.controller.FtueActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FtueView extends FrameLayout {

    @InjectView(R.id.view_ftue_message)
    TextView mMessageTextView;

    @InjectView(R.id.view_ftue_image)
    ImageView mImageView;

    private FtueActivity.FtueItem mFtueItem;

    public FtueView(Context context) {
        this(context, null);
    }

    public FtueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        updateUI();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_ftue, this);
        ButterKnife.inject(this);
    }

    private void updateUI() {

        if (mFtueItem == null) {
            mMessageTextView.setText("");
            return;
        }

        mMessageTextView.setText(mFtueItem.getMessageResId());
        mImageView.setImageResource(mFtueItem.getImageResId());
    }

    public void setFtueItem(FtueActivity.FtueItem ftueItem) {
        mFtueItem = ftueItem;
        updateUI();
    }
}
