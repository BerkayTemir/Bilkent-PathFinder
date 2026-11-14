package com.bilkent;

public class Calculator {
    public double realDistance(Location loc1, Location loc2){
        //Haversine formula
        final int R = 6371000; // Type of Meter Radius
        double lat1 = loc1.getX() * Math.PI / 180;
        double lat2 = loc2.getX() * Math.PI / 180;
        double lon1 = loc1.getY() * Math.PI / 180;
        double lon2 = loc2.getY() * Math.PI / 180;

        double a =  Math.pow(Math.sin((lat2 - lat1) / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon2 - lon1) / 2), 2)
        ;

        double d = R * 2 * Math.asin(Math.sqrt(a));
        return d;
    }
}
