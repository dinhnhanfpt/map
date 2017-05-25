package com.example.lehao.atmfinder.untils;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.lehao.atmfinder.MainActivity;
import com.example.lehao.atmfinder.model.model_direction.DataDirection;
import com.example.lehao.atmfinder.model.model_direction.Leg;
import com.example.lehao.atmfinder.model.model_direction.Routes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Hao on 21/05/2017.
 */

public class Direction extends AsyncTask<String, Leg, List<Leg>> {
    String mStart, mEnd;
    List<Leg> list;
    MainActivity mainactivity;

    public Direction(String start, String end){
        this.mStart = start;
        this.mEnd = end;
        mainactivity  = new MainActivity();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        list = new ArrayList<>();

    }

    @Override
    protected List<Leg> doInBackground(String... params) {
        DataDirection dataDirection = XulyJson.getDatadirection(mStart,mEnd);
        for (Routes r: dataDirection.getRoutes()){
            for (Leg leg:r.getLegs()){
                list.add(leg);
                publishProgress(leg);
                SystemClock.sleep(100);
            }

            return list;
        }
        return null;
    }



    @Override
    protected void onProgressUpdate(Leg... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Leg> leg) {
        super.onPostExecute(leg);
        Log.d("OnpostExcute",leg.get(0).getEndAddress().toString());
        mainactivity.setdirection(leg);

    }
}
