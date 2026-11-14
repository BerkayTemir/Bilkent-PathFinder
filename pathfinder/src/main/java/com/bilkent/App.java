package com.bilkent;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class App 
{
    public static void main( String[] args )
    {
        CampusGraph campus = new CampusGraph();
        Calculator calc = new Calculator();

        String overpassQuery = "[out:json];(way[highway=footway](39.86,32.73,39.88,32.76);>;);out;";
        String encodedQuery = URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8);
        String requestURL = "https://overpass-api.de/api/interpreter?data=" + encodedQuery;
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(requestURL))
            .GET()
            .build();
        HttpClient client = HttpClient.newHttpClient();
        try{
            System.out.println("Connecting to API...");

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Answer came");
            
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray elements = jsonResponse.getJSONArray("elements");
            ArrayList<String> namesList = new ArrayList();

            for(int i = 0; i < elements.length(); i++){
                JSONObject element = elements.getJSONObject(i);
                String type = element.getString("type");

                //Adding nodes to campus
                if(type.equals("node")){
                    long id = element.getLong("id");
                    double lat = element.getDouble("lat");
                    double lon = element.getDouble("lon");
                    String name = "No Name";
                    if(element.has("tags")){
                        JSONObject tags = element.getJSONObject("tags");
                        name = tags.optString("name", "No Name");
                    }
                    Location newNode = new Location(id, lat, lon, name);
                    campus.addLocation(newNode);
                }
            }

            for(int i = 0; i < elements.length(); i++){
                JSONObject element = elements.getJSONObject(i);
                String type = element.getString("type");

                //Adding edges to campus
                if(type.equals("way")){
                    JSONArray nodes = element.getJSONArray("nodes");

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

            campus.printGraphStats();
            long baslangicID = 2190672611L;
            long hedefID = 4230186078L;

            List<Location> rota = campus.findShortestPath(baslangicID, hedefID);

            System.out.println("--- DIJKSTRA ROUTE TEST ---");
            if (rota.isEmpty()) {
                System.out.println("There is no route between this points");
            } 
            else 
            {
                for(Location loc : rota) {
                    System.out.println(" -> " + loc.getId()); 
                }
            }
            
            System.out.println("MAP is done with success");
            
        }
        catch (Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
