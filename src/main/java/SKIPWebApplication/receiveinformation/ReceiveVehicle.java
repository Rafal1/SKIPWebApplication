package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import returnobjects.Coordinates;
import returnobjects.Vehicle;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import static org.atmosphere.di.ServletContextHolder.getServletContext;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveVehicle {
    private static Properties prop = new Properties();

    public static ArrayList<Vehicle> getVehiclesList() {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/vehicles";

        ArrayList<Vehicle> parsingResponse = new ArrayList<Vehicle>();

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
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

//        add request header
        con.setRequestProperty("WebClient", "GETVehiclesList");

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            parsingResponse = mapper.readValue(response.toString(), new TypeReference<ArrayList<Vehicle>>() {
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
        return parsingResponse;
    }

    public static String addVehicle(Vehicle dr) {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/vehicles";

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

        ObjectMapper mapper = new ObjectMapper();
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        String urlParameters = "vehicle=" + drJSON;

        // add request header
        con.setRequestProperty("WebClient", "POSTaddVehicle");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = null;
        StringBuffer response = new StringBuffer();
        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    //todo test changeVehicle
    public static Vehicle changeVehicle(Vehicle dr, Long ID) {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/vehicles/" + ID;

        Vehicle parsingResponse = new Vehicle();

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
        String drJSON = null;
        try {
            drJSON = mapper.writeValueAsString(dr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String urlParameters = "vehicle=" + drJSON;

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
            parsingResponse = mapper.readValue(response.toString(), new TypeReference<Vehicle>() {
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
        return parsingResponse;
    }

    public static String deleteVehicle(Long ID) {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/vehicles/" + ID;

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

        try {
            con.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        // add request header
        con.setRequestProperty("WebClient", "DELETEVehicle");

        // Send post request
        con.setDoOutput(true);
        StringBuffer response = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    //todo test getVehicle
    public static Vehicle getVehicle(Long ID) {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = prop.getProperty("WebServiceURL") + "/vehicles/" + ID;

        Vehicle parsingResponse = new Vehicle();

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
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        //        add request header
        con.setRequestProperty("WebClient", "GETVehicle");

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            parsingResponse = mapper.readValue(response.toString(), new TypeReference<Vehicle>() {
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
        return parsingResponse;
    }

    //todo test updateCoordinates
    public static Coordinates updateVehicleCoordinates(Coordinates cor, Long ID) {
        InputStream inputStream = getServletContext().getResourceAsStream("VAADIN\\resources\\config.properties");
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
        return parsingResponse;
    }
}