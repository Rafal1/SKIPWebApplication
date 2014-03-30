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
import returnobjects.Driver;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Properties;

import static org.atmosphere.di.ServletContextHolder.getServletContext;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriver {
    private static Properties prop = new Properties();
    private static InputStream input = null;

    public static void initProp() {
        try {
//        File catalinaBase = new File( System.getProperty( "catalina.base" ) ).getAbsoluteFile();
//        File propertyFile = new File( catalinaBase, "src\\main\\resuorces\\config.properties" );
//        InputStream inputStream = new FileInputStream( propertyFile );
            String rootPath = getServletContext().getRealPath("/");
            InputStream inputStream = new FileInputStream(rootPath+"src\\main\\resuorces\\config.properties");
            //FileInputStream in = (FileInputStream) this.getClass().getClassLoader().getResourceAsStream("config.properties");
            //ServletContext context =
            //input = new FileInputStream(new File("src\\main\\resuorces\\config.properties").getAbsolutePath());
            input = inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        try {
//            prop.load(input);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        try {
            instream = new FileInputStream(new File("src\\main\\resuorces\\SSL\\SKIPgen.keystore").getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                trustStore.load(instream, prop.getProperty("keystorePassword").toCharArray());
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


    // v2
//        initProp();
//        ArrayList<Driver> res = null;
//        KeyStore trustStore = null;
//        try {
//            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        FileInputStream instream = null;
//        try {
//            //String rootPath = getServletContext().getRealPath("/");
//            instream = new FileInputStream(new File("C:\\keystore.jks")); // :((
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            try {
//                trustStore.load(instream, "skipskip".toCharArray()); // "skipskip".toCharArray() prop.getProperty("keystorePassword").toCharArray()
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (CertificateException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            try {
//                instream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Trust own CA and all self-signed certs
//        SSLContext sslcontext = null;
//        try {
//            sslcontext = SSLContexts.custom()
//                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
//                    .build();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        // Allow TLSv1 protocol only
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                sslcontext,
//                new String[]{"TLSv1"},
//                null,
//                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//                CloseableHttpClient httpclient = HttpClients.custom()
//                .setSSLSocketFactory(sslsf)
//                .build();
//        try {
//            HttpGet httpget = new HttpGet( "https://localhost:8443" + "/drivers"); //prop.getProperty("WebServiceURL")
//            CloseableHttpResponse response = null;
//            try {
//                response = httpclient.execute(httpget);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                HttpEntity entity = response.getEntity();
//                try {
//                    BufferedReader rd = new BufferedReader(
//                            new InputStreamReader(entity.getContent()));
//                    StringBuffer result = new StringBuffer();
//                    String line = "";
//                    while ((line = rd.readLine()) != null) {
//                        result.append(line);
//                    }
//
//                    ObjectMapper mapper = new ObjectMapper();
//                    res = mapper.readValue(String.valueOf(result), new TypeReference<ArrayList<Driver>>() {
//                    });
//                    System.out.println(res.toString());
//                    //EntityUtils.consume(entity);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } finally {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } finally {
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return res;



        //v3
//        SSLContextBuilder builder = new SSLContextBuilder();
//        try {
//            builder.loadTrustMaterial(null, new TrustStrategy(){
//                public boolean isTrusted(X509Certificate[] chain, String authType) {
//                    return true;
//                }
//            });
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        SSLConnectionSocketFactory sslsf = null;
//        try {
//            sslsf = new SSLConnectionSocketFactory(
//                        builder.build());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
//                    sslsf).build();
//
//        HttpGet httpGet = new HttpGet("https://localhost:8443" + "/drivers");
//        CloseableHttpResponse response = null;
//        try {
//            response = httpclient.execute(httpGet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//                System.out.println(response.getStatusLine());
//                HttpEntity entity = response.getEntity();
//            try {
//                EntityUtils.consume(entity);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//            finally {
//            try {
//                response.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    public static ArrayList<Driver> getDriversListX() {
//        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
//        RestTemplate restTemplate = new RestTemplate();
//        ObjectMapper mapper = new ObjectMapper();
//        String unitsString;
//        unitsString = restTemplate.getForObject(prop.getProperty("WebServiceURL") + "/drivers", String.class);
//        try {
//            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Driver>>() {
//            });
//        } catch (IOException e) {
//            System.out.print("Parsing array error");
//            e.printStackTrace();
//        }

        return null;
    }

    public static String addDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny (unitString)
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        ObjectMapper mapper = new ObjectMapper();
//        String drJSON = null;
//        try {
//            drJSON = mapper.writeValueAsString(dr);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        params.add(new BasicNameValuePair("json", drJSON));
//
//        String url = prop.getProperty("WebServiceURL") + "/drivers";
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        try {
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            httppost.setEntity(new UrlEncodedFormEntity(params));
//            return httpclient.execute(httppost, responseHandler);
//        } catch (ClientProtocolException e) {
//            return null;
//        } catch (IOException e) {
//            return null;
//        }
        return null;
    }

//    public static void changeDriver(Long ID) { //na razie nie ma
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.put("http://localhost:8443/drivers/{id}", Driver.class, ID);
//        restTemplate.put("http://localhost:8443/drivers/{id}", ID);
//    }

    public static String deleteDriver(Long ID) {
//        String url = prop.getProperty("WebServiceURL") + "/drivers" + ID;
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpDelete delQuery = new HttpDelete(url);
//        try {
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            return httpclient.execute(delQuery, responseHandler);
//        } catch (ClientProtocolException e) {
//            return null;
//        } catch (IOException e) {
//            return null;
//        }
        return null;
    }

    public static Driver getDriver(Long id) {
        //RestTemplate restTemplate = new RestTemplate();
        //Driver stream = restTemplate.getForObject(prop.getProperty("WebServiceURL") + "/drivers/{id}", Driver.class, id);
        //return stream;
        return null;
    }

}