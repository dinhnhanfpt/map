package com.example.lehao.atmfinder.model.model_finder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Le Hao on 14/05/2017.
 */

public class Googledata {
    @SerializedName("results")
    @Expose
    private List<Result> results;
    public Googledata() {

    }

    public Googledata(List<Result> results) {
        super();
        this.results = results;
    }
    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
