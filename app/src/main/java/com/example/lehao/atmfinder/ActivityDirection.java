package com.example.lehao.atmfinder;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class ActivityDirection extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    //public static final int REQUESCODE_DIRECTION = 2;
//    @BindView(R.id.txt_locationend)
//    EditText locationend;

    @BindView(R.id.txt_locationstart)
    TextView locationstart;

    PlaceAutocompleteFragment places;
    String mStart, mEnd,address;


    @OnClick(R.id.btn_direction)
    void find() {
        mStart = locationstart.getText().toString();
        //mEnd = locationend.getText().toString();
        if (mStart.isEmpty()) {
            locationstart.setText("error");
            locationstart.setTextColor(RED);
        } else if (mEnd.isEmpty()) {
            ((EditText)findViewById(R.id.place_autocomplete_search_input)).setText("Location null");
            ((EditText)findViewById(R.id.place_autocomplete_search_input)).setTextColor(RED);
        } else {
            Intent intent = new Intent();
            intent.putExtra("start", mStart);
            intent.putExtra("end", mEnd);
            Log.d("apter ***", mStart + "-" + mEnd);
            setResult(MainActivity.RESULT_OK, intent);
            finish();

        }

    }

    void complate(){
       places = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mEnd = (String) place.getName();
                Toast.makeText(getApplicationContext(),mEnd,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setTextColor(WHITE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        address= intent.getStringExtra("address");
        locationstart.setText(address);
        complate();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
