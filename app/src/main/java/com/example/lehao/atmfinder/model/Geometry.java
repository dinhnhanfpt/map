package com.example.lehao.atmfinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hao on 13/05/2017.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    private Mlocation location;
    public Geometry() {
    }

    public Geometry(Mlocation location) {
        super();
        this.location = location;

    }

    public Mlocation getLocation() {
        return location;
    }

    public void setLocation(Mlocation location) {
        this.location = location;
    }

}
