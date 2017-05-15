package com.example.lehao.atmfinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hao on 13/05/2017.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;
    public Geometry() {
    }

    public Geometry(Location location) {
        super();
        this.location = location;

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
