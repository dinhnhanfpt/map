package com.example.lehao.atmfinder;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.lehao.atmfinder.model.Geometry;
import com.example.lehao.atmfinder.model.Googledata;
import com.example.lehao.atmfinder.model.Mlocation;
import com.example.lehao.atmfinder.model.Result;
import com.example.lehao.atmfinder.untils.XulyJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Xuly extends AsyncTask<String, Mlocation, List<Mlocation>> {
    private MainActivity mainActivity;
    private List<Mlocation> arraylist;
    private String _name;
    private Mlocation location;
    private Double lat, log;
    String type;

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
    }

    @Override
    protected List<Mlocation> doInBackground(String... params) {
//        String query = params[0];
        Googledata googledata = null;
        try {
            Log.d("Finder", lat + " - " + log);
            googledata = XulyJson.getdata(lat, log, type);
            for (Result r : googledata.getResults()) {
                Geometry geometry = r.getGeometry();
                location = geometry.getLocation();
                arraylist.add(location);

                publishProgress(location);
                SystemClock.sleep(100);
            }
            return arraylist;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(Mlocation... values) {
        super.onProgressUpdate(values);
        //Log.d("ad**********",values[0].getLat().toString());


    }

    @Override
    protected void onPostExecute(List<Mlocation> value) {
        super.onPostExecute(value);
        //Log.d("value execute",String.valueOf(value.size()));
        if (value.size() == 0) {
            final AlertDialog.Builder aleart = new AlertDialog.Builder(mainActivity);
            aleart.setTitle("WARNING !");
            aleart.setMessage("Can't find recent " + type);
            aleart.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            aleart.show();
        } else {

            mainActivity.setArrayLocation(value);
        }

    }

}




