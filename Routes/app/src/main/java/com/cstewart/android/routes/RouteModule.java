package com.cstewart.android.routes;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteModule {

    private Context mContext;

    public RouteModule(Context context) {
        mContext = context;
    }

    @Provides Context provideAppContext() {
        return mContext;
    }

}
