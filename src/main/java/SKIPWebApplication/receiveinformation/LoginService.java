package SKIPWebApplication.receiveinformation;

import com.vaadin.server.VaadinSession;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

    public static final String COOKIE_STORE_SEESION_TAG = "CookieSession";


    public static boolean login(String username, String password) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;

        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        HttpPost httppost = new HttpPost(SSL_ACCESS + LOGIN_SUFFIX_URL + "?username=" + username + "&password=" + password);
        CloseableHttpResponse response2 = null;
        try {
            HttpResponse response = httpClient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            parsingResponse = EntityUtils.toString(entity);

            {
                HttpGet httpGet = new HttpGet(SSL_ACCESS + "/login");
                response2 = httpClient.execute(httpGet);
                HttpEntity entity2 = response2.getEntity();
                parsingResponse = EntityUtils.toString(entity2);
                if (parsingResponse.equals(username)) {
                    VaadinSession.getCurrent().setAttribute(COOKIE_STORE_SEESION_TAG, cookieStore);
                    return true;
                } else
                    return false;


            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (response2 != null) {
                    response2.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public static boolean logout() {
        Driver parsingResponse = null;
        Object cookieObject = VaadinSession.getCurrent().getAttribute(COOKIE_STORE_SEESION_TAG);
        if (cookieObject == null) {
            return false;
        }
        BasicCookieStore cookieStore = (BasicCookieStore) cookieObject;
        HttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + LOGOUT_SUFFIX_URL);
        String unitsString;
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            unitsString = httpclient.execute(getQuery, responseHandler);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        VaadinSession.getCurrent().close();
        return true;
    }

}
