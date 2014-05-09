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
import returnobjects.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafal Zawadzki
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

    public static String addVehicle(Vehicle veh) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String parsingResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        String vehJSON = null;
        try {
            vehJSON = mapper.writeValueAsString(veh);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("vehicle", vehJSON));

        String url = SSL_ACCESS + "/vehicles";
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

    public static Vehicle changeVehicle(Vehicle veh) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Vehicle parsingResponse = null;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPut putQuery = new HttpPut(SSL_ACCESS + "/vehicles/" + veh.getId());
        putQuery.setHeader( "Content-Type", "application/json" );

        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(veh);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("vehicle", drJSON));

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
            parsingResponse = mapper.readValue(unitsString, new TypeReference<Driver>() {
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;
    }

    //todo test updateCoordinates
    //czy to wgl jest potrzebne??

    public static Coordinates updateVehicleCoordinates(Coordinates cor, Long ID) {
        /*InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/vehicles/" + ID + "/updateCoordinates";

        Coordinates parsingResponse = new Coordinates();

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // optional default is GET
        try {
            con.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        // add request header
        con.setRequestProperty("WebClient", "PUTVehicle");

        ObjectMapper mapper = new ObjectMapper();
        String corJSON = null;
        try {
            corJSON = mapper.writeValueAsString(cor);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String urlParameters = "coordinates=" + corJSON;

        con.setDoOutput(true);
        DataOutputStream wr = null;
        BufferedReader in = null;
        String inputLine;
        StringBuffer response = new StringBuffer();

        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parsingResponse = mapper.readValue(response.toString(), new TypeReference<Coordinates>() {
            });
        } catch (IOException e) {
            System.out.print("Parsing array error");
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsingResponse;*/
        return null;
    }
}