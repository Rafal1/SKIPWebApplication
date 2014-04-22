package SKIPWebApplication.receiveinformation;

import org.junit.Test;
import returnobjects.Coordinates;
import returnobjects.Driver;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriverTest {
    @Test
    public void testGetDriversList() throws Exception {

    }

    @Test
    public void testAddDriver() throws Exception {

    }

    @Test
    public void testChangeDriver() throws Exception {
        ArrayList<Driver> arr = ReceiveDriver.getDriversList();
        Long ID = new Long(4);
        Driver oldDr = arr.get(ID.intValue());

        Driver dr = new Driver();
        dr.setFirstName("marek");
        dr.setLastName("jak");
        dr.setPhoneNumber("664587789");
        dr.setLatestCoordinates(new Coordinates(15.1, 25.1));
        dr.setCoordinatesUpdateDate(new Date());
        dr.setEmail("ja@op.pl");
        dr.setPhoneNumber2("123456789");
        ReceiveDriver.changeDriver(dr, ID);
        if (arr.contains(dr)) {
            arr.set(ID.intValue(), oldDr);
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteDriver() throws Exception {

    }

    @Test
    public void testGetDriver() throws Exception {

    }

    @Test
    public void testUpdateDriverCoordinates() throws Exception {

    }
}
