package SKIPWebApplication.receiveinformation;

import org.junit.Test;
import returnobjects.Vehicle;

import java.util.ArrayList;

/**
 * @author Rafal Zawadzki
 */
public class ReceiveVehicleTest {
    @Test
    public void testGetVehiclesList() throws Exception {
        ArrayList<Vehicle> arr = new ArrayList<Vehicle>();
        arr = ReceiveVehicle.getVehiclesList();
        System.out.println(arr.get(0).getBrand());
    }

    @Test
    public void testAddVehicle() throws Exception {
                Vehicle car = new Vehicle();
                car.setBrand("Mercedes");
                car.setColour("Czerwony");
                car.setMaxLoad(10);
                car.setRegNumber("WN45687");
                String jsonVeh = ReceiveVehicle.addVehicle(car);
                System.out.println(jsonVeh);
    }

    @Test
    public void testChangeVehicle() throws Exception {

    }

    @Test
    public void testDeleteVehicle() throws Exception {

    }

    @Test
    public void testGetVehicle() throws Exception {

    }

    @Test
    public void testUpdateVehicleCoordinates() throws Exception {

    }
}
