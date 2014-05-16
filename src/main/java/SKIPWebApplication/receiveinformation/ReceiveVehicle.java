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
import org.apache.http.message.BasicNameValuePair;
import returnobjects.Driver;
import returnobjects.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveVehicle implements ServerInfo {

    public static ArrayList<Vehicle> getVehiclesList() {
        ArrayList<Vehicle> parsingResponse = new ArrayList<Vehicle>();
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveVehicle: error in getting vehicle List ");
            return parsingResponse;
        }
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/vehicles");
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Vehicle>>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String addVehicle(Vehicle veh) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String vehJSON = null;
        try {
            vehJSON = mapper.writeValueAsString(veh);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("vehicle", vehJSON));

        String url = SSL_ACCESS + "/vehicles";
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveVehicle: error in adding vehicle ");
            return parsingResponse;
        }
        HttpPost httppost = new HttpPost(url);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httppost.setEntity(new UrlEncodedFormEntity(params));
            parsingResponse = httpclient.execute(httppost, responseHandler);
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return parsingResponse;
    }

    public static Vehicle changeVehicle(Vehicle veh) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Vehicle parsingResponse = null;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveVehicle: error in changing vehicle ");
            return parsingResponse;
        }
        HttpPost PostQuery = new HttpPost(SSL_ACCESS + "/vehicles/" + veh.getId());
        PostQuery.setHeader("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(veh);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("vehicle", drJSON));

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            PostQuery.setEntity(new UrlEncodedFormEntity(params));
            unitsString = httpclient.execute(PostQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Driver>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String deleteVehicle(Long ID) {
        String url = SSL_ACCESS + "/vehicles/" + ID;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveVehicle: error in deleting vehicle ");
            return null;
        }
        HttpDelete delQuery = new HttpDelete(url);
        String parsingResponse = null;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            parsingResponse = httpclient.execute(delQuery, responseHandler);
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return parsingResponse;
    }

    public static Vehicle getVehicle(Long id) {
        Vehicle parsingResponse = null;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveVehicle: error in getting vehicle " + id);
            return parsingResponse;
        }
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/vehicles/" + id);
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Driver>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

}