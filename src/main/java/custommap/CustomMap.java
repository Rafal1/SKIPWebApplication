package custommap;

import SKIPWebApplication.view.MainView;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import returnobjects.Driver;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz
 * Date: 09.03.14
 * Time: 20:52
 */
public class CustomMap {
    private GoogleMap googleMap;
    private VerticalLayout verti;
    private SKIPMarkerClickListener listener;

    private String imagePath = "VAADIN/resources/icons/drivers.png";
    private GoogleMapInfoWindow mapInfo = new GoogleMapInfoWindow("");


    public CustomMap() {
        createLayout();
    }

    public void addMultipleMarkers(ArrayList<Marker> markers) {
        googleMap.clearMarkers();

        for (Marker m : markers) {
            addMarker(m.getName(), m.getCoords());
        }
    }

    public long addMarkerFromDriver(Driver driver) {
        if (driver.getLatestCoordinates() == null || (driver.getLatestCoordinates().getLatitude() == 0 && driver.getLatestCoordinates().getLongitude() == 0)) {
            return -1;
        }
        return addMarker(MainView.makeMarkerName(driver), new LatLon(
                driver.getLatestCoordinates().getLatitude(),
                driver.getLatestCoordinates().getLongitude()));


    }

    public void clearMarkers() {
        googleMap.clearMarkers();
    }

    public void addOneMarker(Marker marker) {

        addOneMarker(marker.getName(), marker.getCoords());
    }

    public boolean showMarkerOnMap(long googleMarkerId) {
        for (GoogleMapMarker marker : googleMap.getMarkers()) {
            if (marker.getId() == googleMarkerId) {
                googleMap.setCenter(marker.getPosition());
                showMapInfo(marker);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public void addOneMarker(String name, LatLon coords) {
        //googleMap.clearMarkers();
        addMarker(name, coords);
    }

    public void setDefaultMarkerImage(String path) {
        imagePath = path;
    }

    public VerticalLayout getCustomMap() {
        return verti;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setSKIPMarkerClickListener(SKIPMarkerClickListener listener) {
        this.listener = listener;
    }

    private void createLayout() {
        verti = new VerticalLayout();
        verti.setSizeFull();

        createMap();
        verti.addComponent(googleMap);
        verti.setExpandRatio(googleMap, 1.0f);
        //   setCompositionRoot( verti);

    }

    private Component createMap() {
        googleMap = new GoogleMap(new LatLon(52.0922474, 21.0249287), 8.0, "");
        googleMap.setSizeFull();
        googleMap.setImmediate(true);
        //googleMap.setMinZoom(20.0);
        return googleMap;
    }

    private long addMarker(String name, LatLon coords) {
        googleMap.setCenter(coords);
        long markerId = googleMap.addMarker(name, coords, false,
                imagePath).getId();


        googleMap.addMarkerClickListener(new MarkerClickListener() {
            @Override
            public void markerClicked(GoogleMapMarker googleMapMarker) {
                showMapInfo(googleMapMarker);
                if (listener != null) {
                    listener.onMarkerClick(googleMapMarker);
                }
            }
        });
        return markerId;
    }

    private void showMapInfo(GoogleMapMarker googleMapMarker) {
        mapInfo.setAnchorMarker(googleMapMarker);
        mapInfo.setContent(googleMapMarker.getCaption());
        googleMap.closeInfoWindow(mapInfo);
        googleMap.openInfoWindow(mapInfo);

    }


}
