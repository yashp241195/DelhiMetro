package com.yash.delhimetro.ViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yash.delhimetro.DataProviders.ResultPath;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.ExploreRoutes;
import com.yash.delhimetro.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class RouteSummaryListAdapter extends RecyclerView.Adapter<RouteSummaryListAdapter.MyViewHolder> {

    private ArrayList<ResultPath> resultPathsArrayList;
    private ArrayList<StationDetails> stationDetailsArrayList;
    private HashMap<String,Integer> nameToIndexStation;
    private Context context;

    public RouteSummaryListAdapter(
            Context context,
            ArrayList<ResultPath> resultPathsArrayList,
            ArrayList<StationDetails> stationDetailsArrayList,
            HashMap<String,Integer> nameToIndexStation

    ) {
        this.context = context;
        this.resultPathsArrayList = resultPathsArrayList;
        this.stationDetailsArrayList = stationDetailsArrayList;
        this.nameToIndexStation = nameToIndexStation;

        sortBySwitchCount();

    }


    private void sortBySwitchCount(){
        Collections.sort(resultPathsArrayList, new Comparator<ResultPath>() {
            @Override
            public int compare(ResultPath lhs, ResultPath rhs) {
                return lhs.getSwitchCount().compareTo(rhs.getSwitchCount());
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fareTv,timeTv,stopTv,interchangeTv, parkingTv;
        RecyclerView recyclerViewPathSummary;

        MyViewHolder(View view) {

            super(view);

            fareTv = (TextView) view.findViewById(R.id.row_path_summary_fare_tv);
            timeTv = (TextView) view.findViewById(R.id.row_path_summary_time_tv);
            stopTv = (TextView) view.findViewById(R.id.row_path_summary_stops_tv);
            interchangeTv = (TextView) view.findViewById(R.id.row_path_summary_interchange_tv);
            parkingTv = (TextView) view.findViewById(R.id.row_path_summary_mall_tv);
            recyclerViewPathSummary = (RecyclerView)view.findViewById(R.id.row_path_summary_explore_path_listView);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickAction(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {


        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_path_station_listitem,
                        viewGroup,
                        false
                );


        return new MyViewHolder(itemView);
    }

    private void onClickAction(View v, int i){

        Intent intent = new Intent(context, ExploreRoutes.class);

        ResultPath resultPath = resultPathsArrayList.get(i);
        ArrayList<String> stationList = resultPath.getStationList();

        intent.putStringArrayListExtra("stationList",stationList);

        context.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        ResultPath resultPath = resultPathsArrayList.get(i);

        String fareText = "Fare \nRs " + resultPath.getFare();
        String timeText = "Time \n" + resultPath.getTimeTaken() + " min";
        String stopsText = "Stops \n" + resultPath.getStops();
        String parkingText = "Parking \n" + resultPath.getParkingCount();
        String switchText = "Interchange\n" + resultPath.getSwitchCount();

        ArrayList<String> pathSummary = resultPath.getSummaryList();

        myViewHolder.fareTv.setText(fareText);
        myViewHolder.timeTv.setText(timeText);
        myViewHolder.stopTv.setText(stopsText);
        myViewHolder.interchangeTv.setText(switchText);
        myViewHolder.parkingTv.setText(parkingText);

        ArrayList<StationDetails> pathSummaryStationList = new ArrayList<>();

        for (String StnName : pathSummary) {

            int idx = nameToIndexStation.get(StnName);
            StationDetails stationDetails = stationDetailsArrayList.get(idx);

            pathSummaryStationList.add(stationDetails);

        }

        Log.d("path : ", pathSummaryStationList.toString());


        myViewHolder.recyclerViewPathSummary.setItemViewCacheSize(20);
        myViewHolder.recyclerViewPathSummary.setDrawingCacheEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        myViewHolder.recyclerViewPathSummary.setLayoutManager(mLayoutManager);

        StationListAdapter stationListAdapter = new StationListAdapter(pathSummaryStationList);
        myViewHolder.recyclerViewPathSummary.setAdapter(stationListAdapter);


        myViewHolder.recyclerViewPathSummary.setOnTouchListener(
                new View.OnTouchListener() {

            private long startClickTime;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Log.d("click", "true");

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            startClickTime = System.currentTimeMillis();

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {

                            if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {

                                // Touch was a simple tap. Do whatever.

                            } else {

                                myViewHolder.onClick(v);
                                // Touch was a not a simple tap.

                            }

                        }

                        return true;
                    }
                }

        );
    }


    @Override
    public int getItemCount() {
        return resultPathsArrayList.size();
    }
}
