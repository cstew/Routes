package com.cstewart.android.routes;

import android.location.Location;
import android.os.Bundle;

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
import com.google.android.gms.maps.model.LatLngBounds;

public class RouteMapFragment extends SupportMapFragment {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    public static RouteMapFragment newInstance() {
        return new RouteMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        LatLngBounds currentBounds = new LatLngBounds.Builder()
                .include(currentLatLng)
                .build();
        int margin = getResources().getDimensionPixelSize(R.dimen.map_margin);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(currentBounds, margin);
        mMap.animateCamera(cameraUpdate);
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
            map.setMyLocationEnabled(true);
            map.setBuildingsEnabled(true);

            zoomToLocation();
        }
    };
}
