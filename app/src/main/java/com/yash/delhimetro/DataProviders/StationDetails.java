package com.yash.delhimetro.DataProviders;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class StationDetails {
    private String StationName;
    private Boolean hasToilet,hasParking,hasNearbyMall;
    private ArrayList<String> line = new ArrayList<>();

    public StationDetails(){

    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public Boolean getHasToilet() {
        return hasToilet;
    }

    public void setHasToilet(Boolean hasToilet) {
        this.hasToilet = hasToilet;
    }

    public Boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking;
    }

    @NonNull
    public String toString(){

        return " Name : "+StationName+" hasToilet : "+hasToilet
                +" hasParking :"+hasParking+" line : "+line.toString()+" ";
    }

    public Boolean getHasNearbyMall() {
        return hasNearbyMall;
    }

    public void setHasNearbyMall(Boolean hasNearbyMall) {
        this.hasNearbyMall = hasNearbyMall;
    }

    public ArrayList<String> getLine() {
        return line;
    }

    public void setLine(ArrayList<String> line) {
        this.line = line;
    }
}
