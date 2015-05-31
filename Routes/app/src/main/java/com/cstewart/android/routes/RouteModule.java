package com.cstewart.android.routes;

import android.content.Context;

import com.cstewart.android.routes.data.DirectionsManager;
import com.cstewart.android.routes.data.DirectionsService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import timber.log.Timber;

@Module
public class RouteModule {

    private Context mContext;

    public RouteModule(Context context) {
        mContext = context;
    }

    @Provides DirectionsManager provideDirectionsManager(DirectionsService directionsService) {
        return new DirectionsManager(directionsService);
    }

    @Provides DirectionsService provideDirectionsService(RestAdapter restAdapter) {
        return restAdapter.create(DirectionsService.class);
    }

    @Provides RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com/maps/api")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(message -> Timber.d(message))
                .setRequestInterceptor(request -> request.addQueryParam("key", BuildConfig.DIRECTIONS_API_KEY))
                .build();
    }

    @Provides Context provideAppContext() {
        return mContext;
    }

}
