package SKIPWebApplication.receiveinformation;

import org.junit.Test;
import returnobjects.Driver;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriverTest {

    @Test
    public void testgetDriversListSSLMykong() {
        String https_url = "https://localhost:8443/drivers";
        URL url = null;
        try {
            url = new URL(https_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            System.out.println("Response Length : " + con.getContentLength());
            System.out.println("Response Length : " + con.getResponseMessage());
            System.out.println("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
        return;
    }

    @Test
    public void testgetDriversListSSL() {
        //ReceiveDriver.getDriversListSSL();
        assertTrue(true);
        return;
    }

    @Test
    public void testAddReadDelDriver() throws Exception { //testy dla wbudowanej bazy testowej, dla prawdziwej mogą nie działać
        Date date = new Date();
        Driver exDr = new Driver("Adam", "Zapolski");
        // exDr.setEmail("r@op.pl");
        exDr.setLatestCoordinates("N20.0000000 W132.0000000");
        exDr.setPhoneNumber("229997845");
        // exDr.setPhoneNumber2("48789456123");
        exDr.setCoordinatesUpdateDate(date);
        String res = ReceiveDriver.addDriver(exDr);
        ArrayList<Driver> resultList = ReceiveDriver.getDriversList();
        if (res.equals("")) {
            System.out.println("pusty, nie dodano kierowcy");
            assertTrue(false);
        }
        assertEquals("Adam", resultList.get(0).getFirstName());
        assertEquals("Zapolski", resultList.get(0).getLastName());

        resultList = ReceiveDriver.getDriversList();
        String resDel = ReceiveDriver.deleteDriver(resultList.get(0).getId());
        if (resDel.equals("")) {
            System.out.println("pusty, nie usunięto kierowcy");
            assertTrue(false);
        }
        resultList = ReceiveDriver.getDriversList();

        assertEquals(0, resultList.size());
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
        Date date = new Date();
        Driver exDr = new Driver("Adam", "Zapolski");
        // exDr.setEmail("r@op.pl");
        exDr.setLatestCoordinates("N20.0000000 W132.0000000");
        exDr.setPhoneNumber("229997845");
        // exDr.setPhoneNumber2("48789456123");
        exDr.setCoordinatesUpdateDate(date);
        String res = ReceiveDriver.addDriver(exDr);
        ArrayList<Driver> resultList = ReceiveDriver.getDriversList();
        if (res.equals("")) {
            System.out.println("pusty, nie dodano kierowcy");
            assertTrue(false);
        }
        assertEquals("Adam", resultList.get(0).getFirstName());
        assertEquals("Zapolski", resultList.get(0).getLastName());

        long ID = resultList.get(0).getId();
        Driver dr = ReceiveDriver.getDriver(ID);
        assertEquals(ID, dr.getId());

        resultList = ReceiveDriver.getDriversList();
        String resDel = ReceiveDriver.deleteDriver(resultList.get(resultList.size() - 1)
                .getId());
        if (resDel.equals("")) {
            System.out.println("pusty, nie usunięto kierowcy");
            assertTrue(false);
        }
    }
}
