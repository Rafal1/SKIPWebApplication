package custommap;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz
 * Date: 16.03.14
 * Time: 00:30
 * To change this template use File | Settings | File Templates.
 */
public class Marker {
    private LatLon coords;
    private long driverId ;
    private String name;

    public Marker (String name, LatLon coords, long driverId){
        this.name = name;
        this.coords =  coords;
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLon getCoords() {
        return coords;
    }

    public void setCoords(LatLon coords) {
        this.coords = coords;
    }
}
