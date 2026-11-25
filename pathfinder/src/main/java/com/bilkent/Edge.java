package com.bilkent;

public class Edge {

    private Location from;
    private Location to;
    private double distance;

    public Edge(Location from, Location to, double distance){
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
    
    
}
