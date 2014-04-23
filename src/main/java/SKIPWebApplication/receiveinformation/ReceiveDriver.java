package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import returnobjects.Driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 * @author Kamil Malka
 */
public class ReceiveDriver implements ServerInfo{

    public static ArrayList<Driver> getDriversList() {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/drivers");
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString =  httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Driver>>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsingResponse;
    }

    public static String addDriver(Driver dr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("driver", drJSON));

        String url = SSL_ACCESS + "/drivers";
        HttpClient httpclient = HttpClientBuilder.create().build();
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
//        restTemplate.put("http://localhost:8443/drivers/{id}", Driver.class, ID);
//        restTemplate.put("http://localhost:8443/drivers/{id}", ID);
//    }

    public static String deleteDriver(Long ID) {
        String url = SSL_ACCESS + "/drivers/" + ID;
        HttpClient httpclient = HttpClientBuilder.create().build();
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
        //RestTemplate restTemplate = new RestTemplate();
        //Driver stream = restTemplate.getForObject(prop.getProperty("WebServiceURL") + "/drivers/{id}", Driver.class, id);
        //return stream;
        return null;
    }

}