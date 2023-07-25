import org.codeintelligence.processing.RoadCalculator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

public class RoadCalculatorTest {

    @Test
    public void baseCaseKilometers(){
        double actual = RoadCalculator.kilometersToMiles(1.0);
        double expected = 0.6214;

        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void baseCaseMiles(){
        double actual = RoadCalculator.milesToKilometers(1.0);
        double expected = 1.609;

        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void zeroCaseKilometers(){
        double actual = RoadCalculator.kilometersToMiles(0.0);
        double expected = 0.0;

        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void zeroCaseMiles(){
        double actual = RoadCalculator.milesToKilometers(0.0);
        double expected = 0.0;

        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void testKilometersToMiles() {
        double epsilon = 0.0001;

        // Test single value conversion
        double kilometers = 100.0;
        double expectedMiles = 62.1371;
        double actualMiles = RoadCalculator.kilometersToMiles(kilometers);
        Assertions.assertEquals(expectedMiles, actualMiles, epsilon);

        // Test list conversion
        List<Double> kilometersList = Arrays.asList(50.0, 75.0, 100.0, 125.0, 150.0);
        List<Double> expectedMilesList = Arrays.asList(31.06855, 46.602825, 62.1371, 77.671375, 93.20565);
        List<Double> actualMilesList = RoadCalculator.kilometersToMiles(kilometersList);

        Assertions.assertEquals(expectedMilesList.size(), actualMilesList.size());
        for (int i = 0; i < expectedMilesList.size(); i++) {
            Assertions.assertEquals(expectedMilesList.get(i), actualMilesList.get(i), epsilon);
        }
    }

    @Test
    public void testMilesToKilometers() {
        double epsilon = 0.0001;

        // Test single value conversion
        double miles = 62.1371;
        double expectedKilometers = 100.0;
        double actualKilometers = RoadCalculator.milesToKilometers(miles);
        Assertions.assertTrue(actualKilometers - expectedKilometers < epsilon);

        // Test list conversion
        List<Double> milesList = Arrays.asList(31.06855, 46.602825, 62.1371, 77.671375, 93.20565);
        List<Double> expectedKilometersList = Arrays.asList(50.0, 75.0, 100.0, 125.0, 150.0);
        List<Double> actualKilometersList = RoadCalculator.milesToKilometers(milesList);

        Assertions.assertEquals(expectedKilometersList.size(), actualKilometersList.size());
        for (int i = 0; i < expectedKilometersList.size(); i++) {
            Assertions.assertTrue(actualKilometersList.get(i) - expectedKilometersList.get(i) < epsilon);
        }
    }
}
