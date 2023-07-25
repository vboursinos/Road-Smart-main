import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import org.codeintelligence.processing.SpeedAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SpeedAnalyzerTest {

    private SpeedAnalyzer underTest = new SpeedAnalyzer();

    @Test
    public void checkSizes(){

        List expected = Arrays.asList(0,1,2);
        List actual = underTest.speedLimitConverter(new int[]{0,1,2});

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

        long startTime = System.currentTimeMillis();
        List<Integer> result = underTest.speedLimitConverter(input);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // The test should take at least 10 seconds (10000 milliseconds).
//        Assert.assertTrue("Test took less than 10 seconds", elapsedTime >= 10000);

        Assert.assertEquals(expectedOutput, result);
    }
}
