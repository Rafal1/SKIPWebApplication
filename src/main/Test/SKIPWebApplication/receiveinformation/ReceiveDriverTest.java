package SKIPWebApplication.receiveinformation;

import org.junit.Test;
import returnobjects.Coordinates;
import returnobjects.Driver;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriverTest {
    @Test
    public void testGetDriversList() throws Exception {

    }

    @Test
    public void testAddDriver() throws Exception {
        Driver dr = new Driver();
        dr.setFirstName("marek");
        dr.setLastName("jakk");
        dr.setPhoneNumber("664587789");
        dr.setLatestCoordinates(new Coordinates(15.1, 25.1));
        dr.setCoordinatesUpdateDate(new Date());
        dr.setEmail("ja@op.pl");
        dr.setPhoneNumber2("123456789");
        String resDr = ReceiveDriver.addDriver(dr);
        System.out.println("Zwrócony string z addDriver " + resDr);
        assertTrue(resDr != null);
    }

    @Test
    public void testChangeDriver() throws Exception {
        Driver dr = new Driver();
        dr.setFirstName("marek");
        dr.setLastName("jakk");
        dr.setPhoneNumber("664587789");
        dr.setLatestCoordinates(new Coordinates(15.1, 25.1));
        dr.setCoordinatesUpdateDate(new Date());
        dr.setEmail("ja@op.pl");
        dr.setPhoneNumber2("123456789");
        Driver resDr = ReceiveDriver.changeDriver(new Long(4), dr);
        assertTrue(resDr.getEmail() == "ja@op.pl");
    }

    @Test
    public void testDeleteDriver() throws Exception {

    }

    @Test
    public void testGetDriver() throws Exception {
        Driver resDr = ReceiveDriver.getDriver(new Long(4));
        assertTrue(resDr.getLastName().equals("podolski"));
    }

    @Test
    public void testUpdateDriverCoordinates() throws Exception {
        Coordinates cor = new Coordinates(10.0, 10.0);
        Coordinates resCor = ReceiveDriver.updateDriverCoordinates(new Long(5), cor);
        assertTrue(resCor.getLatitude() == 10.0);
    }
}
