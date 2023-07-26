package org.codeintelligence.utils;

import org.codeintelligence.models.Road;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FileUtils {

    public static void generateTestInputFile(int numEntries, String inputFilePathForXML) throws IOException {

        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePathForXML))) {
            for (int i = 1; i <= numEntries; i++) {
                String name = "Road " + i;
                String country = "Country " + (char) ('A' + random.nextInt(26)); // Random country from A to Z
                double length = 50 + random.nextDouble() * 300; // Random length between 50 and 350
                Integer speedLimit = random.nextInt(101) + 50; // Random speed limit between 50 and 150
                String roadType = "Road Type " + random.nextInt(5); // Random road type from 0 to 4
                double elevation = random.nextDouble() * 1000; // Random elevation between 0 and 1000
                writer.write(name + "," + country + "," + length + "," + speedLimit + "," + roadType + "," + elevation);
                writer.newLine();
            }
        }
    }

    public static List<Road> getRoads(String inputFilePath) throws IOException {
        List<Road> roadsList = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String country = data[1].trim();
                Double length = Double.parseDouble(data[2].trim());
                Integer speedLimit = Integer.parseInt(data[3].trim());
                String roadType = data[4].trim();
                Double elevation = Double.parseDouble(data[5].trim());

                Road road = new Road(name, country, length, speedLimit, roadType, elevation);
                roadsList.add(road);
            }
        }
        return roadsList;
    }

}
