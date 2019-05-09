package com.yash.delhimetro.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.R;
import com.yash.delhimetro.ViewAdapters.PlaceListAdapter;
import com.yash.delhimetro.ViewAdapters.StationListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentRoutes extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private HashMap<String,Integer> nameToIndexStation = new HashMap<>();

    private static final String MY_PREFS_NAME = "Loaded Data";


    private ArrayList<StationDetails> stationDetailsArrayList = new ArrayList<>();


    private ArrayList<String> stationNameList = new ArrayList<>();

    public static PlaceholderFragmentRoutes newInstance(
            int index,ArrayList<String> stationNameList

    ) {
        PlaceholderFragmentRoutes fragment = new PlaceholderFragmentRoutes();


        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);

        bundle.putStringArrayList("stationList",stationNameList);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        stationNameList = getArguments().getStringArrayList("stationList");

        LoadDataFromSharedPref("stationDetails");
        LoadDataFromSharedPref("nameToIndexStation");

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_explore_routes,
                container, false);

        ArrayList<StationDetails> pathDetails = new ArrayList<>();

        for(String name : stationNameList){

            int idx = nameToIndexStation.get(name);
            StationDetails stationDetails = stationDetailsArrayList.get(idx);
            pathDetails.add(stationDetails);

        }

        Log.d("routeList : ",stationNameList.toString());

        StationListAdapter stationListAdapter = new StationListAdapter(pathDetails);


        RecyclerView recyclerView = (RecyclerView) root.findViewById(
                R.id.recyclerViewStationRoutes);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                root.getContext(),
                DividerItemDecoration.VERTICAL)
        );


        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);

        LinearLayoutManager mLayoutManager = new
                LinearLayoutManager(root.getContext());

        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(stationListAdapter);


        return root;
    }

    private void LoadDataFromSharedPref(String Opt){
        SharedPreferences pref = getContext().
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

            case "nameToIndexStation":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                nameToIndexStation = gson.fromJson(
                        pref.getString("nameToIndexStation",""),type);
                break;



        }

    }
}