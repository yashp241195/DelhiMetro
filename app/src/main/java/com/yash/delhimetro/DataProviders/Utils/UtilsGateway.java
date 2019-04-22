package com.yash.delhimetro.DataProviders.Utils;

import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.StationDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class UtilsGateway {

    Graph graph;
    private ArrayList<StationDetails> stationDetailsArrayList;
    private HashMap<String,Integer> nameToIndexStation;
    private ArrayList<NeighbourList> neighbourListArrayList;
    private ArrayList<String> stationNameList;


    public UtilsGateway(ArrayList<StationDetails> stationDetailsArrayList,
                        HashMap<String, Integer> nameToIndexStation,
                        ArrayList<NeighbourList> neighbourListArrayList,
                        ArrayList<String> stationNameList){

        this.stationDetailsArrayList = stationDetailsArrayList;
        this.nameToIndexStation = nameToIndexStation;
        this.neighbourListArrayList = neighbourListArrayList;
        this.stationNameList = stationNameList;
        init();
    }


    private void init(){

        graph = new Graph(stationNameList);
        populateNeighbours();


    }

    public void populateNeighbours(){

        for (NeighbourList neighbourList: neighbourListArrayList){

            int time = neighbourList.getTimeInterval();
            String line = neighbourList.getLine();
            ArrayList<String> adj = neighbourList.getStationList();

            for (int i = 0; i < adj.size()-1; i++) {
                graph.addEdgeUnDirected(adj.get(i),adj.get(i+1),time,line);
            }

        }
    }

    public ArrayList<ResultPath> ComputePaths(String From,String To){
       return graph.getResults(From,To);
    }

    public ArrayList<String> FindNearby(String From,String Opt){

        PriorityQueue<DijkstraNode> ResultQueue = new PriorityQueue<>();
        ResultQueue =  graph.resultDijkstraSorted(From);
        ArrayList<String> resultStations = new ArrayList<>();


        while (ResultQueue.isEmpty()){

            DijkstraNode dijkstraNode = ResultQueue.poll();
            String stationName = dijkstraNode.getCurrentStation();

            int idx = nameToIndexStation.get(stationName);


            switch (Opt){
                case "hasToilet":

                    if(stationDetailsArrayList.get(idx).getHasToilet())
                    resultStations.add(stationName);

                    break;

                case "hasParking":

                    if(stationDetailsArrayList.get(idx).getHasParking())
                        resultStations.add(stationName);

                    break;
            }

        }

        return resultStations;
    }

}
