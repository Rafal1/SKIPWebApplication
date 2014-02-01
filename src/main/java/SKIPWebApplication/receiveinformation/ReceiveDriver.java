package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import returnobjects.Driver;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveDriver {

    public static ArrayList<Driver> getDriversList() {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
        unitsString = restTemplate.getForObject("http://localhost:8080/drivers", String.class);
        try {
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Driver>>() {
            });
        } catch (IOException e) {
            System.out.print("Parsing array error");
            e.printStackTrace();
        }
        return parsingResponse;
    }

    public static String addDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny (unitString)
        RestTemplate restTemplate = new RestTemplate();
        String unitsString;
//            unitsString = restTemplate.postForObject("http://localhost:8080/drivers?json={driver}", dr, String.class, dr );
        unitsString = restTemplate.postForObject("http://localhost:8080/drivers", dr, String.class);
        return unitsString;
    }

    public static void changeDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:8080/drivers/{id}", dr, dr.getId());
    }

    public static void deleteDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/drivers/{id}", dr, dr.getId());
    }

    public static Driver getDriver(Integer id) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny
        RestTemplate restTemplate = new RestTemplate();
        Driver stream = restTemplate.getForObject("http://localhost:8080/drivers/{id}", Driver.class, id);
        return stream;
    }

}
