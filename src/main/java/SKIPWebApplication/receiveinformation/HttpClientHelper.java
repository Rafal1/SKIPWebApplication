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
public class HttpClientHelper {
    public static final String HTTP_CLIENT_SESSION_TAG = "httpClient";
    public static final String COOKIE_STORE_SEESION_TAG = "CookieSession";
    public static final String USER_ROLE_SESSION_TAG = "userRole";
    public static final String USER_LOGIN = "login";


    public static HttpClient getHttpClient() {
        HttpClient httpClient;
        Object httpClientObject = VaadinSession.getCurrent().getAttribute(HTTP_CLIENT_SESSION_TAG);
        if (httpClientObject == null) {
            httpClient = createHttpClient();
            VaadinSession.getCurrent().setAttribute(HTTP_CLIENT_SESSION_TAG, httpClient);
        } else {
            httpClient = (HttpClient) httpClientObject;
        }
        return httpClient;

    }

    private static HttpClient createHttpClient() {
        Object cookieObject = VaadinSession.getCurrent().getAttribute(COOKIE_STORE_SEESION_TAG);
        if (cookieObject == null) {
            return null;
        }

        // Jesli nie bedzie działo, z powodu błedu ssl nalezy odkomentować kod poniżej
        // ten problem moze wystapic w przypadku połączenia z serwera zewmetrzmega
      /*  KeyStore trustStore = null;
        FileInputStream instream =null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/VAADIN/resources/ssl/cacertsts"));
            trustStore.load(instream, "18SK1P".toCharArray());
        } catch (FileNotFoundException e) {
            System.out.println("nie znaleziono pliku z certyfikatem");
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyStoreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if(instream != null){
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyManagementException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyStoreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
         */         //TODO Test działania na serwerze
        BasicCookieStore cookieStore = (BasicCookieStore) cookieObject;
        return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }


}