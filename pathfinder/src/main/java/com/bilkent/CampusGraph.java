package com.bilkent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class CampusGraph {
    private Map<Long, Location> locations;
    private Map<Location, List<Edge>> edges;

    public CampusGraph() {
        locations = new HashMap<>();
        edges = new HashMap<>();
    }

    public Map<Long, Location> getLocations() {
        return locations;
    }

    public Map<Location, List<Edge>> getEdges() {
        return edges;
    }

    public void addLocation(Location location){
        locations.put(location.getId(), location);

        edges.put(location, new ArrayList<>());
    }
    public Location getLocation(long id){
        return locations.get(id);
    }

    public void addEdge(Location from, Location to, double distance){
        //For safety
        if(from == null || to == null || !edges.containsKey(from) || !edges.containsKey(to)){
            return;
        }

        Edge edge1 = new Edge(from, to, distance);
        edges.get(from).add(edge1);
        // if you can walk from A to B you can walk from B to A also
        Edge edge2 = new Edge(to, from, distance);
        edges.get(to).add(edge2);
    }

    public List<Edge> getNeighbours(Location location){
        return edges.get(location);
    }

    class RouteNode{

        long id;
        double distance;
        
        public RouteNode(long id, double distance){
            this.id = id;
            this.distance = distance;
        }
    }

    public void connectPOIsToPaths(Calculator calc){
        List<Location> pathNodes = new ArrayList<>();
        List<Location> pois = new ArrayList<>();

        List<Location> locationsList = new ArrayList<>(locations.values());
        for(int i = 0; i < locationsList.size(); i++){
            if(locationsList.get(i).getType() == LocationType.PATH_NODE){
                pathNodes.add(locationsList.get(i));
            }
            else{
                pois.add(locationsList.get(i));
            }
        }

        for(int i = 0; i < pois.size(); i++){
            Location closestPath = null;
            double minDistance = Double.MAX_VALUE;

            for(int j = 0; j < pathNodes.size(); j++){
                double currDistance = calc.realDistance(pois.get(i), pathNodes.get(j));
                if(currDistance < minDistance){
                    minDistance = currDistance;
                    closestPath = pathNodes.get(j);
                }
            }

            if(closestPath != null){
                addEdge(pois.get(i), closestPath, minDistance);
            }
        }
        
        System.out.println("All POIs are connected with roads succesfully");
    }

    //Usage: to find shortest path to A -> B
    public List<Location> findShortestPath(long startId, long targetId){
        Map<Long,Double> distances = new HashMap<>();
        Map<Long,Long> previous = new HashMap<>();

        PriorityQueue<RouteNode> pq = new PriorityQueue<>((a,b) -> Double.compare(a.distance, b.distance));
        
        distances.put(startId, 0.0);
        pq.add(new RouteNode(startId, 0.0));

        while(!pq.isEmpty()){
            RouteNode current = pq.poll();
            if(current.id == targetId) 
                break;
            List<Edge> neighbours = getNeighbours(getLocation(current.id));
            if(neighbours == null)
                continue;
            for(int i = 0; i < neighbours.size(); i++){
                Edge edge = neighbours.get(i);
                long neighbourId = edge.getTo().getId();

                double newDistance = current.distance + edge.getDistance();
                
                if(newDistance < distances.getOrDefault(neighbourId, Double.MAX_VALUE)){
                    //new shortest distance
                    distances.put(neighbourId, newDistance);
                    //came from current
                    previous.put(neighbourId, current.id);

                    pq.add(new RouteNode(neighbourId, newDistance));
                }
            }
        }

        List<Location> path = new ArrayList<>();
        if(previous.get(targetId) == null && targetId != startId){
            return path;
        }
        Long currId = targetId;

        while(currId != startId){
            path.add(getLocation(currId));
            currId = previous.get(currId);
        }
        path.add(getLocation(startId));
        Collections.reverse(path);

        return path;
    }

    //Usage: to find nearest Cafe / Smoking Area...
    public List<Location> findPathtoNearestSearchedLocation(long startId, LocationType targetType){
        Map<Long,Double> distances = new HashMap<>();
        Map<Long,Long> previous = new HashMap<>();

        PriorityQueue<RouteNode> pq = new PriorityQueue<>((a,b) -> Double.compare(a.distance, b.distance));
        
        distances.put(startId, 0.0);
        pq.add(new RouteNode(startId, 0.0));

        long targetId = -1;

        while(!pq.isEmpty()){
            RouteNode current = pq.poll();
            if(getLocation(current.id).getType() == targetType){
                targetId = current.id;
                break;
            }
            List<Edge> neighbours = getNeighbours(getLocation(current.id));
            if(neighbours == null)
                continue;
            for(int i = 0; i < neighbours.size(); i++){
                Edge edge = neighbours.get(i);
                long neighbourId = edge.getTo().getId();

                double newDistance = current.distance + edge.getDistance();
                
                if(newDistance < distances.getOrDefault(neighbourId, Double.MAX_VALUE)){
                    //new shortest distance
                    distances.put(neighbourId, newDistance);
                    //came from current
                    previous.put(neighbourId, current.id);

                    pq.add(new RouteNode(neighbourId, newDistance));
                }
            }
        }

        List<Location> path = new ArrayList<>();

        if(targetId == -1){
            System.out.println(targetType +" Couldn't find. ");
            return path;
        }
        Long currId = targetId;

        while(currId != startId){
            path.add(getLocation(currId));
            currId = previous.get(currId);
        }
        path.add(getLocation(startId));
        Collections.reverse(path);

        return path;
    }

    //Usage: to print all named Locations
    public void printNamedLocations(){
        for(Location loc : locations.values()){
            if(loc.getType() != LocationType.PATH_NODE){
                System.out.println(loc);
                System.out.println("--------------------");
            }
        }
    }

    //Usage: to print numbers of all Locations and all Edges
    public void printGraphStats() {
        System.out.println("--- MAP STATS ---");
        System.out.println("Total Added Locations: " + locations.size());
        
        int totalEdges = 0;
        for (List<Edge> edgeList : edges.values()) {
            totalEdges += edgeList.size();
        }
    
        System.out.println("Total Added Edges:" + totalEdges);
        System.out.println("-----------------------------");
    }

    //Usage: to find sample Ids for testing
    public void printSampleIds() {
        System.out.println("--- 5 ID FOR TEST ---");
        int count = 0;
        for (Long id : locations.keySet()) {
            System.out.println("Copy ID: " + id + "L");
            count++;
            if (count >= 5) break;
        }
        System.out.println("----------------------------");
    }

    //Usage: to find sample Ids which are connected
    public void printConnectedSampleIds() {
        System.out.println("--- GUARANTEED CONNECTED SAMPLE IDs ---");
        for (Location loc : locations.values()) {
            List<Edge> ways = edges.get(loc);
            
            if (ways != null && ways.size() > 0) {
                Location neighbour = ways.get(0).getTo();
                List<Edge> neighbourWays = edges.get(neighbour);
                

                if (neighbourWays != null && neighbourWays.size() > 1) {
                    Location target = neighbourWays.get(1).getTo();
                    
                    if (loc.getId() != target.getId()) {
                        System.out.println("Connected Start ID: " + loc.getId() + "L");
                        System.out.println("Connected Target ID: " + target.getId() + "L");
                        break;
                    }
                }
            }
        }
        System.out.println("--------------------------------------------");
    }
    
}
