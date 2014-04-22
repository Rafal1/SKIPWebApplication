package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import returnobjects.Driver;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import static org.atmosphere.di.ServletContextHolder.getServletContext;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriver {
    private static Properties prop = new Properties();

    public static ArrayList<Driver> getDriversList() {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/drivers";

        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // optional default is GET
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

//        add request header
        con.setRequestProperty("WebClient", "GETDriversList");

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            parsingResponse = mapper.readValue(response.toString(), new TypeReference<ArrayList<Driver>>() {
            });
        } catch (IOException e) {
            System.out.print("Parsing array error");
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String addDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny (unitString)
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/drivers";

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        String urlParameters = "driver=" + drJSON;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = null;
        StringBuffer response = new StringBuffer();
        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
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