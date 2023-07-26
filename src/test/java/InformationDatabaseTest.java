import static org.junit.Assert.*;

import org.codeintelligence.database.InformationDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.codeintelligence.models.Road;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InformationDatabaseTest {

    private InformationDatabase informationDatabase;

    @Before
    public void setUp() {
        informationDatabase = new InformationDatabase();
        informationDatabase.connect();
    }

    @After
    public void tearDown() {
        informationDatabase.close();
    }

    @Test
    public void testInsertRoadData() {
        Road road = new Road("Test Road", "Test Country", 500.0);
        assertTrue(informationDatabase.insertRoadData(road));

        // Check if the data is inserted correctly by reading all data
        ResultSet resultSet = informationDatabase.readAllData();
        assertNotNull(resultSet);

        boolean found = false;
        try {
            while (resultSet.next()) {
                if (resultSet.getString("name").equals("Test Road")) {
                    found = true;
                    assertEquals("Test Country", resultSet.getString("country"));
                    assertNotNull(resultSet.getString("length"));
                    assertEquals("125.0", resultSet.getString("speedLimit"));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue("Test Road data not found in the database", found);
    }

    @Test
    public void testReadAllData() {
        // Insert some dummy data
        Road road1 = new Road("Road 1", "Country 1", 100.0);
        Road road2 = new Road("Road 2", "Country 2", 200.0);
        informationDatabase.insertRoadData(road1);
        informationDatabase.insertRoadData(road2);

        ResultSet resultSet = informationDatabase.readAllData();
        assertNotNull(resultSet);

        try {
            // Check if both roads are present in the result set
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteRoadData() {
        // Insert a road
        Road road = new Road("Road to Delete", "Country", 300.0);
        informationDatabase.insertRoadData(road);

        // Check if the road exists in the database before deletion
        ResultSet resultSet = informationDatabase.readAllData();
        assertNotNull(resultSet);
        boolean foundBeforeDeletion = false;
        try {
            while (resultSet.next()) {
                if (resultSet.getString("name").equals("Road to Delete")) {
                    foundBeforeDeletion = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue("Road to Delete data not found in the database before deletion", foundBeforeDeletion);

        // Delete the road
        informationDatabase.deleteRoadData("Road to Delete");

        // Check if the road is no longer in the database
        resultSet = informationDatabase.readAllData();
        assertNotNull(resultSet);
        boolean foundAfterDeletion = false;
        try {
            while (resultSet.next()) {
                if (resultSet.getString("name").equals("Road to Delete")) {
                    foundAfterDeletion = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertFalse("Road to Delete data found in the database after deletion", foundAfterDeletion);
    }


    @Test
    public void testInsertAndReadLargeDataset() {
        // Insert a large dataset of roads
        int numRecords = 1000;
        for (int i = 0; i < numRecords; i++) {
            Road road = new Road("Road " + i, "Country " + i, 100.0 + i, "highway", 125.0 + i  );
            informationDatabase.insertRoadData(road);
        }

        // Read all data and verify the number of records
        ResultSet resultSet = informationDatabase.readAllData();
        assertNotNull(resultSet);

        try {
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
            }
            assertEquals(1009, rowCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertAndDeleteLargeDataset() {
        // Insert a large dataset of roads
        int numRecords = 1000;
        for (int i = 0; i < numRecords; i++) {
            Road road = new Road("Road " + i, "Country " + i, 100.0 + i, "highway", 50.0 + i);
            informationDatabase.insertRoadData(road);
        }

        // Delete all roads with even indices
        for (int i = 0; i < numRecords; i += 2) {
            informationDatabase.deleteRoadData("Road " + i);
        }

        // Read all data and verify the number of records (should be half of numRecords)
        ResultSet resultSet = informationDatabase.readAllData();
        assertNotNull(resultSet);

        try {
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
