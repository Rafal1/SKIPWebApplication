package SKIPWebApplication.receiveinformation;

import com.vaadin.server.VaadinSession;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;

/*
 Autor: Mariusz
 Description: Klasa zwraca przechowywane w sesji httpClient w którym znajdują się już wymagane ciasteczka\
 jesli w sesji nie ma jescze zapisanego httpClienta zostaje on stworzony.

 */
public class HttpClientHelper{
    public static final String HTTP_CLIENT_SESSION_TAG = "httpClient";


    public static HttpClient getHttpClient(){
        HttpClient httpClient;
        Object httpClientObject = VaadinSession.getCurrent().getAttribute(HTTP_CLIENT_SESSION_TAG);
        if(httpClientObject == null){
           httpClient = createHttpClient();
           VaadinSession.getCurrent().setAttribute(HTTP_CLIENT_SESSION_TAG, httpClient);
        }
        else{
            httpClient = (HttpClient) httpClientObject;
        }
      return httpClient;

    }

    private static HttpClient createHttpClient() {
        Object cookieObject = VaadinSession.getCurrent().getAttribute(LoginService.COOKIE_STORE_SEESION_TAG);
        if(cookieObject == null){
            return null;
        }
        BasicCookieStore cookieStore = (BasicCookieStore) cookieObject;
        HttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        return httpclient;
    }


}