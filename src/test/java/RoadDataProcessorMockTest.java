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
}
