package com.example.lehao.atmfinder.untils;

import android.util.Log;

import com.example.lehao.atmfinder.model.Googledata;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Le Hao on 13/05/2017.
 */

public class XulyJson  {
    public static Googledata getdata(String reques, double lat, double log) throws IOException {

        try {
            URL url = new URL(reques+lat+","+log+"&radius=500&type=atm&key=AIzaSyBTCp15F4J4Jk4qm_G_nJv7RlC_hF-rYQo");
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), "UTF-8");
            Googledata data = new Gson().fromJson(inputStreamReader, Googledata.class);
//            Log.d("**********",String.valueOf(url.toString()));
            return data;


        } catch (Exception e) {
            Log.e("loi xu ly JSON", e.toString());
        }
        return null;
    }

}
