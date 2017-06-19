package com.example.lehao.atmfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDirection extends AppCompatActivity {
    public static final int REQUESCODE_DIRECTION = 2;
    @BindView(R.id.txt_locationend)
    EditText locationend;
    @BindView(R.id.txt_locationstart)
    EditText locationstart;
    String mStart, mEnd;


    @OnClick(R.id.btn_direction)
    void find() {
        mStart = locationstart.getText().toString();
        mEnd = locationend.getText().toString();
        if (mStart.isEmpty()) {
            locationstart.setError("required");
        } else if (mEnd.isEmpty()) {
            locationend.setError("required");
        } else {
            Intent intent = new Intent();
            intent.putExtra("start", mStart);
            intent.putExtra("end", mEnd);
            Log.d("apter ***",mStart+"-"+mEnd);
            setResult(MainActivity.RESULT_OK, intent);
            finish();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        ButterKnife.bind(this);

    }
}
