package com.cstewart.android.routes.controller;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cstewart.android.routes.R;
import com.cstewart.android.routes.RouteApplication;
import com.cstewart.android.routes.data.DirectionsManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RouteMapFragment extends SupportMapFragment {
    private static final String TAG = RouteMapFragment.class.getSimpleName();

    private static final int DEFAULT_ZOOM_LEVEL = 16;

    @Inject DirectionsManager mDirectionsManager;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    private List<LatLng> mMapPoints = new ArrayList<>();

    public static RouteMapFragment newInstance() {
        return new RouteMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        RouteApplication.get(getActivity()).getRouteGraph().inject(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .build();

        getMapAsync(mOnMapReadyCallback);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.fragment_map_clear:
                clearMap();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, mLocationListener);
    }

    private void zoomToLocation() {
        if (mLocation == null || mMap == null) {
            return;
        }

        LatLng currentLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM_LEVEL);
        mMap.animateCamera(cameraUpdate);
    }

    private void drawPoints(List<LatLng> latLngList) {
        mMap.addPolyline(new PolylineOptions()
                .addAll(latLngList));
    }

    private void clearMap() {
        mMapPoints.clear();
        mMap.clear();
    }

    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            findLocation();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            zoomToLocation();
        }
    };

    private OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap map) {
            mMap = map;
            mMap.setOnMapLongClickListener(mOnMapLongClickListener);
            map.setMyLocationEnabled(true);
            map.setBuildingsEnabled(true);

            zoomToLocation();
        }
    };

    private GoogleMap.OnMapLongClickListener mOnMapLongClickListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            mMapPoints.add(latLng);
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .flat(true));

            if (mMapPoints.size() <= 1) {
                return;
            }
            AppObservable.bindFragment(RouteMapFragment.this, mDirectionsManager.getDirections(mMapPoints))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(latLngs -> drawPoints(latLngs),
                            throwable -> {
                                Log.e(TAG, "Unable to get directions", throwable);
                                Toast.makeText(getActivity(), "Unable to get directions.", Toast.LENGTH_SHORT).show();
                            });
        }
    };


}
