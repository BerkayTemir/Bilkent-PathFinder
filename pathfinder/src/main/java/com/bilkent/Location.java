package com.bilkent;

public class Location {

    private long id;
    private double x;
    private double y;
    private String name;
    public Location(long id, double x, double y, String name){
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override 
    public String toString(){
        return "Name: " + name + "\nId: " + id + "\nx: " + x + "\ny: " + y;
    }
    
}
