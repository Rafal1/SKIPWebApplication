package SKIPWebApplication.receiveinformation;

import SKIPWebApplication.consts.StringConsts;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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


    public static boolean login(String username, String password) throws LoginErrorException {

        String parsingResponse;

        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        HttpPost httppost = new HttpPost(SSL_ACCESS + LOGIN_SUFFIX_URL);
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        CloseableHttpResponse response = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            parsingResponse = EntityUtils.toString(entity);


            if (statusCode == 200) {
                String[] responseArray = parsingResponse.split(" ");

                if(responseArray[1].equals(StringConsts.ROLE_USER))
                    throw new LoginErrorException("Błędne dane logowania");
                if (responseArray[0].equals(username)) {
                    VaadinSession.getCurrent().setAttribute(HttpClientHelper.COOKIE_STORE_SEESION_TAG, cookieStore);
                    VaadinSession.getCurrent().setAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG, responseArray[1]);
                    VaadinSession.getCurrent().setAttribute(HttpClientHelper.USER_LOGIN, username);
                    VaadinSession.getCurrent().setAttribute("login", true);
                    return true;
                }
                else{
                    throw new LoginErrorException("Serwer zwrócił niepoprawne dane");

                }

            } else
                    throw new LoginErrorException("Błędne dane logowania");



            }catch(ClientProtocolException e){
                e.printStackTrace();
                throw new LoginErrorException("Połączenie z serwerem jest w tej chwili nie możliwe, prosimy spróbować za chwile.");

            }catch(IOException e){
                e.printStackTrace();
                throw new LoginErrorException("Błąd połączenia z serwerem");
            }finally{
                try {
                    if (response != null) {
                        response.close();
                    }
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public static boolean logout() {

        HttpClient httpClient = HttpClientHelper.getHttpClient();
        HttpGet getQuery = new HttpGet(SSL_ACCESS + LOGOUT_SUFFIX_URL);

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            httpClient.execute(getQuery, responseHandler);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        VaadinSession.getCurrent().setAttribute(HttpClientHelper.COOKIE_STORE_SEESION_TAG, null);
        VaadinSession.getCurrent().setAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG, null);
        VaadinSession.getCurrent().setAttribute(HttpClientHelper.USER_LOGIN, null);
        VaadinSession.getCurrent().setAttribute("login", false);
        VaadinSession.getCurrent().close();

        return true;
    }

}
