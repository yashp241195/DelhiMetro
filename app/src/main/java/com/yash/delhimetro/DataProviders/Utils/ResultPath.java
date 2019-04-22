package com.yash.delhimetro.DataProviders.Utils;

import java.util.ArrayList;

public class ResultPath{

    private Integer timeTaken;
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
}
