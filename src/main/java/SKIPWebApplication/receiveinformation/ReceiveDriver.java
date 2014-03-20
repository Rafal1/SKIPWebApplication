package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.client.RestTemplate;
import returnobjects.Driver;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriver {
    private static Properties prop = new Properties();
    private static InputStream input = null;

    public static void initProp() {
        try {
            input = new FileInputStream("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(prop.getProperty("WebServiceURL"));

    }

    public static ArrayList<Driver> getDriversList() {
        initProp();
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        FileInputStream instream = null;
//        try {
        instream = null; //new FileInputStream(new File("my.keystore"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            try {
                trustStore.load(instream, null); //"nopassword".toCharArray(
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        } finally {
//            try {
//                instream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
        return null;
    }

    public static ArrayList<Driver> getDriversListX() {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        unitsString = restTemplate.getForObject(prop.getProperty("WebServiceURL") + "/drivers", String.class);
        try {
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Driver>>() {
            });
        } catch (IOException e) {
            System.out.print("Parsing array error");
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String addDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny (unitString)
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("json", drJSON));

        String url = prop.getProperty("WebServiceURL") + "/drivers";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httppost.setEntity(new UrlEncodedFormEntity(params));
            return httpclient.execute(httppost, responseHandler);
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

//    public static void changeDriver(Long ID) { //na razie nie ma
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.put("http://localhost:8080/drivers/{id}", Driver.class, ID);
//        restTemplate.put("http://localhost:8080/drivers/{id}", ID);
//    }

    public static String deleteDriver(Long ID) {
        String url = prop.getProperty("WebServiceURL") + "/drivers" + ID;
        HttpClient httpclient = new DefaultHttpClient();
        HttpDelete delQuery = new HttpDelete(url);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            return httpclient.execute(delQuery, responseHandler);
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static Driver getDriver(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Driver stream = restTemplate.getForObject(prop.getProperty("WebServiceURL") + "/drivers/{id}", Driver.class, id);
        return stream;
    }

}