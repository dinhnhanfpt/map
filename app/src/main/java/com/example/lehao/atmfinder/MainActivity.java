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
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lehao.atmfinder.model.Mlocation;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {
    private static final int REQUEST_CHECK_SETTING = 2;
    private static final String ATM ="atm";
    private static final String GAS_STATION ="gas_station";

    private static final int MY_REQUEST_CODE = 1;
    List<Mlocation> arraylist = new ArrayList<>();
    GoogleApiClient mGoogleapiclient;
    GoogleMap mMap;
    private Location mLaslocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private String mapType;

    @BindView(R.id.layoutmenu)
    LinearLayout layoutmenu;

    @OnClick(R.id.btnatm)
    void setAtmClick() {
        setType(mapType = ATM);
    }

    @OnClick(R.id.btngas)
    void btnGasClick() {
        setType(mapType = GAS_STATION);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        android.app.ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(R.layout.actionbar_layout);
//        TextView  txt= (TextView) findViewById(R.id.action_bar_title);
//        txt.setText("adasdfasdfasdf");

        ButterKnife.bind(this);
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

    //gan dia chi
//    private MarkerOptions placeMarker(LatLng location) {
//
//        MarkerOptions maker = new MarkerOptions().position(location);
//
//        return maker;
//    }

    //kiem tra ket noi
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

    public void setArrayLocation(List<Mlocation> location) {
        mMap.clear();

        arraylist.clear();
        arraylist.addAll(location);

        for (int i = 0; i < arraylist.size(); i++) {
        Mlocation gaslog = new Mlocation();
        gaslog = arraylist.get(i);
        Log.d(mapType, gaslog.getLng().toString() + "," + gaslog.getLat().toString());
        LatLng gas = new LatLng(gaslog.getLat(), gaslog.getLng());
        mMap.addMarker(new MarkerOptions().position(gas)
                .title(mapType.equals(ATM)?ATM:GAS_STATION)
                .icon(BitmapDescriptorFactory.fromResource(mapType.equals(ATM)?R.drawable.markeratm:R.drawable.markergas)));
    }
}

    public void setType(String type) {
        Log.d("Finder", type);
        Xuly xuly = new Xuly(latitude, longitude, type, this);
        xuly.execute();
    }

    //lay vi ti hien tai
    private void mylocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleapiclient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLaslocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleapiclient);
            if (mLaslocation != null) {
                LatLng currentLocation = new LatLng(mLaslocation.getLatitude(), mLaslocation
                        .getLongitude());
                latitude = mLaslocation.getLatitude();
                longitude = mLaslocation.getLongitude();


                //  Log.d("Main************",String.valueOf(latitude+"--"+longitude).toString());
              //  mMap.addMarker(placeMarker(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));

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
