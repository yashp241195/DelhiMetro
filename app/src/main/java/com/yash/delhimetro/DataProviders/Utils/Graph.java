package com.yash.delhimetro.DataProviders.Utils;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

class ConnectorEdge{
    private String FromStation,ToStation,edgeLine;
    private Integer  edgeTimeInterval;

    public ConnectorEdge(){

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

    public Integer getEdgeTimeInterval() {
        return edgeTimeInterval;
    }

    public void setEdgeTimeInterval(Integer edgeTimeInterval) {
        this.edgeTimeInterval = edgeTimeInterval;
    }

    public String getEdgeLine() {
        return edgeLine;
    }

    public void setEdgeLine(String edgeLine) {
        this.edgeLine = edgeLine;
    }
}

class StationVertex{
    private String stationName;
    private ArrayList<ConnectorEdge> neighbourListStation=new ArrayList<>();

    public StationVertex(){

    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public ArrayList<ConnectorEdge> getNeighbourListStation() {
        return neighbourListStation;
    }

    public void setNeighbourListStation(ArrayList<ConnectorEdge> neighbourListStation) {
        this.neighbourListStation = neighbourListStation;
    }

}

public class Graph {

    private HashMap<String,StationVertex> VertexHash = new HashMap<>();
    private ArrayList<String> stationNameList;
    private HashMap<String,Integer> nameToIndex = new HashMap<>();

    public Graph(ArrayList<String> stationNameList){

        int size = stationNameList.size();
        this.stationNameList = stationNameList;

        for (int i = 0; i < size; i++) {

            StationVertex stationVertex = new StationVertex();

            String Name = stationNameList.get(i);
            stationVertex.setStationName(Name);

            VertexHash.put(Name, stationVertex);
            nameToIndex.put(Name, i);

        }

    }

    public void addEdgeUnDirected(String From,String To,Integer timeInterval,String Line){

        addEdge(From,To,timeInterval,Line);
        addEdge(To,From,timeInterval,Line);

    }

    // don't allow directed edges in the Graph make it private

    private void addEdge(String From,String To,Integer timeInterval,String Line){

        ConnectorEdge connectorEdge = new ConnectorEdge();
        connectorEdge.setFromStation(From);
        connectorEdge.setToStation(To);
        connectorEdge.setEdgeTimeInterval(timeInterval);
        connectorEdge.setEdgeLine(Line);

        VertexHash.get(From).getNeighbourListStation().add(connectorEdge);
    }

    public ArrayList<ResultPath> getResults(String From,String To){

        ArrayList<ResultPath> resultPaths = new ArrayList<>();

        if(
                VertexHash.containsKey(From) &&
                VertexHash.containsKey(To)
        ){


            PriorityQueue<DijkstraNode> pqResult = this.resultDijkstraSorted(From);
            int timeLimit = (int)(this.getMinTimeTo(To,pqResult)*1.3);

            Log.d("time Limit : ",Integer.valueOf(timeLimit).toString());
            ArrayList<String> path = new ArrayList<>();

            int timeTaken = 0;

            HashSet<String> visited = new HashSet<>();

            path.add(From);
            this.findRoutes(From,To,timeTaken,path,visited,resultPaths,timeLimit);

            return refactorResults(resultPaths);

        }

        return resultPaths;

    }

    private ArrayList<ResultPath> refactorResults(ArrayList <ResultPath> rpList){


        // sorting the result list
        Collections.sort(rpList);

        // remove duplicates from sorted list
        for (int i = 0; i < rpList.size()-1; i++) {
            if(rpList.get(i).equals(rpList.get(i+1))){
                rpList.remove(rpList.get(i));
            }
        }

        // populate the switch counts

        for (ResultPath rp: rpList) {

            ArrayList<String> stationList = rp.getStationList();

            // if ConnectorEdge12 and ConnectorEdge23 are not same then there
            // is a change in line

            int count = 0;
            String ConnectorEdge12, ConnectorEdge23;


            for (int i = 0; i < stationList.size()-2; i++) {

                String f1 = stationList.get(i);
                String f2 = stationList.get(i+1);
                String f3 = stationList.get(i+2);

                ConnectorEdge12 = ConnectorEdge23 = "";


                ArrayList<ConnectorEdge> neighbours12 = VertexHash.get(f1).
                        getNeighbourListStation();

                for (ConnectorEdge e: neighbours12) {
                    if(e.getToStation().equals(f2)){
                        ConnectorEdge12 = e.getEdgeLine();
                        break;
                    }
                }

                ArrayList<ConnectorEdge> neighbours23 = VertexHash.get(f2).
                        getNeighbourListStation();

                for (ConnectorEdge e: neighbours23) {
                    if(e.getToStation().equals(f3)){
                       ConnectorEdge23 = e.getEdgeLine();
                       break;

                    }
                }

                count += (ConnectorEdge12.equals(ConnectorEdge23))?0:1;

            }

            rp.setSwitchCount(count);

        }

        // sorting the result list again
        Collections.sort(rpList);


        return rpList;
    }


    private int getMinTimeTo(
                String To,
                PriorityQueue<DijkstraNode>
                        resultDijkstraSorted
    )
    {
        int time = 0;


        for(DijkstraNode dn: resultDijkstraSorted){
            if(dn.getCurrentStation().equals(To)){
                time = dn.getMinTimeFromSource();
            }

        }

        return time;
    }


    // results

    public PriorityQueue<DijkstraNode> resultDijkstraSorted(String From){

        ArrayList<DijkstraNode> DijkstraResult = new ArrayList<>();
        PriorityQueue<DijkstraNode> Queue = new PriorityQueue<>();

        HashSet<String> visited = new HashSet<>();


        int i = 0;

        for (String stationName : stationNameList){

            DijkstraNode currentNode = new DijkstraNode();
            currentNode.setCurrentStation(stationName);

            // set parents to null
            currentNode.setParentStation(null);

            // set distance to infinity
            currentNode.setMinTimeFromSource(Integer.MAX_VALUE);

            i++;

            DijkstraResult.add(currentNode);

        }

        DijkstraResult.get(nameToIndex.get(From)).setMinTimeFromSource(0);

        DijkstraNode currentNode = new DijkstraNode();
        currentNode.setCurrentStation(From);
        currentNode.setParentStation(null);
        currentNode.setMinTimeFromSource(0);

        Queue.add(currentNode);


        while (!Queue.isEmpty()){

            currentNode =  Queue.poll();

            String currentNodeName = currentNode.getCurrentStation();
            visited.add(currentNodeName);

            int FromIdx = nameToIndex.get(currentNodeName);

            int minTimeFromSourceTillCurrentNode = DijkstraResult.
                    get(FromIdx).getMinTimeFromSource();


            for (ConnectorEdge neighbour : VertexHash
                    .get(currentNodeName).
                    getNeighbourListStation()
                 )
            {

                String ToStationName = neighbour.getToStation();
                int edgeTimeInterval = neighbour.getEdgeTimeInterval();

                if(!visited.contains(ToStationName)){

                    int toIdx = nameToIndex.get(ToStationName);

                    int minTimeFromSourceTillTo = DijkstraResult.get(toIdx).getMinTimeFromSource();
                    int alt = minTimeFromSourceTillCurrentNode + edgeTimeInterval;

                    if(alt < minTimeFromSourceTillTo) {

                        DijkstraResult.get(toIdx).setMinTimeFromSource(alt);
                        DijkstraResult.get(toIdx).setParentStation(currentNodeName);

                        Queue.add(DijkstraResult.get(toIdx));

                    }

                }

            }

        }

        return new PriorityQueue<>(DijkstraResult);


    }

    private void findRoutes(String From, String To,
                            int timeTaken, ArrayList<String> path,
                            HashSet<String> visited,
                            ArrayList<ResultPath> resultPaths,
                            int timeLimit
                            ){


        if(timeTaken > timeLimit){
//            Log.d("path terminate ","time limit exceed terminating.. ");
            return;
        }


        visited.add(From);


        if(From.equals(To)){

            ResultPath rp = new ResultPath();

            rp.setStationList(new ArrayList<String>(path));
            rp.setTimeTaken(timeTaken);

//            Log.d("path founded : ",rp.toString());
            resultPaths.add(rp);



        }else {


            for (ConnectorEdge neighbour : VertexHash.get(From).
                    getNeighbourListStation()
            ) {

                String ToStation = neighbour.getToStation();

                if (!visited.contains(ToStation)) {

                    path.add(ToStation);
                    timeTaken += neighbour.getEdgeTimeInterval();

//                    Log.d("path added ",ToStation);

                    this.findRoutes(ToStation, To, timeTaken,
                            path, visited, resultPaths, timeLimit);

                    path.remove(ToStation);
                    timeTaken -= neighbour.getEdgeTimeInterval();

//                    Log.d("path removed ",ToStation);


                }

            }
        }


        visited.remove(From);


    }





}
