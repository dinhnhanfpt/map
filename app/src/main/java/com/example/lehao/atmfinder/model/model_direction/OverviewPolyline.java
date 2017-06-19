package com.example.lehao.atmfinder.model.model_direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hao on 24/05/2017.
 */

public class OverviewPolyline {
    @SerializedName("points")
    @Expose
    private String points;
    public OverviewPolyline(){

    }
    public OverviewPolyline(String points){
        super();
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
