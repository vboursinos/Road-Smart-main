import org.codeintelligence.models.Road;
import org.codeintelligence.processing.DataManagement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class DataManagementTest {
    private static final int NUM_ENTRIES = 2000000;
    private static final String TEST_INPUT_FILE_PATH = "src/test/resources/testInputFile.txt";

    @Before
    public void generateTestInputFile() throws IOException {

        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_INPUT_FILE_PATH))) {
            for (int i = 1; i <= NUM_ENTRIES; i++) {
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

    @Test
    public void TestFindTopLongestRoadsFromFile(){
        DataManagement dataManagement = new DataManagement();
        try {
            List<Road> longestRoads = dataManagement.findTop10LongestRoadsFromFile(TEST_INPUT_FILE_PATH, 10);
            System.out.println(longestRoads);
            Assert.assertTrue(longestRoads.size() == 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
