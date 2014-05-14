package custommap;

import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz
 * Date: 14.05.14
 * Time: 15:33
 * Listener z możlioscia dodania do Custom map, uruchamiany po kliknięciu na marker
 */
public interface SKIPMarkerClickListener {

    public void onMarkerClick(GoogleMapMarker googleMarker);
}
