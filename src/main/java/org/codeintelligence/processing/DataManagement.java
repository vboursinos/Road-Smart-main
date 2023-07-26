package org.codeintelligence.processing;

import org.codeintelligence.models.Road;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManagement {
    public List<Road> findTop10LongestRoadsFromFile(String filePath, int number0fLongest) throws IOException {

        List<Road> roads = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String name = parts[0];
                    String country = parts[1];
                    double length = Double.parseDouble(parts[2]);
                    Integer speedLimit = Integer.parseInt(parts[3]);
                    String roadType = parts[4];
                    double elevation = Double.parseDouble(parts[5]);
                    roads.add(new Road(name, country, length, speedLimit, roadType, elevation));
                }
            }
        }

        Collections.sort(roads, (r1, r2) -> Double.compare(r2.getLength(), r1.getLength()));

        int numToRetrieve = Math.min(number0fLongest, roads.size());
        return roads.subList(0, numToRetrieve);
    }
}
