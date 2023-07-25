package org.codeintelligence.processing;

import org.codeintelligence.models.Road;
import org.junit.Assert;

import java.util.*;

public class RoadDataProcessor {

    private Road road;

    public RoadDataProcessor(Road road){
        this.road = road;
    }

    public RoadDataProcessor(){
    }

    public String computeLength(){
        String name = road.getName();
        Random random = new Random();

        return String.format("%.1f", 100.0 + (1000.0)*random.nextDouble());
    }

    public String computeRoadLength(Road road) {
        double lengthMultiplier = 1.0; // A factor that varies based on road type
        double elevationMultiplier = 1.0; // A factor that varies based on elevation

        // Adjust length multiplier based on road type
        switch (road.getRoadType().toLowerCase()) {
            case "highway":
                lengthMultiplier = 1.2;
                break;
            case "local":
                lengthMultiplier = 0.8;
                break;
            // Add more cases for other road types as needed
            default:
                lengthMultiplier = 1.0;
                break;
        }

        // Adjust length multiplier based on elevation
        if (road.getElevation() > 500.0) {
            elevationMultiplier = 1.3;
        } else if (road.getElevation() < 100.0) {
            elevationMultiplier = 0.8;
        }

        // Calculate the final road length based on factors
        return String.valueOf(road.getLength() * lengthMultiplier * elevationMultiplier);
    }

    public Double computeSpeedLimit(){
        String country = road.getCountry();

        if (country.equals("")){
            System.out.println("ERROR: Invalid country");
            return 0.0;
        }
        else if (country.toLowerCase().equals("usa") || country.toLowerCase().equals("canada")){
            return 100.0;
        }
        else {
            return 125.0;
        }
    }

    public Map<String, Double> calculateAverageSpeedLimits(List<Road> roads) {
        Map<String, List<Double>> speedLimitsByCountry = new HashMap<>();
        Map<String, Double> averageSpeedLimits = new HashMap<>();

        for (Road road : roads) {
            String country = road.getCountry();
            Double speedLimit = computeSpeedLimit();

            speedLimitsByCountry.computeIfAbsent(country, k -> new ArrayList<>()).add(speedLimit);
        }

        for (Map.Entry<String, List<Double>> entry : speedLimitsByCountry.entrySet()) {
            String country = entry.getKey();
            List<Double> speedLimits = entry.getValue();
            Double averageSpeedLimit = calculateAverage(speedLimits);
            averageSpeedLimits.put(country, averageSpeedLimit);
        }

        return averageSpeedLimits;
    }

    private Double calculateAverage(List<Double> values) {
        if (values.isEmpty()) {
            return 0.0;
        }

        Double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }

        return sum / values.size();
    }

    public Map<String, Map<String, Double>> calculateTotalLengthByCountry(List<Road> roads) {
        Map<String, Map<String, Double>> resultsByCountry = new HashMap<>();

        for (Road road : roads) {
            String country = road.getCountry();
            double roadLength = Double.parseDouble(computeRoadLength(road));

            // Categorize the road based on length thresholds
            String category;
            if (roadLength < 300.0) {
                category = "Short";
            } else if (roadLength < 500.0) {
                category = "Medium";
            } else {
                category = "Long";
            }

            // Update the total length and road count for the category in the country's map
            resultsByCountry.putIfAbsent(country, new HashMap<>());
            Map<String, Double> countryResults = resultsByCountry.get(country);
            countryResults.put("TotalLength_" + category, countryResults.getOrDefault("TotalLength_" + category, 0.0) + roadLength);
            countryResults.put("RoadCount_" + category, countryResults.getOrDefault("RoadCount_" + category, 0.0) + 1.0);
        }

        // Calculate the average length for each category in each country
        for (Map.Entry<String, Map<String, Double>> countryEntry : resultsByCountry.entrySet()) {
            Map<String, Double> countryResults = countryEntry.getValue();
            List<String> categories = Arrays.asList("Short", "Medium", "Long");
            for (String category : categories) {
                Double totalLength = countryResults.getOrDefault("TotalLength_" + category, 0.0);
                Double roadCount = countryResults.getOrDefault("RoadCount_" + category, 0.0);

                if (roadCount > 0) {
                    Double averageLength = totalLength / roadCount;
                    countryResults.put("AverageLength_" + category, averageLength);
                }
            }
        }

        return resultsByCountry;
    }
}
