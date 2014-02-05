package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.client.RestTemplate;
import returnobjects.Driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriver {

    public static ArrayList<Driver> getDriversList() {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
        RestTemplate restTemplate = new RestTemplate();
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

    public static HttpResponse addDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny (unitString)
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
            httppost.setEntity(new UrlEncodedFormEntity(params));
            return httpclient.execute(httppost);
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

    public static void deleteDriver(Long ID) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/drivers/{id}", ID);
    }

    public static Driver getDriver(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Driver stream = restTemplate.getForObject("http://localhost:8080/drivers/{id}", Driver.class, id);
        return stream;
    }

}
