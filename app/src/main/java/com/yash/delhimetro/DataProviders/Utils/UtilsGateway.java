package com.yash.delhimetro.DataProviders.Utils;

import android.content.Context;
import android.util.Log;

import com.yash.delhimetro.DataProviders.FareMetro;
import com.yash.delhimetro.DataProviders.MetroFareDBHandler;
import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.ResultPath;
import com.yash.delhimetro.DataProviders.StationDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class UtilsGateway {

    private Graph graph;
    private ArrayList<StationDetails> stationDetailsArrayList;
    private HashMap<String,Integer> nameToIndexStation;
    private ArrayList<NeighbourList> neighbourListArrayList;
    private ArrayList<String> stationNameList;

    private Context context;

    public UtilsGateway(
            Context context,
            ArrayList<StationDetails> stationDetailsArrayList,
                        HashMap<String, Integer> nameToIndexStation,
                        ArrayList<NeighbourList> neighbourListArrayList,
                        ArrayList<String> stationNameList){

        this.context = context;
        this.stationDetailsArrayList = stationDetailsArrayList;
        this.nameToIndexStation = nameToIndexStation;
        this.neighbourListArrayList = neighbourListArrayList;
        this.stationNameList = stationNameList;

        init();
        populateNeighbours();
    }


    private void init(){

        graph = new Graph(stationNameList);
        graph.setStationDetailsArrayList(stationDetailsArrayList);

    }

    private void populateNeighbours(){

        for (NeighbourList neighbourList: neighbourListArrayList){

            int time = neighbourList.getTimeInterval();
            String line = neighbourList.getLine();
            ArrayList<String> adj = neighbourList.getStationList();

            for (int i = 0; i < adj.size()-1; i++) {
                graph.addEdgeUnDirected(adj.get(i),adj.get(i+1),time,line);
            }

        }
    }

    private int getFareFromDB(Context context, String From, String To){

        MetroFareDBHandler metroFareDBHandler = new MetroFareDBHandler(context);

        FareMetro fm = metroFareDBHandler.getFareMetro(From,To);

        return  (fm == null)?-1:fm.getFare();
    }

    public ArrayList<ResultPath> ComputePaths(String From, String To){

        ArrayList<ResultPath> rpList = new ArrayList<>();

        int fare = getFareFromDB(context,From,To);

        for (ResultPath rp: graph.getResults(From,To)) {

            rp.setFare(fare);

            int countParking = 0;

            for (String stn: rp.getStationList()){

                int idx = nameToIndexStation.get(stn);

                if(stationDetailsArrayList.get(idx).getHasParking())
                    countParking++;
            }

            rp.setParkingCount(countParking);

            rpList.add(rp);
        }

       return rpList;
    }


    public ArrayList<String> FindNearby(String From,String Opt){

        PriorityQueue<DijkstraNode> ResultQueue = graph.
                resultDijkstraSorted(From);

        ArrayList<String> resultStations = new ArrayList<>();

        int count = 0;

        while (!ResultQueue.isEmpty()){

            DijkstraNode dn = ResultQueue.poll();

            String name = dn.getCurrentStation();

            int idx = nameToIndexStation.get(name);


            if(count > 3)
                break;



            switch (Opt){
                case "hasToilet":

                    if(stationDetailsArrayList.get(idx).getHasToilet()) {
                        resultStations.add(name);
                        count++;
                    }
                    break;

                case "hasParking":

                    if(stationDetailsArrayList.get(idx).getHasParking()) {
                        resultStations.add(name);
                        count++;
                    }
                    break;
            }

        }

        Log.d("Nearby "+Opt+" : ",resultStations.toString());

        return resultStations;
    }


}
