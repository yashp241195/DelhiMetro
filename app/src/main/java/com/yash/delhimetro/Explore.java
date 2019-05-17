package com.yash.delhimetro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.ResultPath;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.DataProviders.Utils.UtilsGateway;
import com.yash.delhimetro.ViewAdapters.RouteSummaryListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    ArrayList<ResultPath> resultPaths = new ArrayList<>();
    RecyclerView recyclerView;
    RouteSummaryListAdapter routeSummaryListAdapter;

    private int optId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        receiveData();
        receiveDataFromIntent();
        LoadWidgets();


        utilsGateway
                = new UtilsGateway(
                        this,
                stationDetailsArrayList,
                nameToIndexStation,
                neighbourListArrayList,
                stationNameArrayList
        );

        /*
        Log.d("stationDetail : ", stationDetailsArrayList.toString());
        Log.d("stationNameHash : ", nameToIndexStation.toString());
        Log.d("neighbourList : ", neighbourListArrayList.toString());
        Log.d("stationName : ", stationNameArrayList.toString());
        */

        if (optId == R.id.optToStation || optId == R.id.optToPlace) {

            resultPaths = utilsGateway.ComputePaths(FromStation, ToStation);

        }


        TextView FromStationTv = (TextView)findViewById(R.id.actE_valFromStation);
        TextView ToStationTv = (TextView)findViewById(R.id.actE_valToStation);
        TextView ToPlaceTv = (TextView)findViewById(R.id.actE_valToPlace);
        TextView ToPlaceLabelTv = (TextView)findViewById(R.id.actE_labelToPlace);
        TextView ToStationLabelTv = (TextView)findViewById(R.id.actE_labelToStation);



        if (optId == R.id.optToToilet) {

            int idx = nameToIndexStation.get(FromStation);

            if(stationDetailsArrayList.get(idx).getHasToilet()){
                ToStationTv.setText(FromStation);
                ToPlaceLabelTv.setText("Toilet Nearby");
                ToPlaceTv.setText("Toilet already present at Station");

                resultPaths = utilsGateway.ComputePaths(FromStation, FromStation);
            }else {

                NearbyToiletStations = utilsGateway.FindNearby(
                        FromStation, "hasToilet");

                StringBuilder text = new StringBuilder();


                int j = 0;

                for (String to:NearbyToiletStations){

                    text.append(to);
                    if(j < NearbyToiletStations.size()-1)
                        text.append(", ");
                    else
                        text.append(" ");

                    j++;



                    ArrayList<ResultPath> tempRP = utilsGateway.ComputePaths(FromStation,to);

                    Collections.sort(tempRP, new Comparator<ResultPath>() {
                        @Override
                        public int compare(ResultPath lhs, ResultPath rhs) {
                            return lhs.getSwitchCount().compareTo(rhs.getSwitchCount());
                        }
                    });

                    resultPaths.add(tempRP.get(0));
                }

                text.append(" "+((j>1)?"are":"is")+" the nearest stations with toilets ");


                ToStationTv.setText(text);

                ToPlaceLabelTv.setVisibility(View.GONE);
                ToPlaceTv.setVisibility(View.GONE);


                Log.d("ToiletNearby",NearbyToiletStations.toString());
            }


        }

        if (optId == R.id.optToParking) {

            int idx = nameToIndexStation.get(FromStation);

            if(stationDetailsArrayList.get(idx).getHasParking()){
                ToStationTv.setText(FromStation);
                ToPlaceLabelTv.setText("Parking Nearby");
                ToPlaceTv.setText("Parking already present at Station");
                resultPaths = utilsGateway.ComputePaths(FromStation, FromStation);

            }else {

                NearbyParkingStations = utilsGateway.FindNearby(
                        FromStation, "hasParking");


                StringBuilder text = new StringBuilder();


                int j = 0;


                for (String to:NearbyParkingStations){

                    text.append(to);
                    if(j < NearbyParkingStations.size()-1)
                        text.append(", ");
                    else
                        text.append(" ");

                    j++;

                    ArrayList<ResultPath> tempRP = utilsGateway.ComputePaths(FromStation,to);

                    Collections.sort(tempRP, new Comparator<ResultPath>() {
                        @Override
                        public int compare(ResultPath lhs, ResultPath rhs) {
                            return lhs.getSwitchCount().compareTo(rhs.getSwitchCount());
                        }
                    });

                    resultPaths.add(tempRP.get(0));
                }

                text.append(" "+((j>1)?"are":"is")+" the nearest stations with parking ");

                ToStationTv.setText(text.toString());

                ToPlaceLabelTv.setVisibility(View.GONE);
                ToPlaceTv.setVisibility(View.GONE);

                Log.d("ParkingNearby",NearbyToiletStations.toString());

            }
        }


        Log.d("Transit",FromStation+"->"+ToStation);
        Log.d("Result Paths : ",resultPaths.toString());

        routeSummaryListAdapter = new RouteSummaryListAdapter(this,
                resultPaths,
                stationDetailsArrayList,
                nameToIndexStation

        );



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RouteSummaryListView);


        recyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext(),
                DividerItemDecoration.VERTICAL));


        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(routeSummaryListAdapter);




    }


    // Load data from resources

    private void receiveData(){

        LoadDataFromSharedPref("stationDetails");

        for (int i = 0; i < stationDetailsArrayList.size(); i++) {

            stationNameArrayList.add(stationDetailsArrayList
                    .get(i).getStationName());

        }

        LoadDataFromSharedPref("placeDetails");

        LoadDataFromSharedPref("nameToIndexStation");
        LoadDataFromSharedPref("neighbourDetails");

        LoadDataFromSharedPref("hashMapFare");


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
//                ToPlaceLabelTv.setVisibility(View.GONE);
//                ToPlaceTv.setVisibility(View.GONE);

//                ToStationLabelTv.setVisibility(View.GONE);
//                ToStationTv.setVisibility(View.GONE);
                break;
            case R.id.optToToilet:
//                ToPlaceLabelTv.setVisibility(View.GONE);
//                ToPlaceTv.setVisibility(View.GONE);

//                ToStationLabelTv.setVisibility(View.GONE);
//                ToStationTv.setVisibility(View.GONE);
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



            case "nameToIndexStation":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                nameToIndexStation = gson.fromJson(
                        pref.getString("nameToIndexStation",""),type);
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
