package com.example.lehao.atmfinder.model;

/**
 * Created by Le Hao on 15/05/2017.
 */

public class Config {
    String apikey="&key=AIzaSyC_sZGTTdDE-w_dTz3vbrEIfSXMMg6ePA4";
    String http ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public Config(){}
    public Config(String link, String apikey){
        this.http = link;
        this.apikey = apikey;
    }

    public String getApikey() {
        return apikey;
    }

    public String getHttp() {
        return http;
    }
}
