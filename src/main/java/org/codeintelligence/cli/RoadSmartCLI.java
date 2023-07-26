package org.codeintelligence.cli;

import org.codeintelligence.database.InformationDatabase;
import org.codeintelligence.models.Road;
import org.codeintelligence.processing.DataOutput;
import org.codeintelligence.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RoadSmartCLI {

    InformationDatabase db;
    DataOutput output;

    private Scanner scanner;

    private final String greeting = "Welcome to Road Smart, the intelligent tool for getting road information!\n";

    private final String optionsPrompt = "Please select from the following options:\n" +
        "[C]reate new road entry\n" +
        "[R]ead all road entries\n" +
        "[D]elete road entry\n" +
        "[P]rint road entries reports\n" +
        "[Q]uit this application\n\n";

    private final String promptString = ">> ";

    public RoadSmartCLI(InformationDatabase db){
        this.db = db;
        db.connect();
        output = new DataOutput(db);
    }

    public RoadSmartCLI(){
        this(new InformationDatabase());
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting RoadSmart CLI...");

        // Start the timer
        long startTime = System.currentTimeMillis();

        String TEST_INPUT_FILE_PATH_FOR_XML = "roads.csv";
        String TEST_OUTPUT_XML_FILE = "roads.xml";
        FileUtils.generateTestInputFile(1000000, TEST_INPUT_FILE_PATH_FOR_XML);
        DataOutput dataOutput = new DataOutput();
        dataOutput.toXML(TEST_INPUT_FILE_PATH_FOR_XML, TEST_OUTPUT_XML_FILE);
        List<Road> roads = FileUtils.getRoads(TEST_INPUT_FILE_PATH_FOR_XML);
        List<Road> largestRoads = findLargerRoads(roads, 10);
        System.out.println(largestRoads);
        // Stop the timer and calculate the elapsed time
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Execution time: " + elapsedTime + " milliseconds");

        System.out.println("Generated test input file");
        new File(TEST_INPUT_FILE_PATH_FOR_XML).deleteOnExit();
        new File(TEST_OUTPUT_XML_FILE).deleteOnExit();
    }

    private static List<Road> findLargerRoads(List<Road> roads, int numOfLongest) throws IOException {
        System.out.println("Finding the " + numOfLongest + " longest roads...");
        List<Road> largestRoads = new LinkedList<>();
        Collections.sort(roads, Comparator.comparing(Road::getLength).reversed());

        if (roads.size() >= numOfLongest) {
            Road longestRoad = roads.get(0);
            Road secondLongestRoad = roads.get(1);
            System.out.println("The two longest roads are:");
            System.out.println("1. " + longestRoad);
            System.out.println("2. " + secondLongestRoad);
        } else if (roads.size() == 1) {
            Road longestRoad = roads.get(0);
            System.out.println("There is only one road:");
            System.out.println("1. " + longestRoad);
        } else {
            System.out.println("No roads found.");
        }

        for (int i = 0; i < numOfLongest; i++) {
            largestRoads.add(roads.get(i));
        }

        System.out.println("Number of roads: " + largestRoads.size());
        return largestRoads;
    }

    public void runCLI(){
        String input;
        scanner = new Scanner(System.in);
        System.out.print(greeting);

        while (true){
            System.out.print(optionsPrompt);
            System.out.print(promptString);

            input = scanner.next();
            processOption(input);
        }
    }

    private void processOption(String optionIn){

        switch (optionIn.toLowerCase()) {
            case "c": createOption(); break;
            case "r": readOption(); break;
            case "d": deleteOption(); break;
            case "p": printOption(); break;
            case "q": quitOption(); break;
            default: invalidOption(); break;
        }
    }
    private void createOption() {
        System.out.println("Create new road entry");

        System.out.print("Enter the road's name: ");
        String name = scanner.next();

        System.out.print("Enter the road's origin country: ");
        String country = scanner.next();

        db.insertRoadData(new Road(name, country));

    }
    private void readOption() {
        System.out.println("Read all roads");

        output.toConsole();
    }

    private void deleteOption() {
        System.out.println("Delete road");

        System.out.print("Enter the road's name: ");
        String name = scanner.next();

        db.deleteRoadData(name);
    }

    private void printOption(){
        System.out.println("Print roads to report");

        output.toXML();
    }

    private void quitOption(){
        db.close();
        System.out.println("Quitting...");
        System.exit(0);
    }

    private void invalidOption(){
        System.out.println("Invalid option, try again.");
    }
}
