package com.yash.delhimetro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.DataProviders.Utils.ResultPath;
import com.yash.delhimetro.DataProviders.Utils.UtilsGateway;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Explore extends AppCompatActivity {

    private String FromStation,ToStation,ToPlace;

    private HashMap<String,Integer> hashMapFare = new HashMap<>();
    private ArrayList<StationDetails> stationDetailsArrayList = new ArrayList<>();
    private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();
    private HashMap<String,Integer> nameToIndexStation = new HashMap<>();
    private ArrayList<NeighbourList> neighbourListArrayList = new ArrayList<>();

    private static final String MY_PREFS_NAME = "Loaded Data";

    private ArrayList<String> stationNameArrayList = new ArrayList<>();
    private ArrayList<String> placeNameArrayList = new ArrayList<>();

    private UtilsGateway utilsGateway;
    // results from utility

    private ArrayList<String> NearbyToiletStations = new ArrayList<>();
    private ArrayList<String> NearbyParkingStations = new ArrayList<>();

    ArrayList<ResultPath> resultPaths;

    private int optId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button



        new Handler().postDelayed(new Runnable() {
            public void run() {

                receiveData();
                receiveDataFromIntent();
                LoadWidgets();

                try {

                    utilsGateway
                            = new UtilsGateway(
                            stationDetailsArrayList,
                            nameToIndexStation,
                            neighbourListArrayList,
                            stationNameArrayList
                    );

                    if (optId == R.id.optToStation || optId == R.id.optToPlace) {
                        resultPaths = utilsGateway.ComputePaths(FromStation, ToStation);
                    }

                    if (optId == R.id.optToToilet) {

                        NearbyToiletStations = utilsGateway.FindNearby(
                                FromStation, "hasToilet");
                    }

                    if (optId == R.id.optToParking) {

                        NearbyParkingStations = utilsGateway.FindNearby(
                                FromStation, "hasParking");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, 100);




    }


    // Load data from resources

    private void receiveData(){

        LoadDataFromSharedPref("stationDetails");
        LoadDataFromSharedPref("placeDetails");
        LoadDataFromSharedPref("nameToIndexStation");


    }

    private void receiveDataFromIntent(){

        Intent intent = getIntent();
        FromStation = intent.getStringExtra("FromStation");

        optId = intent.getIntExtra("opt_id",0);

        switch (optId){
            case R.id.optToStation:
                ToStation = intent.getStringExtra("To");
                ToPlace = "None";
                break;

            case R.id.optToPlace:
                ToStation = intent.getStringExtra("placeNearbyMetro");
                ToPlace = intent.getStringExtra("To");

                break;

            case R.id.optToParking:
                ToStation = ToPlace = "None";
                break;
            case R.id.optToToilet:
                ToStation = ToPlace = "None";
                break;

        }

    }

    private void LoadWidgets(){

        TextView FromStationTv = (TextView)findViewById(R.id.actE_valFromStation);
        TextView ToStationTv = (TextView)findViewById(R.id.actE_valToStation);
        TextView ToPlaceTv = (TextView)findViewById(R.id.actE_valToPlace);
        TextView ToPlaceLabelTv = (TextView)findViewById(R.id.actE_labelToPlace);
        TextView ToStationLabelTv = (TextView)findViewById(R.id.actE_labelToStation);


        FromStationTv.setText(FromStation);
        ToStationTv.setText(ToStation);
        ToPlaceTv.setText(ToPlace);

        switch (optId){
            case R.id.optToStation:
                ToPlaceLabelTv.setVisibility(View.GONE);
                ToPlaceTv.setVisibility(View.GONE);
                break;
            case R.id.optToPlace:
                break;
            case R.id.optToParking:
                ToPlaceLabelTv.setVisibility(View.GONE);
                ToPlaceTv.setVisibility(View.GONE);

                ToStationLabelTv.setVisibility(View.GONE);
                ToStationTv.setVisibility(View.GONE);
                break;
            case R.id.optToToilet:
                ToPlaceLabelTv.setVisibility(View.GONE);
                ToPlaceTv.setVisibility(View.GONE);

                ToStationLabelTv.setVisibility(View.GONE);
                ToStationTv.setVisibility(View.GONE);
                break;
        }


    }




    // Please don't uncomment : Copy Paste as it is across different activities

    // Receive data from shared preference



    private void LoadDataFromSharedPref(String Opt){
        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        Type type;

        switch (Opt){
            case "stationDetails":
                type =  new TypeToken<ArrayList<StationDetails>>(){}.getType();
                stationDetailsArrayList = gson.fromJson(
                        pref.getString("stationDetails",""),
                        type);
                break;

            case "placeDetails":
                type =  new TypeToken<ArrayList<PlaceDetails>>(){}.getType();
                placeDetailsArrayList = gson.fromJson(
                        pref.getString("placeDetails",""),type);
                break;

            case "hashMapFare":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                hashMapFare = gson.fromJson(
                        pref.getString("hashMapFare",""),type);
                break;

            case "nameToIndexStation":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                nameToIndexStation = gson.fromJson(
                        pref.getString("hashMapFare",""),type);
                break;

            case "neighbourDetails":
                type = new TypeToken<ArrayList<NeighbourList>>(){}.getType();
                neighbourListArrayList = gson.fromJson(
                        pref.getString("neighbourDetails",""),type);
                break;

        }

    }


    // end of receive data from shared preference




    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
