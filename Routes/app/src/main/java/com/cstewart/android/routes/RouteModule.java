package com.cstewart.android.routes;

import android.content.Context;

import com.cstewart.android.routes.data.DirectionsService;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

@Module
public class RouteModule {

    private Context mContext;

    public RouteModule(Context context) {
        mContext = context;
    }

    @Provides DirectionsService provideDirectionsService(RestAdapter restAdapter) {
        return restAdapter.create(DirectionsService.class);
    }

    @Provides RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint("http://maps.googleapis.com/maps/api")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("key", BuildConfig.DIRECTIONS_API_KEY);
                    }
                })
                .build();
    }

    @Provides Context provideAppContext() {
        return mContext;
    }

}