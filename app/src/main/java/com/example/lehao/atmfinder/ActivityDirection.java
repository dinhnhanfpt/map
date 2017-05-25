package com.example.lehao.atmfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.lehao.atmfinder.untils.Direction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDirection extends AppCompatActivity {

    @BindView(R.id.txt_locationend)
    EditText locationend;
    @BindView(R.id.txt_locationstart)
    EditText locationstart;
    String mStart, mEnd;
    Direction direction;


    @OnClick(R.id.btn_direction)
     void find() {
        mStart = locationstart.getText().toString();
        mEnd = locationend.getText().toString();
        if(mStart.isEmpty()){
            locationstart.setError("required");
        }
        else if(mEnd.isEmpty()){
            locationend.setError("required");
        }
        else {
            Log.d("befor",mStart+"---"+mEnd);
             direction = new Direction(mStart,mEnd);
            direction.execute();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        ButterKnife.bind(this);

    }
}
