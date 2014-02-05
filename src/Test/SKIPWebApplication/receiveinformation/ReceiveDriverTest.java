package SKIPWebApplication.receiveinformation;

import org.junit.Test;
import returnobjects.Driver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriverTest {

    @Test
    public void testAddAndReadDriver() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //dateFormat.format(date)
        Date date = new Date();
        Driver exDr = new Driver("Adam", "Zapolski");
        exDr.setEmail("r@op.pl");
        exDr.setLatestCoordinates("N20.0000000 W132.0000000");
        exDr.setPhoneNumber("229997845");
        exDr.setPhoneNumber2("48789456123");
        exDr.setCoordinatesUpdateDate(date);
        ReceiveDriver.addDriver(exDr);
        ArrayList<Driver> resultList = ReceiveDriver.getDriversList();
        assertEquals("Adam", resultList.get(0).getFirstName());
        assertEquals("Zapolski", resultList.get(0).getLastName());

    }

    @Test
    public void testGetDriversList() throws Exception {

    }

    @Test
    public void testAddDriver() throws Exception {

    }

    @Test
    public void testChangeDriver() throws Exception {

    }

    @Test
    public void testDeleteDriver() throws Exception {

    }

    @Test
    public void testGetDriver() throws Exception {

    }
}
