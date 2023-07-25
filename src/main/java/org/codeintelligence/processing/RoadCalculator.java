package org.codeintelligence.processing;

import java.util.ArrayList;
import java.util.List;

public class RoadCalculator {

    public static Double kilometersToMiles(Double kilometers){
        return 0.621371*kilometers;
    }

    public static List<Double> kilometersToMiles(List<Double> kilometersList){
        List milesList = new ArrayList();
        for (Double kilometers : kilometersList){
            milesList.add(kilometersToMiles(kilometers));
        }
        return milesList;
    }
    public static Double milesToKilometers(Double miles){
        return 1.60934*miles;
    }

    public static List<Double> milesToKilometers(List<Double> milesList){
        List kilometersList = new ArrayList();
        for (Double miles : milesList){
            kilometersList.add(milesToKilometers(miles));
        }
        return kilometersList;
    }

}
