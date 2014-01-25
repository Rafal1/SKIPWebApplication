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

    public static Driver getDriverByID(Integer unitID) {
        RestTemplate restTemplate = new RestTemplate();
        Driver stream = restTemplate.getForObject("http://localhost:8080/overunit?driverID={unitID}", Driver.class, unitID);
        return stream;
    }

    public static ArrayList<Driver> getUnitsByPhrase(String phrase) {
        ArrayList<Driver> parsingResponse = new ArrayList<Driver>();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String unitsString;
            unitsString = restTemplate.getForObject("http://localhost:8080/search?phrase={phrase}&wholeWord={wholeWord}", String.class, phrase );
        try {
            parsingResponse = mapper.readValue(unitsString, new TypeReference<ArrayList<Driver>>() {
            });
        } catch (IOException e) {
            System.out.print("Parsing array error");
            e.printStackTrace();
        }
        return parsingResponse;
    }
}
