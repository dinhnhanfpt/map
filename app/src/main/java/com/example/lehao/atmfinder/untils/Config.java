package com.example.lehao.atmfinder.untils;

/**
 * Created by Le Hao on 15/05/2017.
 */

public class Config {
    String apikey="&key=AIzaSyC_sZGTTdDE-w_dTz3vbrEIfSXMMg6ePA4";
    String http ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    String httpDirection = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    String apiDirectionkey ="&key=AIzaSyBW5DRWdPi6fKz7pXL_YJvyN1vYLbG4Vjo";
    public Config(){}
    public Config(String link, String apikey, String httpDirection, String apiDirectionkey){
        this.http = link;
        this.apikey = apikey;
        this.httpDirection = httpDirection;
        this.apiDirectionkey = apiDirectionkey;
    }

    public String getHttpDirection() {
        return httpDirection;
    }

    public String getApiDirectionkey() {
        return apiDirectionkey;
    }

    public String getApikey() {
        return apikey;
    }

    public String getHttp() {
        return http;
    }
}
