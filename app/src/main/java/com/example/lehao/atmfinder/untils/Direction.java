package com.example.lehao.atmfinder.untils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.lehao.atmfinder.MainActivity;
import com.example.lehao.atmfinder.model.model_direction.DataDirection;
import com.example.lehao.atmfinder.model.model_direction.Routes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Hao on 21/05/2017.
 */

public class Direction extends AsyncTask<String, Routes, List<Routes>> {
    String mStart, mEnd;
    List<Routes> list;
    MainActivity mainActivity;
    ProgressDialog progressDialog;

    public Direction(String start, String end, MainActivity mainActivity) {
        this.mStart = start;
        this.mEnd = end;
        this.mainActivity = mainActivity;



    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        list = new ArrayList<>();
         progressDialog = ProgressDialog.show(mainActivity,"Please wait", "Finding...!",true);

    }

    @Override
    protected List<Routes> doInBackground(String... params) {
        DataDirection dataDirection = XulyJson.getDatadirection(mStart, mEnd);
        for (Routes r :dataDirection.getRoutes()) {
            list.add(r);
            return list;
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Routes... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Routes> routes) {
        super.onPostExecute(routes);
        mainActivity.setdirection(routes,progressDialog);

    }
}
