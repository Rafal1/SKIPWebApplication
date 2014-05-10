package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import returnobjects.Driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz
 * Date: 10.05.14
 * Time: 17:33
 * Klasa do autentykacji z backendem
 */
public class LoginService implements ServerInfo {


    public static boolean login(String username, String password){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String usernameJSON = null;
        String passwordJSON = null;
        try {
            usernameJSON = mapper.writeValueAsString(username);
            passwordJSON = mapper.writeValueAsString(password);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("username", usernameJSON));
        params.add(new BasicNameValuePair("password", passwordJSON));


        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(LOGIN_SUFFIX_URL);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httppost.setEntity(new UrlEncodedFormEntity(params));
            parsingResponse = httpclient.execute(httppost, responseHandler);
        } catch (ClientProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        //TODO sprawdzenie czy udało sie zalogować. należy sprawdzić co zostało zwrócone przez serwer.

        System.out.println(parsingResponse);
        return true;
    }

    public static boolean logout(){
        Driver parsingResponse = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + LOGOUT_SUFFIX_URL);
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            unitsString = httpclient.execute(getQuery, responseHandler);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO sprawdzić czy udało się wylogować
        return true;
    }

}
