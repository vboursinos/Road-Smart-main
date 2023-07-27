package org.codeintelligence.processing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class SpeedAnalyzer {

    public List speedLimitConverter(int[] limitsInKilometers) {
        ArrayList<Integer> integers = new ArrayList<>();
        ArrayList<Integer> output = new ArrayList<>();
        int pos = 0;
        while (pos >= 0 && pos < limitsInKilometers.length) {
            integers.add(limitsInKilometers[pos]);
            pos = limitsInKilometers[pos] + 1;
        }
        for (int i: integers){
            output.add(Math.floorDiv(i, 2));
        }
        return output;
    }

    public List<Double> speedLimitConverterInMiles(int[] limitsInKilometers) {
        LinkedList<Integer> linkedList = new LinkedList<>();
        TreeMap<Integer, Double> treeMap = new TreeMap<>();

        int pos = 0;
        while (pos >= 0 && pos < limitsInKilometers.length) {
            linkedList.add(limitsInKilometers[pos]);
            pos = limitsInKilometers[pos] + 1;
        }

        for (int i : linkedList) {
            double milesPerHour = i * 0.621371;
            treeMap.put(i, milesPerHour);
        }

        return new ArrayList<>(treeMap.values());
    }


}
