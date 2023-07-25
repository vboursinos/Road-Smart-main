import org.codeintelligence.database.InformationDatabase;
import org.codeintelligence.processing.DataOutput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class DataOutputTest {

    @Mock
    private InformationDatabase mockDb;

    @Mock
    private ResultSet mockResultSet;

    private DataOutput dataOutput;

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
}
