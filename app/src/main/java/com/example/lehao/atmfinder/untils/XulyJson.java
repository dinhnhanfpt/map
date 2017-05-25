package com.example.lehao.atmfinder.untils;

import android.util.Log;

import com.example.lehao.atmfinder.model.model_direction.DataDirection;
import com.example.lehao.atmfinder.model.model_finder.Googledata;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Le Hao on 13/05/2017.
 */

public class XulyJson  {
     static Config config;
    public static Googledata getdata(double lat, double log,String type) throws IOException {
        config = new Config();
        try {
            URL url = new URL(config.getHttp()+lat+","+log+"&radius=500&type="+type+ config.getApikey());
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), "UTF-8");
            Googledata data = new Gson().fromJson(inputStreamReader, Googledata.class);
           Log.d("**********",String.valueOf(url.toString()));
            return data;


        } catch (Exception e) {
            Log.e("loi xu ly JSON", e.toString());
        }
        return null;
    }
    public static DataDirection getDatadirection(String start, String end){
        config = new Config();
        try {
            String mstart =URLEncoder.encode(start,"UTF-8");
            String mend = URLEncoder.encode(end,"UTF-8");
            URL url = new URL(config.httpDirection+mstart+"&destination="+mend+config.getApiDirectionkey());
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(),"UTF-8");
            DataDirection direction = new Gson().fromJson(inputStreamReader,DataDirection.class);
            Log.d("LINK DIRECTION****",url.toString());
            return direction;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

}
