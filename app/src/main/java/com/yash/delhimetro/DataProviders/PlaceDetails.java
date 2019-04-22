package com.yash.delhimetro.DataProviders;

import android.support.annotation.NonNull;

public class PlaceDetails  {

    private String placeName, nearbyMetroStation, placeType,placeImage;
    private float distance;

    public PlaceDetails(){

    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getNearbyMetroStation() {
        return nearbyMetroStation;
    }

    public void setNearbyMetroStation(String nearbyMetroStation) {
        this.nearbyMetroStation = nearbyMetroStation;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @NonNull
    public String toString(){

        return " Name : "+placeName+" nearby Metro Station : "
                +nearbyMetroStation+" placeType :"+placeType
                +" distance "+distance+" km ";
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }
}