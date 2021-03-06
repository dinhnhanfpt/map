package com.example.lehao.atmfinder.model.model_direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hao on 21/05/2017.
 */


public class EndLocation {

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lng")
    @Expose
    private Double lng;

    public EndLocation() {
    }
    public EndLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
