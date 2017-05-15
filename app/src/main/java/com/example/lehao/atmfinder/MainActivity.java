package com.example.lehao.atmfinder;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {
    private static final int MY_REQUEST_CODE = 1;
    GoogleApiClient mGoogleapiclient;
    GoogleMap mMap;
    private Location mLaslocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private static final int REQUEST_CHECK_SETTING = 2;

    @BindView(R.id.layoutmenu)
    LinearLayout layoutmenu;

    @BindView(R.id.btnatm)
    ImageView btnatm;
    @BindView(R.id.btngas)
    ImageView btngas;

    @OnClick(R.id.btngas)
    void btngasclick() {


    }

    @OnClick(R.id.btnatm)
    void btnatmclick() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentmap);
        mapFragment.getMapAsync(this);


        if (mGoogleapiclient == null) {
            mGoogleapiclient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                    .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                    .build();

        }
        createLocationRequest();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mylocation();
        mMap.getUiSettings();
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true);
    }

    private MarkerOptions placeMarker(LatLng location) {

        MarkerOptions maker = new MarkerOptions().position(location);


        return maker;
    }


    private void checkpermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_CODE);
            return;
        } else {

        }
        mMap.setMyLocationEnabled(true);
    }

    double latitude, longitude;

    private void mylocation() {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleapiclient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLaslocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleapiclient);
            if (mLaslocation != null) {
                LatLng currentLocation = new LatLng(mLaslocation.getLatitude(), mLaslocation
                        .getLongitude());
                //add pin at user's location
                latitude = mLaslocation.getLatitude();
                longitude = mLaslocation.getLongitude();
               // show();


//                placeMarker(currentLocation);
                mMap.addMarker(placeMarker(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
            }
        }
    }

    private void startLocationUpdate() {
        checkpermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleapiclient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // 2
        mLocationRequest.setInterval(10000);
        // 3
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleapiclient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    // 4
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationUpdateState = true;
                        startLocationUpdate();
                        break;
                    // 5
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTING);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    // 6
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleapiclient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleapiclient != null && mGoogleapiclient.isConnected()) {
            mGoogleapiclient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTING) {
            if (resultCode == RESULT_OK) {
                mLocationUpdateState = true;
                startLocationUpdate();


            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleapiclient, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleapiclient.isConnected() && !mLocationUpdateState) {
            startLocationUpdate();

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        mLaslocation = location;
        mylocation();
        Xuly xyly = new Xuly(latitude,longitude);
        xyly.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");


//            placeMarker(new LatLng(mLaslocation.getLatitude(), mLaslocation.getLongitude()));
//       // startLocationUpdate();

    }

    @Override
    public void onConnected(Bundle bundle) {
        checkpermission();
        mylocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }
}
