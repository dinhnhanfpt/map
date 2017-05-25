package com.example.lehao.atmfinder;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lehao.atmfinder.model.model_direction.Leg;
import com.example.lehao.atmfinder.model.model_finder.Mlocation;
import com.example.lehao.atmfinder.untils.Xuly;
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
    private static final String ATM = "atm";
    private static final String GAS_STATION = "gas_station";

    private static final int MY_REQUEST_CODE = 1;
    List<Mlocation> arraylist = new ArrayList<>();
    private GoogleApiClient mGoogleapiclient;
    GoogleMap mMap;
    private Location mLaslocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private String mapType;

    private List<Leg> mArraylist;

    @BindView(R.id.layoutinfor)
    LinearLayout layoutinfor;

    @BindView(R.id.txtdistance)
    TextView textdistance;

    @BindView(R.id.txtduration)
    TextView textduration;

    @OnClick(R.id.btnatm)
    void setAtmClick() {
        setType(mapType = ATM);
    }

    @OnClick(R.id.btngas)
    void btnGasClick() {
        setType(mapType = GAS_STATION);
    }

    @OnClick(R.id.btndirection)
    void openDirection() {
        Intent intent = new Intent(this, ActivityDirection.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mArraylist = new ArrayList<>();

        layoutinfor.setVisibility(View.INVISIBLE);
        layoutinfor.setVisibility(LinearLayout.GONE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentmap);
        mapFragment.getMapAsync(this);

        if (mGoogleapiclient == null) {
            mGoogleapiclient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

//        createLocationRequest();
//        checkpermission();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkpermission();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings();

    }


    //kiem tra ket noi
    private void checkpermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            createLocationRequest();
//            mylocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("onRequestPermission","Top = " + String.valueOf(grantResults[0]).toString());
            mMap.setMyLocationEnabled(true);
            createLocationRequest();
//            mylocation();
        } else {
            Log.d("onRequestPermission", "Bot");
        }
    }

    double latitude, longitude;

    private void mylocation() {
        Log.d("onRequestPermission", "mylocation");
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleapiclient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLaslocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleapiclient);
            if (mLaslocation != null) {
                LatLng currentLocation = new LatLng(mLaslocation.getLatitude(), mLaslocation
                        .getLongitude());
                latitude = mLaslocation.getLatitude();
                longitude = mLaslocation.getLongitude();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

            }

        }
    }

    public void setArrayLocation(List<Mlocation> location, ProgressDialog dialog) {
        mMap.clear();

        arraylist.clear();
        arraylist.addAll(location);

        for (int i = 0; i < arraylist.size(); i++) {
            Mlocation gaslog = new Mlocation();
            gaslog = arraylist.get(i);
            Log.d(mapType, gaslog.getLng().toString() + "," + gaslog.getLat().toString());
            LatLng gas = new LatLng(gaslog.getLat(), gaslog.getLng());
            mMap.addMarker(new MarkerOptions().position(gas)
                    .title(mapType.equals(ATM) ? ATM : GAS_STATION)
                    .icon(BitmapDescriptorFactory.fromResource(mapType.equals(ATM) ? R.drawable.markeratm : R.drawable.markergas)));
        }
        dialog.dismiss();

    }

    public void setdirection(List<Leg> list) {
        this.mArraylist = list;
        Log.d("arraylist****", list.get(0).getEndAddress().toString());


    }

    //xu ly tim atm hoac gas
    private void setType(String type) {
        Log.d("Finder", type);
        Xuly xuly = new Xuly(latitude, longitude, type, this);
        xuly.execute();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleapiclient.connect();
        Log.d("Start*******", "afdasf");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleapiclient != null && mGoogleapiclient.isConnected()) {
            mGoogleapiclient.disconnect();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //  LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleapiclient, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleapiclient.isConnected() && !mLocationUpdateState) {
            startLocationUpdate();
            Log.d("Resume*******", "afdasf");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onRequestPermission", "onLocationChanged");
        mMap.clear();
        mLaslocation = location;
        mylocation();
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

    private void startLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleapiclient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
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
                Log.d("onRequestPermission", "LocationSettingsResult " + status);
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationUpdateState = true;
                        startLocationUpdate();
                        mylocation();
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
    public void onConnected(Bundle bundle) {
        mylocation();
        Log.d("connetcted*******", "afdasf");
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
