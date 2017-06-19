package com.example.lehao.atmfinder.model.model_direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hao on 24/05/2017.
 */

public class Leg{
        @SerializedName("distance")
        @Expose
        private Distance distance;

        @SerializedName("duration")
        @Expose
        private Duration duration;

        @SerializedName("end_address")
        @Expose
        private String endAddress;

        @SerializedName("end_location")
        @Expose
        private EndLocation endLocation;

        @SerializedName("start_address")
        @Expose
        private String startAddress;

        @SerializedName("start_location")
        @Expose
        private StartLocation startLocation;
    public Leg() {
    }


    public Leg(Distance distance, Duration duration, String endAddress, EndLocation endLocation, String startAddress, StartLocation startLocation) {
        this.distance = distance;
        this.duration = duration;
        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    public Distance getDistance() {
        return distance;
    }


    public Duration getDuration() {
        return duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }



    public StartLocation getStartLocation() {
        return startLocation;
    }


}
