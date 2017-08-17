package com.example.lehao.atmfinder.untils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.lehao.atmfinder.MainActivity;
import com.example.lehao.atmfinder.model.model_finder.Geometry;
import com.example.lehao.atmfinder.model.model_finder.Googledata;
import com.example.lehao.atmfinder.model.model_finder.Mlocation;
import com.example.lehao.atmfinder.model.model_finder.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Xuly extends AsyncTask<String, Result, List<Result>> {
    private MainActivity mainActivity;
    private List<Result> arraylist;
    private Mlocation location;
    private Double lat, log;
    String type;
    ProgressDialog progressDialog;
    public Xuly(Double lat, Double log, String type, MainActivity context) {
        this.lat = lat;
        this.log = log;
        this.type = type;
        this.mainActivity = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        arraylist = new ArrayList<>();
        progressDialog = ProgressDialog.show(mainActivity,"Please wait", "Finding "+ type,true);
    }

    @Override
    protected List<Result> doInBackground(String... params) {
        Googledata googledata = null;
        try {
            Log.d("Finder", lat + " - " + log);
            googledata = XulyJson.getdata(lat, log, type);
            for (Result r : googledata.getResults()) {
                arraylist.add(r);
                publishProgress(r);
                SystemClock.sleep(100);
            }
            return arraylist;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(Result... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(List<Result> value) {
        super.onPostExecute(value);
        //Log.d("value execute",String.valueOf(value.size()));
            mainActivity.setArrayLocation(value,progressDialog,type);


    }

}




