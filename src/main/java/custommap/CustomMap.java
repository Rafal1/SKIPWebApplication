package custommap;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz
 * Date: 09.03.14
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class CustomMap {
    private GoogleMap googleMap;
    private VerticalLayout verti;
    private String imagePath = "VAADIN/resources/icons/drivers.png";


    public CustomMap() {
        createLayout();
    }

    public void addFilters() {

    }

    public void addMultipleMarkers(ArrayList<Marker> markers){
        googleMap.clearMarkers();
        Iterator<Marker> iter = markers.iterator();
        while(iter.hasNext()){
            Marker m = iter.next();
            addMarker(m.getName(), m.getCoords());
        }
    }

    public void addOneMarker(Marker marker){
        addOneMarker(marker.getName(), marker.getCoords());
    }

    public void addOneMarker( String name,  LatLon coords ) {
        googleMap.clearMarkers();
        addMarker(name, coords);
    }

    public void setDefaultMarkerImage(String path){
        imagePath = path;
    }

    public Component getCustomMap(){
        return verti;
    }

    public GoogleMap getGoogleMap(){
        return googleMap;
    }

    private void createLayout() {
        verti = new VerticalLayout();
        verti.setSizeFull();

        createMap();
        verti.addComponent(googleMap);
        verti.setExpandRatio(googleMap , 1.0f);
     //   setCompositionRoot( verti);

    }

    private Component createMap(){
        googleMap = new GoogleMap(new LatLon(52.0922474, 21.0249287), 12.0, "");
        googleMap.setSizeFull();
        googleMap.setImmediate(true);
        googleMap.setMinZoom(4.0);
        return googleMap;
    }

    private void addMarker(String name , LatLon coords){
        googleMap.setCenter(coords);
        googleMap.addMarker(name, coords, false,
                imagePath);
    }


}
