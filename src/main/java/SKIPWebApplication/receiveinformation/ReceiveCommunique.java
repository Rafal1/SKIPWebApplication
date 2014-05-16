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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import returnobjects.Communique;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
 *
 */
public class ReceiveCommunique implements ServerInfo {

    public static ArrayList<Communique> getCommuniquesList() {
        ArrayList<Communique> parsingResponse = new ArrayList<Communique>();

        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveCommunique: error in getting driverList ");
            return parsingResponse;
        }
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/statements");
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Communique>>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String addCommunique(Communique dr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("statement", drJSON));

        String url = SSL_ACCESS + "/statements";
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveCommunique: error in adding driver ");
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

    public static Communique changeCommunique(Communique dr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Communique parsingResponse = null;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveCommunique: error in changing driver ");
            return parsingResponse;
        }
        HttpPost postQuery = new HttpPost(SSL_ACCESS + "/statements/" + dr.getId());
        postQuery.setHeader("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("statements", drJSON));

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            postQuery.setEntity(new UrlEncodedFormEntity(params));
            unitsString = httpclient.execute(postQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Communique>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String deleteCommunique(Long ID) {
        String url = SSL_ACCESS + "/statements/" + ID;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveCommunique: error in deleting statement ");
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

    public static Communique getCommunique(Long id) {
        Communique parsingResponse = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/statements/" + id);
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Communique>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

}