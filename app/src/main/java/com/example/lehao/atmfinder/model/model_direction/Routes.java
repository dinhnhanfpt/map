package com.example.lehao.atmfinder.model.model_direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Le Hao on 21/05/2017.
 */

public class Routes {

    @SerializedName("legs")
    @Expose
    private List<Leg> legs;

    @SerializedName("overview_polyline")
    @Expose
    private OverviewPolyline overviewPolyline;

    public Routes() {
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public Routes(List<Leg> legs, OverviewPolyline overviewPolyline) {
        this.legs = legs;
        this.overviewPolyline = overviewPolyline;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }


}
