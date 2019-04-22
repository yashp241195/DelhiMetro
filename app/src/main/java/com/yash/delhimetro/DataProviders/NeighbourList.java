package com.yash.delhimetro.DataProviders;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class NeighbourList {

    private int timeInterval;
    private String line;
    private ArrayList<String> stationList = new ArrayList<>();

    public NeighbourList(){

    }


    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public ArrayList<String> getStationList() {
        return stationList;
    }

    public void setStationList(ArrayList<String> stationList) {
        this.stationList = stationList;
    }

    @NonNull
    public String toString(){
        return " timeInterval : "+timeInterval+
                " line : "+line
                +" stationList : "+stationList.toString();
    }

}
