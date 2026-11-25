package com.bilkent;

import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        MapDataLoader loader = new MapDataLoader();
        CampusGraph campus = loader.loadBilkentMap();
        Calculator calc = new Calculator();

        // Örneğin merkezde bir yoldan (startId) en yakın kafeyi arıyoruz
        long testStartId = 2190672611L; // Senin önceki loglarında çalışan geçerli bir ID
        List<Location> cafeRoute = campus.findPathtoNearestSearchedLocation(testStartId, LocationType.RING_STOP);

        System.out.println("--- EN YAKIN KAFE BULUCU ---");
        if (cafeRoute.isEmpty()) {
            System.out.println("Hata: Ulaşılabilir bir kafe bulunamadı!");
        } else {
            Location cafe = cafeRoute.get(cafeRoute.size() - 1);
            System.out.println("DISTANCE: " + calc.realDistance(campus.getLocation(testStartId), cafe));
            System.out.println("Hedef Bulundu: " + cafeRoute.get(cafeRoute.size() - 1).getName());
            System.out.println("Adım Adım Rota:");
            for (Location loc : cafeRoute) {
                System.out.println(" -> " + (loc.getName().equals("No Name") ? "Yol Ayrımı" : loc.getName()));
            }
        }
                    
            }
}
