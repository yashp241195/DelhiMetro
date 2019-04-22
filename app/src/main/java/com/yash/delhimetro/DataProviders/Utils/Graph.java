package com.yash.delhimetro.DataProviders.Utils;


import android.support.annotation.NonNull;

import java.util.ArrayList;
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

    private int size;
    private HashMap<String,StationVertex> VertexHash = new HashMap<>();
    private ArrayList<String> stationNameList = new ArrayList<>();

    public Graph(ArrayList<String> stationNameList){

        this.size = stationNameList.size();

        for (int i = 0; i < size; i++) {

            StationVertex stationVertex = new StationVertex();

            String Name = stationNameList.get(i);
            stationVertex.setStationName(Name);

            VertexHash.put(Name,stationVertex);

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

            ArrayList<String> path = new ArrayList<>();
            int time = 0;

            HashSet<String> visited = new HashSet<>();
            visited.add(From);

            this.findRoutes(From,To,time,path,visited,resultPaths);

        }

        return resultPaths;

    }

    // sorted results

    public PriorityQueue<DijkstraNode> resultDijkstraSorted(String From){

        ArrayList<DijkstraNode> DijkstraResult = new ArrayList<>();
        PriorityQueue<DijkstraNode> Queue = new PriorityQueue<>();

        HashMap<String,Integer> nameToIndex = new HashMap<>();

        int i = 0;

        for (String stationName : stationNameList){

            DijkstraNode currentNode = new DijkstraNode();

            currentNode.setCurrentStation(stationName);

            nameToIndex.put(stationName,i);
            currentNode.setParentStation(null);

            // set distance to infinity except the source station
            int time = stationName.equals(From)?0:Integer.MAX_VALUE;

            currentNode.setMinTimeFromSource(time);


            i++;

            Queue.add(currentNode);
            DijkstraResult.add(currentNode);

        }

        HashSet<String> visited = new HashSet<>();

        visited.add(From);



        while (!Queue.isEmpty()){

            DijkstraNode currentNode =  Queue.poll();
            String currentNodeName = currentNode.getCurrentStation();


            for (ConnectorEdge neighbour:
                    VertexHash.get(currentNodeName).
                    getNeighbourListStation()
                 )
            {

                String ToStationName = neighbour.getToStation();

                if(!visited.contains(ToStationName)){

                    int toIdx = nameToIndex.get(ToStationName);

                    int edgeTimeInterval = neighbour.getEdgeTimeInterval();
                    int minTimeFromSource = DijkstraResult.get(toIdx).getMinTimeFromSource();

                    int alt = minTimeFromSource + edgeTimeInterval;

                    if(alt < minTimeFromSource) {

                        visited.add(ToStationName);

                        DijkstraResult.get(toIdx).setMinTimeFromSource(alt);
                        DijkstraResult.get(toIdx).setParentStation(From);

                        Queue.add(DijkstraResult.get(toIdx));

                    }

                }

            }

        }

        PriorityQueue<DijkstraNode> ResultQueue = new PriorityQueue<>();

        for(DijkstraNode dijkstraNode:DijkstraResult){
            ResultQueue.add(dijkstraNode);
        }

        return ResultQueue;


    }

    private void findRoutes(String From, String To,
                            Integer timeTaken,
                            ArrayList<String> path,
                            HashSet<String> visited,
                            ArrayList<ResultPath> resultPaths
                            ){

        StationVertex stationVertexFrom = VertexHash.get(From);
        String FromStation = stationVertexFrom.getStationName();

        visited.add(FromStation);

        if(FromStation.equals(To)){
            path.add(FromStation);

            ResultPath resultPath = new ResultPath();

            resultPath.setStationList(path);
            resultPath.setTimeTaken(timeTaken);

            resultPaths.add(resultPath);
        }

        for (ConnectorEdge neighbour :
                stationVertexFrom.
                getNeighbourListStation()){

            String ToStation = neighbour.getToStation();

            if(!visited.contains(ToStation)){

                visited.add(ToStation);
                path.add(ToStation);

                timeTaken += neighbour.getEdgeTimeInterval();

                findRoutes(ToStation,To,timeTaken,path,visited,resultPaths);

                timeTaken -= neighbour.getEdgeTimeInterval();


            }


        }

        visited.remove(FromStation);

    }






}
