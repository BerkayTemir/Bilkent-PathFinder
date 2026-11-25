package com.bilkent;

public class Location {

    private long id;
    private double lon;
    private double lat;
    private String name;
    private LocationType type;
    public Location(long id, double lon, double lat, String name, LocationType type){
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.name = name;
        this.type = type;
    }

    public LocationType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    @Override 
    public String toString(){
        return "Name: " + name + "\nId: " + id + "\nlon: " + lon + "\nlat: " + lat;
    }
    
}
