package com.example.lehao.atmfinder;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.lehao.atmfinder.model.Geometry;
import com.example.lehao.atmfinder.model.Googledata;
import com.example.lehao.atmfinder.model.Location;
import com.example.lehao.atmfinder.model.Result;
import com.example.lehao.atmfinder.untils.XulyJson;

import java.io.IOException;
import java.util.List;

/**
 * Created by Le Hao on 15/05/2017.
 */

//public class Xuly extends AppCompatActivity {
//    Double lat,log;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bundle bun = getIntent().getBundleExtra("bundle");
//        lat = bun.getDouble("lat");
//         log = bun.getDouble("log");
//        Toast.makeText(this,String.valueOf(lat)+String.valueOf(log),Toast.LENGTH_LONG).show();
//        Chaof cc = new Chaof();
//        cc.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
//    }
    public class Xuly extends AsyncTask<String, Result, List<Result>> {
        String _name;
        Double lat,log;
        public Xuly(Double lat, Double log){
            this.lat= lat;
            this.log = log;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Result> doInBackground(String... params) {
            String query = params[0];
            Googledata googledata=null;
            try {
                googledata = XulyJson.getdata(query,lat,log);
//                Log.d("********",String.valueOf(googledata));
                for(Result r: googledata.getResults()) {
                    Geometry geometry = r.getGeometry();
                    Location location = geometry.getLocation();
                    Log.d("*******DOINBACKGROUND",location.getLat().toString());
                    //     arrayResult.add(r);

                    publishProgress(r);
                    SystemClock.sleep(1000);

                }
                return googledata.getResults();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Result... values) {
            super.onProgressUpdate(values);
//            _name = values[0].getName();
//            Log.d("********",_name.toString());






        }

        @Override
        protected void onPostExecute(List<Result> value) {
            super.onPostExecute(value);

//            Toast.makeText(getApplicationContext(), String.valueOf(googledata),Toast.LENGTH_LONG).show();
            //Log.d("*********",value.toString());


        }


    }



