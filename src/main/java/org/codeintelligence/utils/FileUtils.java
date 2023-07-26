package org.codeintelligence.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

}
