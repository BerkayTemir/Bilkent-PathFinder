package com.bilkent;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapDataLoader {
    public CampusGraph loadBilkentMap(){
        Path mapFilePath = Paths.get("bilkent_map_data.json");
        String jsonData = "";

        CampusGraph campus = new CampusGraph();
        Calculator calc = new Calculator();

        try {
            if(Files.exists(mapFilePath)){
                System.out.println("Map is loading using Local File (bilkent_map_data.json)... ");
                jsonData = Files.readString(mapFilePath);
            }
            else{
                System.out.println("Couldn't Find Local File. Map is loading from API... ");

                String overpassQuery = "[out:json];(" +
                    "way[highway=footway](39.86,32.73,39.88,32.76);" +
                    "way[highway=path](39.86,32.73,39.88,32.76);" +
                    "way[highway=steps](39.86,32.73,39.88,32.76);" +
                    "way[highway=pedestrian](39.86,32.73,39.88,32.76);" +
                    "way[highway=crossing](39.86,32.73,39.88,32.76);" +
                    "node[highway=bus_stop](39.86,32.73,39.88,32.76);" + 
                    "node[amenity](39.86,32.73,39.88,32.76);" +
                    "way[building](39.86,32.73,39.88,32.76);" +
                ");out body;>;out skel qt;";
                String encodedQuery = URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8);
                String requestURL = "https://overpass-api.de/api/interpreter?data=" + encodedQuery;
                //String requestURL = "https://lz4.overpass-api.de/api/interpreter?data=" + encodedQuery;
                //String requestURL = "https://overpass.openstreetmap.fr/api/interpreter?data=" + encodedQuery;
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestURL))
                    .header("User-Agent", "BilkentPathFinderApp/1.0 (Student CS102 Project)")
                    .GET()
                    .build();
                HttpClient client = HttpClient.newHttpClient();
                
                System.out.println("Connecting to API...");

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Answer came");

                jsonData = response.body();

                //Saving and security
                if (jsonData.startsWith("{")) {
                    Files.writeString(mapFilePath, jsonData);
                    System.out.println("Map data saved to local file successfully!");
                } else {
                    System.out.println("API ERROR MESSAGE:\n" + jsonData);
                    return campus;
                }
            }

            JSONObject jsonResponse = new JSONObject(jsonData);
            JSONArray elements = jsonResponse.getJSONArray("elements");

            for(int i = 0; i < elements.length(); i++){
                JSONObject element = elements.getJSONObject(i);
                String type = element.getString("type");

                //Adding nodes to campus
                if(type.equals("node")){
                    long id = element.getLong("id");
                    double lat = element.getDouble("lat");
                    double lon = element.getDouble("lon");
                    String name = "No Name";
                    LocationType locType = LocationType.PATH_NODE;

                    if(element.has("tags")){
                        JSONObject tags = element.getJSONObject("tags");
                        name = tags.optString("name", "No Name");
                        locType = determineType(tags);
                    }
                    Location newNode = new Location(id, lon, lat, name, locType);
                    campus.addLocation(newNode);
                }
            }

            for(int i = 0; i < elements.length(); i++){
                JSONObject element = elements.getJSONObject(i);
                String type = element.getString("type");

                //Adding edges to campus
                if(type.equals("way")){

                    if(!element.has("nodes"))
                        continue;
                    JSONArray nodes = element.getJSONArray("nodes");

                    if (element.has("tags")) {
                        JSONObject tags = element.getJSONObject("tags");
                        LocationType locType = determineType(tags);
                        
                        if (locType != LocationType.PATH_NODE) {
                            long firstNodeId = nodes.getLong(0); 
                            Location cornerNode = campus.getLocation(firstNodeId);
                            long wayId = element.getLong("id"); 

                            if (cornerNode != null && campus.getLocation(wayId) == null) {
                                String name = tags.optString("name", "İsimsiz Bina");
                                Location buildingPoi = new Location(wayId, cornerNode.getLon(), cornerNode.getLat(), name, locType);
                                campus.addLocation(buildingPoi);
                            }
                        }
                    }

                    for(int j = 0; j < nodes.length() - 1; j++){
                        long fromId = nodes.getLong(j);
                        long toId = nodes.getLong(j + 1);

                        Location fromNode = campus.getLocation(fromId);
                        Location toNode = campus.getLocation(toId);

                        if(fromNode != null && toNode != null){
                            double realDist = calc.realDistance(fromNode, toNode);
                            campus.addEdge(fromNode, toNode, realDist);
                        }
                    }
                    
                }
            }
        } 
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        campus.connectPOIsToPaths(calc);

        return campus;
    }
    private LocationType determineType(JSONObject tags){
        if(tags == null || tags.isEmpty()){
            return LocationType.PATH_NODE;
        }
        if(tags.has("amenity")){
            String amenity = tags.getString("amenity");
            if(amenity.equals("cafe") || amenity.equals("restaurant") ||
             amenity.equals("fast_food") || amenity.equals("food_court") || amenity.equals("cafeteria"))
             {
                return LocationType.CAFE_DINING;
            }
            if(amenity.equals("library")){
                return LocationType.LIBRARY;
            }
        }

        if(tags.has("highway")){
            String highway = tags.getString("highway");
            if(highway.equals("bus_stop")){
                return LocationType.RING_STOP;
            }
        }

        if(tags.has("building")){
            String bldg = tags.getString("building").toLowerCase();
            String name = tags.optString("name", "").toLowerCase();
            
            if(bldg.equals("dormitory") || name.contains("yurt") || name.contains("dorm")){
                return LocationType.DORM;
            }
            return LocationType.ACADEMIC_BUILDING;
        }

        if(tags.has("name")){
            return LocationType.OTHER;
        }

        return LocationType.PATH_NODE;
    }
}

