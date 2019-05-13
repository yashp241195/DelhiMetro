package com.yash.delhimetro.DataProviders;


import android.support.annotation.NonNull;

public class FareMetro {
    private Integer fare;
    private String FromStation,ToStation;

    public FareMetro( String fromStation, String toStation,Integer fare) {
        this.fare = fare;
        FromStation = fromStation;
        ToStation = toStation;
    }


    public String getFromStation() {
        return FromStation;
    }

    public void setFromStation(String fromStation) {
        FromStation = fromStation;
    }

    public String getToStation() {
        return ToStation;
    }

    public void setToStation(String toStation) {
        ToStation = toStation;
    }

    public Integer getFare() {
        return fare;
    }

    public void setFare(Integer fare) {
        this.fare = fare;
    }

    @NonNull
    public String toString(){
        return "From Station "+getFromStation()
                +" To Station "+getToStation()
                +" fare "+getFare();
    }

}
