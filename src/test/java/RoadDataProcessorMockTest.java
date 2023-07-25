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

public class RoadDataProcessorMockTest {

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
    public void testCalculateAverageSpeedLimits() {
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
    public void testComputeLength_WithNegativeValues() {
        Road road = mock(Road.class);
        when(road.getLength()).thenReturn(-200.0);
        RoadDataProcessor roadDataProcessorNegative = new RoadDataProcessor(road);

        String length = roadDataProcessorNegative.computeLength();
        assertNotNull(length);
        assertTrue(length.matches("^\\d+\\.\\d$")); // Format should be 1 decimal place, e.g., "123.4"
    }

    @Test
    public void testComputeSpeedLimit_InvalidCountry_() {
        Road road = mock(Road.class);
        when(road.getCountry()).thenReturn("InvalidCountry");
        RoadDataProcessor roadDataProcessorInvalidCountry = new RoadDataProcessor(road);

        Double speedLimit = roadDataProcessorInvalidCountry.computeSpeedLimit();
        assertEquals(125.0, speedLimit, 0.001); // Double comparison with a tolerance of 0.001
    }

    @Test
    public void testCalculateTotalLengthByCountry() {
        Road road1 = mock(Road.class);
        Road road2 = mock(Road.class);
        Road road3 = mock(Road.class);

        when(road1.getCountry()).thenReturn("USA");
        when(road1.getLength()).thenReturn(150.0);
        when(road1.getRoadType()).thenReturn("local");
        when(road1.getElevation()).thenReturn(100.0);

        when(road2.getCountry()).thenReturn("Canada");
        when(road2.getLength()).thenReturn(300.0);
        when(road2.getRoadType()).thenReturn("highway");
        when(road2.getElevation()).thenReturn(700.0);

        when(road3.getCountry()).thenReturn("USA");
        when(road3.getLength()).thenReturn(500.0);
        when(road3.getRoadType()).thenReturn("local");
        when(road3.getElevation()).thenReturn(400.0);

        List<Road> roads = Arrays.asList(road1, road2, road3);

        RoadDataProcessor roadDataProcessor = new RoadDataProcessor(road1);

        Map<String, Map<String, Double>> resultsByCountry = roadDataProcessor.calculateTotalLengthByCountry(roads);
        assertNotNull(resultsByCountry);
        assertEquals(2, resultsByCountry.size());

        // Check results for "USA"
        Map<String, Double> usaResults = resultsByCountry.get("USA");
        assertNotNull(usaResults);
        assertEquals(6, usaResults.size());
        assertEquals(120.0, usaResults.getOrDefault("TotalLength_Short", 0.0), 0.001);
        assertEquals(400.0, usaResults.getOrDefault("TotalLength_Medium", 0.0), 0.001);
        assertEquals(0.0, usaResults.getOrDefault("TotalLength_Long", 0.0), 0.001);
        assertEquals(1.0, usaResults.getOrDefault("RoadCount_Short", 0.0), 0.001);
        assertEquals(1.0, usaResults.getOrDefault("RoadCount_Medium", 0.0), 0.001);
        assertEquals(0.0, usaResults.getOrDefault("RoadCount_Long", 0.0), 0.001);
        assertEquals(120.0, usaResults.getOrDefault("AverageLength_Short", 0.0), 0.001);
        assertEquals(400.0, usaResults.getOrDefault("AverageLength_Medium", 0.0), 0.001);
        assertEquals(0, usaResults.getOrDefault("AverageLength_Long", 0.0), 0.001);

        // Check results for "Canada"
        Map<String, Double> canadaResults = resultsByCountry.get("Canada");
        assertNotNull(canadaResults);
        assertEquals(3, canadaResults.size());
        assertEquals(0.0, canadaResults.getOrDefault("TotalLength_Short", 0.0), 0.001);
        assertEquals(468.0, canadaResults.getOrDefault("TotalLength_Medium", 0.0), 0.001);
        assertEquals(0.0, canadaResults.getOrDefault("TotalLength_Long", 0.0), 0.001);
        assertEquals(0.0, canadaResults.getOrDefault("RoadCount_Short", 0.0), 0.001);
        assertEquals(1.0, canadaResults.getOrDefault("RoadCount_Medium", 0.0), 0.001);
        assertEquals(0.0, canadaResults.getOrDefault("RoadCount_Long", 0.0), 0.001);
        assertEquals(0.0, canadaResults.getOrDefault("AverageLength_Short", 0.0), 0.001);
        assertEquals(468.0, canadaResults.getOrDefault("AverageLength_Medium", 0.0), 0.001);
        assertEquals(0.0, canadaResults.getOrDefault("AverageLength_Long", 0.0), 0.001);
    }
    @Test
    public void testCalculateAverageSpeedLimits_SameCountryDifferentSpeedLimits() {
        Road road1 = mock(Road.class);
        Road road2 = mock(Road.class);

        when(road1.getCountry()).thenReturn("Germany");
        when(road2.getCountry()).thenReturn("Germany");

        when(road1.getSpeedLimit()).thenReturn(80);
        when(road2.getSpeedLimit()).thenReturn(100);

        List<Road> roads = Arrays.asList(road1, road2);

        RoadDataProcessor roadDataProcessorSameCountry = new RoadDataProcessor(road1);

        Map<String, Double> averageSpeedLimits = roadDataProcessorSameCountry.calculateAverageSpeedLimits(roads);
        assertNotNull(averageSpeedLimits);
        assertEquals(1, averageSpeedLimits.size());
        assertEquals(125.0, averageSpeedLimits.get("Germany"), 0.001); // Average of 80.0 and 100.0
    }
}
