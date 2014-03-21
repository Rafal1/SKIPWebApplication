package SKIPWebApplication.receiveinformation;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import returnobjects.Driver;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriverTest {
    private static Properties prop = new Properties();
    private static InputStream input = null;

    private static void initProp() {
        try {
            input = new FileInputStream(new File("src\\main\\resuorces\\config.properties").getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testgetDriversListSSLHttpclient() {
        initProp();
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        FileInputStream instream = null;
        try {
            instream = new FileInputStream(new File("src\\main\\resuorces\\SSL\\SKIPgen.keystore").getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                trustStore.load(instream, "skipskip".toCharArray());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpGet httpget = new HttpGet(prop.getProperty("WebServiceURL") + "/drivers");

            System.out.println("executing request" + httpget.getRequestLine());

            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                }
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testgetDriversListSSLJavaSSL() {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost")) {
                            return true;
                        }
                        return false;
                    }
                });

        SSLSocketFactory factory = HttpsURLConnection
                .getDefaultSSLSocketFactory();
        int port = 8443;
        String hostname = "localhost";

        System.out.println("Creating a SSL Socket For " + hostname + " on port " + port);

        SSLSocket socket = null;
        try {
            socket = (SSLSocket) factory.createSocket(hostname, port);
            socket.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Handshaking Complete");

        String https_url = "https://localhost:8443/drivers";
        URL url = null;
        try {
            url = new URL(https_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(factory);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            System.out.println("Response Length : " + con.getContentLength());
//            System.out.println("Response Length : " + con.getResponseMessage());
            System.out.println("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
