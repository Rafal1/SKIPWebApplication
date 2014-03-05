package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import returnobjects.Driver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriver {

    public static ArrayList<Driver> getDriversList() {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
        SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);

                //Basic Authentication for Police API
                String authorisation = "user" + ":" + "6cdb4056-d927-4ffe-8c1a-c8af8fb01bab";
                byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + new String(encodedAuthorisation));
            }
        };
        RestTemplate restTemplate = new RestTemplate(s);
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        unitsString = restTemplate.getForObject("http://localhost:8080/drivers", String.class);
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

        String url = "http://localhost:8080/drivers";
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
        String url = "http://localhost:8080/drivers/" + ID;
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
        Driver stream = restTemplate.getForObject("http://localhost:8080/drivers/{id}", Driver.class, id);
        return stream;
    }

}
