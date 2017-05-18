package com.example.lehao.atmfinder.model;

/**
 * Created by Le Hao on 15/05/2017.
 */

public class Confix {
    String apikey="&key=AIzaSyBTCp15F4J4Jk4qm_G_nJv7RlC_hF-rYQo";
    String http ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public Confix(){}
    public Confix(String link,String apikey){
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
