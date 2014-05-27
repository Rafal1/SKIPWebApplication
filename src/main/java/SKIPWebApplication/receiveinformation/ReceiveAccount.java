package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import returnobjects.Account;
import returnobjects.Driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kamil
 * Date: 27.05.14
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveAccount implements ServerInfo {

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

        String url = SSL_ACCESS + "/admins";
        HttpClient httpclient = HttpClientHelper.getHttpClient();
        if (httpclient == null) {
            System.out.println("ReceiveAccount: error in adding account ");
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
}
