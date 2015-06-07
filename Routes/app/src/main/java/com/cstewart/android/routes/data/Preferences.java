package com.cstewart.android.routes.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String HAS_SEEN_FTUE = "HasSeenFtue";

    private Context mContext;

    public Preferences(Context context) {
        mContext = context;
    }

    public boolean hasCompletedFtue() {
        return getSharedPreferences().getBoolean(HAS_SEEN_FTUE, false);
    }

    public void setHasCompletedFtue(boolean hasCompletedFtue) {
        getSharedPreferences()
                .edit()
                .putBoolean(HAS_SEEN_FTUE, hasCompletedFtue)
                .apply();
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences("routes", Context.MODE_PRIVATE);
    }
}
