package com.example.lehao.atmfinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.example.lehao.atmfinder.model.model_direction.Distance;
import com.example.lehao.atmfinder.model.model_direction.Duration;
import com.example.lehao.atmfinder.model.model_direction.EndLocation;
import com.example.lehao.atmfinder.model.model_direction.Leg;
import com.example.lehao.atmfinder.model.model_direction.OverviewPolyline;
import com.example.lehao.atmfinder.model.model_direction.Routes;
import com.example.lehao.atmfinder.model.model_direction.StartLocation;
import com.example.lehao.atmfinder.model.model_finder.Mlocation;
import com.example.lehao.atmfinder.untils.Direction;
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
import com.google.android.gms.maps.model.PolylineOptions;

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
    private static final int REQUES_CODE_DIRECTION = 3;

    private GoogleApiClient mGoogleapiclient;
    GoogleMap mMap;

    private Location mLaslocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private String mapType;

    List<Routes> mArraylist = new ArrayList<>();
    List<Mlocation> arraylist = new ArrayList<>();
    List<Leg> legs = new ArrayList<>();
    List<LatLng> decoded;
    String end, start, distance, txtduration;
    LatLng currentLocation;

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
        startActivityForResult(intent, REQUES_CODE_DIRECTION);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
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
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("onRequestPermission", "Top = " + String.valueOf(grantResults[0]).toString());
            mMap.setMyLocationEnabled(true);
            createLocationRequest();
        } else {
            Log.d("onRequestPermission", "Bot");
        }
    }

    double latitude, longitude;

    //vi tri hien tai
    private void mylocation() {
        Log.d("onRequestPermission", "mylocation");
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleapiclient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLaslocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleapiclient);
            if (mLaslocation != null) {
                 currentLocation = new LatLng(mLaslocation.getLatitude(), mLaslocation
                        .getLongitude());
                latitude = mLaslocation.getLatitude();
                longitude = mLaslocation.getLongitude();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

            }

        }
    }

    //set marker ATM | GASSTATION
    public void setArrayLocation(List<Mlocation> location, ProgressDialog dialog) {
        dialog.dismiss();
        Log.d("arraylocation",String.valueOf(location.size()));
        if(location.size()==0){
            AlertDialog.Builder db = new AlertDialog.Builder(this);
            db.setTitle("Tim kiem");
            db.setMessage("Khong co "+mapType+" nao gan day");
            db.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int arg1) {
                    d.cancel();
                };
            });
            AlertDialog alertDialog = db.create();
            alertDialog.show();
        }
        mMap.clear();
        arraylist.clear();
        arraylist.addAll(location);

        for (int i = 0; i < arraylist.size(); i++) {
            Mlocation gaslog = new Mlocation();
            gaslog = arraylist.get(i);
            LatLng gas = new LatLng(gaslog.getLat(), gaslog.getLng());
            mMap.addMarker(new MarkerOptions().position(gas)
                    .title(mapType.equals(ATM) ? ATM : GAS_STATION)
                    .icon(BitmapDescriptorFactory.fromResource(mapType.equals(ATM) ? R.drawable.markeratm : R.drawable.markergas)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,16));
        }



    }

    public void getLocationDirection() {
        Direction direction = new Direction(start, end, this);
        direction.execute();

    }

    //lay list data Leg
    public void setdirection(List<Routes> list, ProgressDialog progressDialog) {
        mMap.clear();
        if(mArraylist!=null && decoded!=null) {
            Log.d("IF***","if");
            mArraylist.clear();
            decoded.clear();
            mArraylist.addAll(list);
            setpoly();
            progressDialog.cancel();

        }
        else {
            Log.d("ELSE***","else");

            mArraylist.addAll(list);
            setpoly();
            progressDialog.cancel();

        }

    }
    private void setpoly(){
        Routes routes = new Routes();
        routes = mArraylist.get(0);
        legs = routes.getLegs();

        OverviewPolyline overviewPolyline = new OverviewPolyline();
        overviewPolyline = routes.getOverviewPolyline();
        setMakerdirection();
        decodePolyLine(overviewPolyline.getPoints());
        formatPolyline(overviewPolyline);

    }

    //set markerdirection
    private void setMakerdirection() {
        layoutinfor.setVisibility(LinearLayout.GONE);
        layoutinfor.setVisibility(View.VISIBLE);
        for (int j = 0; j < legs.size(); j++) {
            Leg leg = new Leg();
            leg = legs.get(j);

            Duration duration = new Duration();
            duration = leg.getDuration();
            textduration.setText(duration.getText());

            Distance distance = new Distance();
            distance = leg.getDistance();
            textdistance.setText(distance.getText());

            StartLocation startLocation = new StartLocation();
            startLocation = leg.getStartLocation();
            LatLng Ln = new LatLng(startLocation.getLat(), startLocation.getLng());

            EndLocation endLocation = new EndLocation();
            endLocation = leg.getEndLocation();
            LatLng Eln = new LatLng(endLocation.getLat(), endLocation.getLng());

            mMap.addMarker(new MarkerOptions().position(Ln).title("Start").icon(BitmapDescriptorFactory.
                    fromResource(R.drawable.startlocation)));

            mMap.addMarker(new MarkerOptions().position(Eln).title("Finish").icon(BitmapDescriptorFactory.fromResource(R.drawable.endloca)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Eln, 15));
            Log.d("dia chi***", Ln.latitude + "," + Ln.longitude);


        }
    }

    //giai code polyline
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;

    }

    //dinh dang duong ve
    private void formatPolyline(OverviewPolyline overviewPolyline) {

        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.RED).
                width(10);
        for (int i = 0; i < decoded.size(); i++) {
            polylineOptions.add(decoded.get(i));
            mMap.addPolyline(polylineOptions);
        }

    }

    //xu ly tim atm hoac gas
    private void setType(String type) {
        layoutinfor.setVisibility(View.INVISIBLE);
        layoutinfor.setVisibility(LinearLayout.GONE);
        Log.d("Finder", type);
        Xuly xuly = new Xuly(latitude, longitude, type, this);
        xuly.execute();
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
    protected void onPause() {
        super.onPause();
        //  LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleapiclient, this);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTING) {
            if (resultCode == RESULT_OK) {
                mLocationUpdateState = true;
                startLocationUpdate();

            }
        }
        if (requestCode == REQUES_CODE_DIRECTION) {
            if (resultCode == RESULT_OK) {
                start = data.getStringExtra("start");
                end = data.getStringExtra("end");
                Log.d("intent****", start + "-" + end);
                mylocation();
                getLocationDirection();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }


    @Override
    public void onConnected(Bundle bundle) {
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
