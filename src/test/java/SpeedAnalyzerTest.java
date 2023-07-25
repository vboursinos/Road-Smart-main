import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import org.codeintelligence.processing.SpeedAnalyzer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SpeedAnalyzerTest {

    private SpeedAnalyzer underTest = new SpeedAnalyzer();

    @Test
    public void checkSizes() {

        List expected = Arrays.asList(0, 1, 2);
        List actual = underTest.speedLimitConverter(new int[]{0, 1, 2});

        Assert.assertEquals(expected.size(), actual.size());
    }

    @FuzzTest
    void fuzzComplexProcessing(FuzzedDataProvider data) {
        SpeedAnalyzer underTest = new SpeedAnalyzer();

        underTest.speedLimitConverter(data.consumeInts(10));
    }

    @Test
    public void testSpeedLimitConverter_NormalInput() {

        int[] input = {10, 15, 20, 25, 30};
        List<Integer> expectedOutput = Arrays.asList(5);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_NegativeInput() {

        int[] input = {-10, -5, 0, 5, 10};
        List<Integer> expectedOutput = Arrays.asList(-5);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_EmptyInput() {

        int[] input = {};
        List<Integer> expectedOutput = Collections.emptyList();

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_SingleElementInput() {

        int[] input = {5};
        List<Integer> expectedOutput = Arrays.asList(2);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_DuplicateValuesInput() {
        int[] input = {10, 10, 20, 20, 30, 30};
        List<Integer> expectedOutput = Arrays.asList(5);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_NegativeStepInput() {
        int[] input = {20, 18, 16, 14, 12};
        List<Integer> expectedOutput = Arrays.asList(10);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_LongRunning() {
        int[] input = {0, 100, 200, 300, 400, 500};
        List<Integer> expectedOutput = Arrays.asList(0, 50);
        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_ZeroStepInput() {
        int[] input = {10, 10, 10, 10};
        List<Integer> expectedOutput = Arrays.asList(5);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_RepeatingPatternInput() {
        int[] input = {5, 10, 15, 5, 10, 15};
        List<Integer> expectedOutput = Arrays.asList(2);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverter_LargeInput() {
        int[] input = new int[1000];
        for (int i = 0; i < 1000; i++) {
            input[i] = i * 10;
        }
        List<Integer> expectedOutput = new ArrayList<>();
        expectedOutput.add(0);
        expectedOutput.add(5);
        expectedOutput.add(55);
        expectedOutput.add(555);

        List<Integer> result = underTest.speedLimitConverter(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testSpeedLimitConverterInMiles() {
        SpeedAnalyzer speedAnalyzer = new SpeedAnalyzer();
        int[] input = {60, 70, 80, 90, 100};
        List<Double> expectedOutput = Arrays.asList(37.2826, 43.4957, 49.7088, 55.9219, 62.135);

        List<Double> actualOutput = speedAnalyzer.speedLimitConverterInMiles(input);
        // We need to use a custom epsilon for double comparison due to potential floating-point inaccuracies
        double epsilon = 0.0001;

        double actualValue = actualOutput.get(0);
        Assertions.assertEquals(37.28226, actualValue, epsilon);

    }
}
