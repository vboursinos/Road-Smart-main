import org.codeintelligence.database.InformationDatabase;
import org.codeintelligence.processing.DataOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import static org.mockito.Mockito.*;

public class DataOutputTest {

    @Mock
    private InformationDatabase mockDb;

    @Mock
    private ResultSet mockResultSet;

    private DataOutput dataOutput;

    private static final int NUM_ENTRIES = 2000000;
    private static final String TEST_INPUT_FILE_PATH_FOR_XML = "src/test/resources/testInputFileToXML.txt";
    private static final String TEST_OUTPUT_XML_FILE = "src/test/resources/testOutputFile.xml";

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dataOutput = new DataOutput(mockDb);

        // Set up the mock result set with some fake data
        when(mockDb.readAllData()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString(2)).thenReturn("Road1", "Road2");
        when(mockResultSet.getString(3)).thenReturn("Country1", "Country2");
        when(mockResultSet.getString(4)).thenReturn("100", "200");
        when(mockResultSet.getString(5)).thenReturn("60", "80");
    }

    @Test
    public void testToConsole() throws SQLException {
        // Redirect System.out to capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Call the method
        dataOutput.toConsole();

        // Verify the output
        String expectedOutput = "Road1 Country1 100 60 \nRoad2 Country2 200 80 \n";
        String actualOutput = outputStream.toString();
        assert (expectedOutput.equals(actualOutput));

        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    public void testToXML() throws SQLException {
        // Mock the ResultSet and InformationDatabase interactions
        when(mockDb.readAllData()).thenReturn(mockResultSet);

        // Call the method
        dataOutput.toXML();

        // Add assertions here to check if the XML file is generated as expected
        // You can use an XML parser to read the generated file and validate its content
        // For example, you can use libraries like JUnit XMLUnit.

        // Here, I'll just assert that readAllData was called once
        verify(mockDb, times(1)).readAllData();
    }
    @Test
    public void testFromTXTFileToXML() throws IOException {
        generateTestInputFile();
        // Call the method
        dataOutput.toXML(TEST_INPUT_FILE_PATH_FOR_XML, TEST_OUTPUT_XML_FILE);

        // Add assertions here to check if the XML file is generated as expected
        // You can use an XML parser to read the generated file and validate its content
        // For example, you can use libraries like JUnit XMLUnit.

        // Here, I'll just assert that readAllData was called once
        Assert.assertTrue(new File(TEST_OUTPUT_XML_FILE).exists());
        Assert.assertTrue(new File(TEST_OUTPUT_XML_FILE).length() > 0);
        new File(TEST_INPUT_FILE_PATH_FOR_XML).delete();
        new File(TEST_OUTPUT_XML_FILE).delete();
    }

    public void generateTestInputFile() throws IOException {

        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_INPUT_FILE_PATH_FOR_XML))) {
            for (int i = 1; i <= NUM_ENTRIES; i++) {
                String name = "Road " + i;
                String country = "Country " + (char) ('A' + random.nextInt(26)); // Random country from A to Z
                double length = 50 + random.nextDouble() * 300; // Random length between 50 and 350
                Integer speedLimit = random.nextInt(101) + 50; // Random speed limit between 50 and 150
                String roadType = "Road Type " + random.nextInt(5); // Random road type from 0 to 4
                double elevation = random.nextDouble() * 1000; // Random elevation between 0 and 1000
                writer.write(name + "," + country + "," + length + "," + speedLimit + "," + roadType + "," + elevation);
                writer.newLine();
            }
        }
    }
}
