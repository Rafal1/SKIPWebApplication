package SKIPWebApplication.receiveinformation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    public static ResponseEntity<String> addDriver(Driver dr) { // webservice zwróci ID pod krórym ten kierowca będzie dostepny (unitString)
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<String> unitsString;
        unitsString = restTemplate.postForEntity("http://localhost:8080/drivers", dr, String.class);
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
