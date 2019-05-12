package com.yash.delhimetro.ui.main;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.R;
import com.yash.delhimetro.ViewAdapters.GridSpacingItemDecoration;
import com.yash.delhimetro.ViewAdapters.PlaceListAdapter;
import com.yash.delhimetro.ViewAdapters.StationListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentPlaceNearby extends Fragment {


    private static final String MY_PREFS_NAME = "Loaded Data";


    private PlaceListAdapter placeListAdapter;
    private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();
    private ArrayList<String> stationNameList = new ArrayList<>();
    private HashSet<String> stationNameHash;



    public static PlaceholderFragmentPlaceNearby newInstance(int index
            ,ArrayList<String> stationNameList
    ) {
        PlaceholderFragmentPlaceNearby fragment = new PlaceholderFragmentPlaceNearby();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("stationList",stationNameList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        stationNameList = getArguments().getStringArrayList("stationList");
        stationNameHash = new HashSet<>(stationNameList);

        LoadDataFromSharedPref("placeDetails");

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_explore_routes,
                container, false);


        if(!stationNameList.isEmpty()) {
            ArrayList<PlaceDetails> placeDetailsNearby = new ArrayList<>();

            for (PlaceDetails placeDetails1 : placeDetailsArrayList) {

                String nearbyMetro = placeDetails1.getNearbyMetroStation();

                if (stationNameHash.contains(nearbyMetro)) {
                    placeDetailsNearby.add(placeDetails1);
                }

            }


            placeListAdapter = new PlaceListAdapter(getContext(), placeDetailsNearby);


            RecyclerView recyclerView = (RecyclerView) root.findViewById(
                    R.id.recyclerViewStationRoutes);

            recyclerView.addItemDecoration(
                    new GridSpacingItemDecoration(2,
                            dpToPx(10), true)
            );


            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(
                    root.getContext(),2);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(placeListAdapter);

        }


        return root;
    }

    // used for spacing in Grid Spacing change density pixel to pixel
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void LoadDataFromSharedPref(String Opt){
        SharedPreferences pref = getContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        Type type;

        switch (Opt){


            case "placeDetails":
                type =  new TypeToken<ArrayList<PlaceDetails>>(){}.getType();
                placeDetailsArrayList = gson.fromJson(
                        pref.getString("placeDetails",""),type);
                break;

        }


    }


}