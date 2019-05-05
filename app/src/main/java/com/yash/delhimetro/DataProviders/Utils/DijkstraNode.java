package com.yash.delhimetro.DataProviders.Utils;

import android.support.annotation.NonNull;

public class DijkstraNode implements Comparable<DijkstraNode> {

    private String currentStation, parentStation;
    private Integer minTimeFromSource;

    @Override
    public int compareTo(@NonNull DijkstraNode o) {
        return minTimeFromSource.compareTo(o.getMinTimeFromSource());
    }

    public String getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(String currentStation) {
        this.currentStation = currentStation;
    }

    public String getParentStation() {
        return parentStation;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }

    public Integer getMinTimeFromSource() {
        return minTimeFromSource;
    }

    public void setMinTimeFromSource(Integer minTimeFromSource) {
        this.minTimeFromSource = minTimeFromSource;
    }

    @NonNull
    public String toString(){

        return "Current : "+currentStation+
                " Parent : "+parentStation+
                " minTime : "+minTimeFromSource;

    }
}
