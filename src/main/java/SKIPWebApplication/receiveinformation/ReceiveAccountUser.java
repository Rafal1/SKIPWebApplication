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
import returnobjects.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiveAccountUser implements ServerInfo {

    public static Account addAccount(Account acc) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String accJSON = null;
        Account result = null;
        try {
            accJSON = mapper.writeValueAsString(acc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("json", accJSON));

        String url = SSL_ACCESS + "/users";
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveAccountUser: error in adding account ");
            return null;
        }
        HttpPost httppost = new HttpPost(url);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httppost.setEntity(new UrlEncodedFormEntity(params));
            parsingResponse = httpclient.execute(httppost, responseHandler);
            result = mapper.readValue(parsingResponse, new TypeReference<Account>() {
            });
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }  catch (Exception e) {
            return null;
        }

        return result;
    }

    public static List<Account> prepareForAccountsList() {
        ArrayList<Account> parsingResponse = new ArrayList<Account>();

        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveAccount: error in getting accountList ");
            return parsingResponse;
        }
        HttpGet getQuery = new HttpGet(SSL_ACCESS + "/users");
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Account>>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String deleteAccount(String username) {
        String url = SSL_ACCESS + "/users/" + username;
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveAccountUser: error in deleting account ");
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
}
