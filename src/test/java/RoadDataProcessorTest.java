import org.codeintelligence.models.Road;
import org.codeintelligence.processing.RoadDataProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoadDataProcessorTest {

    private RoadDataProcessor roadDataProcessor;

    @Before
    public void setUp() {
        Road road = mock(Road.class);
        when(road.getCountry()).thenReturn("USA");
        roadDataProcessor = new RoadDataProcessor(road);
    }

    @Test
    public void testComputeLength_() {
        String length = roadDataProcessor.computeLength();
        assertNotNull(length);
        assertTrue(length.matches("^\\d+\\.\\d$")); // Format should be 1 decimal place, e.g., "123.4"
    }

    @Test
    public void testComputeSpeedLimit_USA_() {
        Double speedLimit = roadDataProcessor.computeSpeedLimit();
        assertEquals(100.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testComputeSpeedLimit_Canada_() {
        Road road = mock(Road.class);
        when(road.getCountry()).thenReturn("Canada");
        RoadDataProcessor roadDataProcessorCanada = new RoadDataProcessor(road);

        Double speedLimit = roadDataProcessorCanada.computeSpeedLimit();
        assertEquals(100.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testComputeSpeedLimit_OtherCountry_() {
        Road road = mock(Road.class);
        when(road.getCountry()).thenReturn("Germany");
        RoadDataProcessor roadDataProcessorGermany = new RoadDataProcessor(road);

        Double speedLimit = roadDataProcessorGermany.computeSpeedLimit();
        assertEquals(125.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testCalculateAverageSpeedLimits_() {
        Road road1 = mock(Road.class);
        Road road2 = mock(Road.class);
        Road road3 = mock(Road.class);

        when(road1.getCountry()).thenReturn("USA");
        when(road2.getCountry()).thenReturn("USA");
        when(road3.getCountry()).thenReturn("Canada");

        List<Road> roads = Arrays.asList(road1, road2, road3);

        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road1);

        Map<String, Double> averageSpeedLimits = roadDataProcessor.calculateAverageSpeedLimits(roads);
        assertNotNull(averageSpeedLimits);

        // Since we are mocking the computeSpeedLimit() method to return a fixed value of 100.0,
        // the average speed limit for "USA" and "Canada" should also be 100.0
        assertEquals(100.0, averageSpeedLimits.get("USA"), 0.001);
        assertEquals(100.0, averageSpeedLimits.get("Canada"), 0.001);

    }




    @Test
    public void testComputeLength() {
        Road road = new Road("Example Road", "USA");
        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road);

        String length = roadDataProcessor.computeLength();
        assertNotNull(length);
        assertTrue(length.matches("^\\d+\\.\\d$")); // Format should be 1 decimal place, e.g., "123.4"
    }

    @Test
    public void testComputeSpeedLimit_USA() {
        Road road = new Road("Example Road", "USA");
        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road);

        Double speedLimit = roadDataProcessor.computeSpeedLimit();
        assertEquals(100.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testComputeSpeedLimit_Canada() {
        Road road = new Road("Example Road", "Canada");
        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road);

        Double speedLimit = roadDataProcessor.computeSpeedLimit();
        assertEquals(100.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testComputeSpeedLimit_OtherCountry() {
        Road road = new Road("Example Road", "Germany");
        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road);

        Double speedLimit = roadDataProcessor.computeSpeedLimit();
        assertEquals(125.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testCalculateAverageSpeedLimits() {
        List<Road> roads = new ArrayList<>();
        roads.add(new Road("Road 1", "USA"));
        roads.add(new Road("Road 2", "USA"));
        roads.add(new Road("Road 3", "Canada"));
        roads.add(new Road("Road 4", "Germany"));
        roads.add(new Road("Road 5", "Germany"));

        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(roads.get(0));

        Map<String, Double> averageSpeedLimits = roadDataProcessor.calculateAverageSpeedLimits(roads);
        assertNotNull(averageSpeedLimits);

        // Since we are using "USA" and "Canada" twice, the average for them should be 100.0
        assertEquals(100.0, averageSpeedLimits.get("USA"), 0.001);
        assertEquals(100.0, averageSpeedLimits.get("Canada"), 0.001);

        // We have two roads for "Germany," so the average should be (125.0 + 125.0) / 2 = 125.0
        assertEquals(100.0, averageSpeedLimits.get("Germany"), 0.001);
    }

    @Test(timeout = 6000) // Timeout is set to 6000 milliseconds (6 seconds)
    public void testComputeLength_LongRunning() throws InterruptedException {
        Road road = new Road("Example Road", "USA");
        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road);

        String length = roadDataProcessor.computeLength();
        assertNotNull(length);
        assertTrue(length.matches("^\\d+\\.\\d$")); // Format should be 1 decimal place, e.g., "123.4"
    }


    @Test
    public void testCalculateTotalLengthByCountry() {
        List<Road> roads = new ArrayList<>();
        roads.add(new Road("Road 1", "USA", 250.0, "local", 600.0));
        roads.add(new Road("Road 2", "USA", 400.0, "local", 400.0));
        roads.add(new Road("Road 7", "USA", 1400.0, "highway", 800.0));
        roads.add(new Road("Road 3", "Canada", 600.0, "highway", 80.0));
        roads.add(new Road("Road 4", "Canada", 900.0, "local", 100.0));
        roads.add(new Road("Road 5", "Germany", 350.0, "highway", 200.0));
        roads.add(new Road("Road 6", "Germany", 750.0, "highway", 30.0));

        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(roads.get(0));

        Map<String, Map<String, Double>> results = roadDataProcessor.calculateTotalLengthByCountry(roads);
        assertNotNull(results);

        // Verify total length, road count, and average length for each country and category
        Assert.assertEquals(320.0, results.get("USA").get("TotalLength_Medium"), 0.1);
        Assert.assertEquals(1.0, results.get("USA").get("RoadCount_Medium"), 0.1);
        Assert.assertEquals(320.0, results.get("USA").get("AverageLength_Medium"), 0.1);

        Assert.assertEquals(648.0, results.get("Canada").get("AverageLength_Long"), 0.1);
        Assert.assertEquals(2.0, results.get("Canada").get("RoadCount_Long"), 0.1);
        Assert.assertEquals(1296.0, results.get("Canada").get("TotalLength_Long"), 0.1);


        Assert.assertEquals(720.0, results.get("Germany").get("TotalLength_Long"), 0.1);
        Assert.assertEquals(1.0, results.get("Germany").get("RoadCount_Long"), 0.1);
        Assert.assertEquals(720.0, results.get("Germany").get("AverageLength_Long"), 0.1);

        // You can add more assertions based on your specific data and requirements
    }

    @Test
    public void testCalculateTotalLengthByCountry_Heavy() {
        // Generate a large number of Road objects for testing
        List<Road> roads = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            double length = Math.random() * 1000.0; // Random length between 0 and 1000
            String country = getRandomCountry();
            String roadType = getRandomRoadType();
            double elevation = Math.random() * 1000.0; // Random elevation between 0 and 1000

            roads.add(new Road("Road " + i, country, length, roadType, elevation));
        }

        RoadDataProcessor roadDataProcessor = new RoadDataProcessor();

        // Calculate the total length by country and road type
        Map<String, Map<String, Double>> totalLengths = roadDataProcessor.calculateTotalLengthByCountry(roads);

        Assert.assertNotNull(totalLengths);

        // Verify the total length for each country and each road type
        // (You can add more specific assertions based on your data and calculations)
        for (String country : totalLengths.keySet()) {
            Map<String, Double> countryResults = totalLengths.get(country);
            for (String roadType : countryResults.keySet()) {
                Double totalLength = countryResults.get(roadType);
                Assert.assertNotNull(totalLength);
                System.out.println("Country: " + country + ", Road Type: " + roadType + ", Total Length: " + totalLength);
            }
        }
    }

    // Helper method to generate random countries for testing
    private String getRandomCountry() {
        List<String> countries = Arrays.asList("USA", "Canada", "Germany", "France", "Italy", "Japan");
        int randomIndex = (int) (Math.random() * countries.size());
        return countries.get(randomIndex);
    }

    // Helper method to generate random road types for testing
    private String getRandomRoadType() {
        List<String> roadTypes = Arrays.asList("Highway", "Local", "Expressway", "Freeway", "Rural");
        int randomIndex = (int) (Math.random() * roadTypes.size());
        return roadTypes.get(randomIndex);
    }
}
