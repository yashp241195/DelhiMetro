package com.yash.delhimetro.DataProviders.Utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ResultPath implements Comparable<ResultPath>{

    private Integer timeTaken,SwitchCount=0;
    private ArrayList<String> StationList;


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
                +" stationList : "+StationList;
    }

    public Integer getSwitchCount() {
        return SwitchCount;
    }

    public void setSwitchCount(Integer switchCount) {
        SwitchCount = switchCount;
    }
}
