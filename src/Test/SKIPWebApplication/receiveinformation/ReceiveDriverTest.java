package SKIPWebApplication.receiveinformation;

import org.junit.Test;
import returnobjects.Driver;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriverTest {

    @Test
    public void testAddReadDelDriver() throws Exception { //testy dla wbudowanej bazy testowej, dla prawdziwej mogą nie działać
        Date date = new Date();
        Driver exDr = new Driver("Adam", "Zapolski");
        exDr.setEmail("r@op.pl");
        exDr.setLatestCoordinates("N20.0000000 W132.0000000");
        exDr.setPhoneNumber("229997845");
        exDr.setPhoneNumber2("48789456123");
        exDr.setCoordinatesUpdateDate(date);
        String res = ReceiveDriver.addDriver(exDr);
        ArrayList<Driver> resultList = ReceiveDriver.getDriversList();
        if(res.equals("")){
            System.out.println("pusty, nie dodano kierowcy");
            assertTrue(false);
        }
        assertEquals("Adam", resultList.get(0).getFirstName());
        assertEquals("Zapolski", resultList.get(0).getLastName());

        resultList = ReceiveDriver.getDriversList();
        String resDel = ReceiveDriver.deleteDriver(resultList.get(0).getId());
        if(resDel.equals("")){
            System.out.println("pusty, nie usunięto kierowcy");
            assertTrue(false);
        }
        resultList = ReceiveDriver.getDriversList();

        assertEquals(1, resultList.size());
    }

    @Test
    public void testGetDriversList() throws Exception {

    }

    @Test
    public void testAddDriver() throws Exception {

    }

//    @Test //not implemented yet
//    public void testChangeDriver() throws Exception {
//        Date date = new Date();
//        Driver exDr = new Driver("REAL", "Zapolski");
//        exDr.setEmail("@op.pl");
//        exDr.setLatestCoordinates("N20.0000000 W132.0000000");
//        exDr.setPhoneNumber("229997845");
//        exDr.setPhoneNumber2("48789456123");
//        exDr.setCoordinatesUpdateDate(date);
//        ReceiveDriver.addDriver(exDr);
//        Long nr = new Long("14");
//        ReceiveDriver.changeDriver(nr);
//    }

    @Test
    public void testDeleteDriver() throws Exception {
//        ReceiveDriver.deleteDriver((long) 1);
    }

    @Test
    public void testGetDriver() throws Exception {
        long ID = new Long("16");
        Driver dr = ReceiveDriver.getDriver(new Long("16"));
        assertEquals(ID, dr.getId());
    }
}
