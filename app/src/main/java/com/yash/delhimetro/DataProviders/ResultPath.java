package com.yash.delhimetro.DataProviders;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ResultPath implements Comparable<ResultPath>{

    private Integer timeTaken, SwitchCount = 0, fare = 0, stops = 0, parkingCount = 0;
    private ArrayList<String> StationList, SummaryList;


    public ResultPath(){

    }


    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    public ArrayList<String> getStationList() {
        return StationList;
    }

    public void setStationList(ArrayList<String> stationList) {
        StationList = stationList;
    }

    // sort by {Time -> Switch Count -> stationList}

    @Override
    public int compareTo(@NonNull ResultPath o) {
        // sort by time taken
        int val1 = timeTaken.compareTo(o.timeTaken);
        // sort by switch count
        int val2 = SwitchCount.compareTo(o.SwitchCount);
        // sort by switch count
        int val3 = StationList.toString().compareTo(o.StationList.toString());

        if(val1 == 0){
            if(val2 == 0){
                    return val3;
            }
            return val2;
        }

        return val1;
    }

    public boolean equals(ResultPath rp){
        return timeTaken.equals(rp.getTimeTaken())
                && StationList.equals(rp.getStationList());
    }

    @NonNull
    public String toString(){

        return "time : "+timeTaken+"" +
                " switches : "+SwitchCount
                +" summaryList : "+SummaryList
                +" stationList : "+StationList;
    }

    public Integer getSwitchCount() {
        return SwitchCount;
    }

    public void setSwitchCount(Integer switchCount) {
        SwitchCount = switchCount;
    }

    public ArrayList<String> getSummaryList() {
        return SummaryList;
    }

    public void setSummaryList(ArrayList<String> summaryList) {
        SummaryList = summaryList;
    }


    public Integer getFare() {
        return fare;
    }

    public void setFare(Integer fare) {
        this.fare = fare;
    }

    public Integer getStops() {
        return stops;
    }

    public void setStops(Integer stops) {
        this.stops = stops;
    }

    public Integer getParkingCount() {
        return parkingCount;
    }

    public void setParkingCount(Integer parkingCount) {
        this.parkingCount = parkingCount;
    }
}
