package com.cstewart.android.routes.controller;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cstewart.android.routes.R;
import com.cstewart.android.routes.RouteApplication;
import com.cstewart.android.routes.data.DirectionsManager;
import com.cstewart.android.routes.data.model.Distance;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RouteMapFragment extends SupportMapFragment {

    private static final int DEFAULT_ZOOM_LEVEL = 16;

    private static final int REQUEST_PLACE_PICKER = 1;

    @Inject DirectionsManager mDirectionsManager;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private int mMarkerDragIndex = -1;

    private List<LatLng> mMapPoints = new ArrayList<>();

    public static RouteMapFragment newInstance() {
        return new RouteMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

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

        if (mMapPoints.size() <= 0) {
            menu.removeItem(R.id.fragment_map_undo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.fragment_map_search:
                startPlacePicker();
                return true;

            case R.id.fragment_map_undo:
                undoPoint();
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.fragment_map_clear:
                clearMap();
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.fragment_map_help:
                startActivity(FtueActivity.newIntent(getActivity()));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_PLACE_PICKER:
                if (resultCode == Activity.RESULT_OK) {

                    if (data == null) {
                        Timber.i("Place picked but data is null");
                        handlePlacePickerError();
                        return;
                    }

                    Place place = PlacePicker.getPlace(data, getActivity());
                    if (place == null) {
                        Timber.i("Place picked but place not found");
                        handlePlacePickerError();
                        return;
                    }

                    clearMap();
                    LatLng placeLocation = place.getLatLng();
                    addPoint(placeLocation);
                    zoomToLatLng(place.getLatLng());

                } else {
                    Timber.i("Received an error result from places picker. Result code: " + resultCode);
                    handlePlacePickerError();
                }

                return;

        }
    }

    private void handlePlacePickerError() {
        Toast.makeText(getActivity(), R.string.search_fail, Toast.LENGTH_SHORT).show();
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
        zoomToLatLng(currentLatLng);
    }

    private void zoomToLatLng(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
        mMap.animateCamera(cameraUpdate);
    }

    private void drawPoints(List<LatLng> latLngList) {

        float distance = 0f;
        if (latLngList.size() > 1) {
            for (int i = 1; i < latLngList.size(); i++) {
                LatLng previous = latLngList.get(i - 1);
                LatLng current = latLngList.get(i);

                float[] results = new float[1];
                Location.distanceBetween(
                        previous.latitude,
                        previous.longitude,
                        current.latitude,
                        current.longitude,
                        results);
                distance += results[0];
            }
        }
        Timber.i("Distance: %f meters", distance);

        mMap.addPolyline(new PolylineOptions()
                .addAll(latLngList));
    }

    private void clearMap() {
        mMapPoints.clear();
        mMap.clear();
        updateDistance(null);
    }

    private void undoPoint() {
        if (mMapPoints.isEmpty()) {
            return;
        }

        mMap.clear();
        mMapPoints.remove(mMapPoints.size() - 1);
        drawAllPoints();
        requestRoute();
    }

    private void drawAllPoints() {
        for (LatLng latLng : mMapPoints) {
            drawPoint(latLng);
        }
    }

    private void drawPoint(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(false)
                .flat(true));
    }

    private void requestRoute() {
        if (mMapPoints.size() <= 1) {
            updateDistance(null);
            return;
        }
        AppObservable.bindFragment(RouteMapFragment.this, mDirectionsManager.getDirections(mMapPoints))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(routeOverview -> {
                            updateDistance(routeOverview.getDistance());
                            drawPoints(routeOverview.getPoints());
                        },
                        throwable -> {
                            Timber.e("Unable to get directions", throwable);
                            Toast.makeText(getActivity(), R.string.locations_error, Toast.LENGTH_SHORT).show();
                        });
    }

    private void updateDistance(Distance distance) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        String subtitle = (distance == null)? null : distance.getText();
        actionBar.setSubtitle(subtitle);
    }

    private void startPlacePicker() {
        Intent intent = null;
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                .build();
        try {
            intent = new PlacePicker.IntentBuilder()
                    .setLatLngBounds(bounds)
                    .build(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
            Timber.e("Unable to create places picker", e);
        } catch (GooglePlayServicesNotAvailableException e) {
            Timber.e("Unable to create places picker", e);
        }

        if (intent == null) {
            return;
        }

        startActivityForResult(intent, REQUEST_PLACE_PICKER);
    }

    private void addPoint(LatLng latLng) {
        mMapPoints.add(latLng);
        drawPoint(latLng);

        requestRoute();
        getActivity().invalidateOptionsMenu();
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
            mMap.setOnMarkerDragListener(mOnMarkerDragListener);
            map.setMyLocationEnabled(true);
            map.setBuildingsEnabled(true);

            zoomToLocation();
        }
    };

    private GoogleMap.OnMapLongClickListener mOnMapLongClickListener = latLng -> {
        addPoint(latLng);
    };

    private GoogleMap.OnMarkerDragListener mOnMarkerDragListener = new GoogleMap.OnMarkerDragListener() {

        @Override
        public void onMarkerDragStart(Marker marker) {
            LatLng update = marker.getPosition();
            mMarkerDragIndex = mMapPoints.indexOf(update);
            Timber.i("Marker index: " + mMarkerDragIndex);
        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            if (mMarkerDragIndex < 0) {
                return;
            }

            mMapPoints.set(mMarkerDragIndex, marker.getPosition());
            mMap.clear();
            drawAllPoints();
            requestRoute();
        }
    };

}
