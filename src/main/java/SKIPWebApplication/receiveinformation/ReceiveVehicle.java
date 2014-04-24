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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import returnobjects.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 * @author Kamil Malka
 */
public class ReceiveVehicle implements ServerInfo {

    public static ArrayList<Vehicle> getVehiclesList() {
        ArrayList<Vehicle> parsingResponse = new ArrayList<Vehicle>();
        HttpClient httpclient = HttpClientBuilder.create().build();
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

    public static String addVehicle(Vehicle dr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("vehicle", drJSON));

        String url = SSL_ACCESS + "/vehicle";
        HttpClient httpclient = HttpClientBuilder.create().build();
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

    public static Vehicle changeVehicle(Long id, Vehicle dr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Vehicle parsingResponse = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPut putQuery = new HttpPut(SSL_ACCESS + "/vehicles/" + id);
        putQuery.setHeader( "Content-Type", "application/json" );

        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("vehicle", drJSON));

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            putQuery.setEntity(new UrlEncodedFormEntity(params));
            unitsString = httpclient.execute(putQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Vehicle>() {
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
        HttpClient httpclient = HttpClientBuilder.create().build();
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
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/vehicles/" + id);
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Vehicle>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }
}