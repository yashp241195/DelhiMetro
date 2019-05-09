package com.yash.delhimetro.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.R;
import com.yash.delhimetro.ViewAdapters.PlaceListAdapter;
import com.yash.delhimetro.ViewAdapters.StationListAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentPlaceNearby extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";


    private PlaceListAdapter placeListAdapter;
    private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();



    public static PlaceholderFragmentPlaceNearby newInstance(int index) {
        PlaceholderFragmentPlaceNearby fragment = new PlaceholderFragmentPlaceNearby();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_explore_routes,
                container, false);


        placeListAdapter = new PlaceListAdapter(placeDetailsArrayList);


        RecyclerView recyclerView = (RecyclerView) root.findViewById(
                R.id.recyclerViewStationRoutes);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                root.getContext(),
                DividerItemDecoration.VERTICAL)
        );


        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(placeListAdapter);



        return root;
    }
}