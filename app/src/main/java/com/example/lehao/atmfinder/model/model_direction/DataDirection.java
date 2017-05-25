package com.example.lehao.atmfinder.model.model_direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Le Hao on 24/05/2017.
 */

public class DataDirection {
    @SerializedName("routes")
    @Expose
    private List<Routes> routes;
    public DataDirection(){

    }
    public DataDirection(List<Routes> routes) {
        super();
        this.routes = routes;
    }
    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {

        this.routes = routes;
    }
}
