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
import returnobjects.Coordinates;
import returnobjects.Driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 * @author Kamil Malka
 */
public class ReceiveDriver implements ServerInfo {

    public static ArrayList<Driver> getDriversList() {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();

        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveDriver: error in getting driverList ");
            return parsingResponse;
        }
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/drivers");
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
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
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("driver", drJSON));

        String url = SSL_ACCESS + "/drivers";
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveDriver: error in adding driver ");
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

    public static Driver changeDriver(Driver dr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Driver parsingResponse = null;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveDriver: error in changing driver ");
            return parsingResponse;
        }
        HttpPut putQuery = new HttpPut(SSL_ACCESS + "/drivers/" + dr.getId());
        putQuery.setHeader("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("driver", drJSON));

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            putQuery.setEntity(new UrlEncodedFormEntity(params));
            unitsString = httpclient.execute(putQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Driver>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String deleteDriver(Long ID) {
        String url = SSL_ACCESS + "/drivers/" + ID;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveDriver: error in deleting driver ");
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

    public static Driver getDriver(Long id) {
        Driver parsingResponse = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/drivers/" + id);
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

    public static Coordinates updateDriverCoordinates(Long id, Coordinates cor) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Coordinates parsingResponse = null;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveDriver: error in updating driver coordinates ");
            return parsingResponse;
        }
        HttpPut putQuery = new HttpPut(SSL_ACCESS + "/drivers/" + id + "/updateCoordinates");
        putQuery.setHeader("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        String corJSON = null;
        try {
            corJSON = mapper.writeValueAsString(cor);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("coordinates", corJSON));
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            putQuery.setEntity(new UrlEncodedFormEntity(params));
            unitsString = httpclient.execute(putQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Coordinates>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }
}