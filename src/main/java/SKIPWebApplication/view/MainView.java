package SKIPWebApplication.view;

import SKIPWebApplication.receiveinformation.ReceiveDriver;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import custommap.CustomMap;
import custommap.Marker;

import org.vaadin.jouni.animator.AnimatorProxy;
import returnobjects.Driver;

import java.util.ArrayList;
import java.util.Iterator;

import static com.vaadin.ui.Alignment.TOP_CENTER;

/**
 * User: Mariusz
 * Date: 09.03.14
 * Time: 19:34
 * Desc: Klasa bedąca odwzorowaniem widoku głownego w aplikacji
 */
public class MainView extends VerticalLayout implements View {


    private CustomMap customMap;
    private Table table = new Table();
    ArrayList<Driver> driverList;
    private boolean isTableVisible = true;
    AnimatorProxy proxy = new AnimatorProxy();
    public MainView() {
        setSizeFull();
        initLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        refreshData();
    }

    public void refreshData() {
        driverList = ReceiveDriver.getDriversList();
        ArrayList<Marker> markerList = makeMarkerListfromDrivers(driverList);
        addAllDrivers(driverList);
        customMap.addMultipleMarkers(markerList);
    }

    private void initLayout() {
        Component navigationBar = DefaultViewBuilderHelper.getDefaultMenuPanel();
        Component bodyContent = getBodyContent();

        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.setSizeFull();
        String PAGE_WIDTH = "1024px";
        mainPanel.setWidth(PAGE_WIDTH);
        mainPanel.addComponent(navigationBar);
        mainPanel.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
        mainPanel.addComponent(bodyContent);
        mainPanel.setExpandRatio(bodyContent, 1.0f);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(mainPanel);
        layout.setComponentAlignment(mainPanel, TOP_CENTER);
        layout.setSizeFull();
        addComponent(layout);


    }

    private ArrayList<Marker> makeMarkerListfromDrivers(ArrayList<Driver> allDrivers) {
        Iterator<Driver> driversIterator = allDrivers.iterator();
        ArrayList<Marker> markersList = new ArrayList<Marker>();
        while (driversIterator.hasNext()) {
            Driver tmpDriver = driversIterator.next();
            markersList.add(new Marker(makeMarkerName(tmpDriver),
                    new LatLon(tmpDriver.getLatestCoordinates().getLatitude(),
                            tmpDriver.getLatestCoordinates().getLongitude()), tmpDriver.getId()));
        }
        return markersList;
    }

    private Component getBodyContent() {
        customMap = new CustomMap();
        final VerticalLayout tableLayout = (VerticalLayout) getTableLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        final Button moreLessButton = new Button("Mniej");
        moreLessButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Notification.show("buttonClick"); //TEST

                if (isTableVisible) {
                   // proxy.animate(table, AnimType.FADE_OUT).setDuration(200);
                    isTableVisible = false;
                    tableLayout.setVisible(false);
                    moreLessButton.setCaption("Więcej");
                } else {
                    //proxy.animate(table, AnimType.FADE_IN).setDuration(300);
                    //TODO animacja pojawiania i zanikania

                    isTableVisible = true;
                    tableLayout.setVisible(true);
                    moreLessButton.setCaption("Mniej");
                }

            }
        });
        horizontalLayout.addComponent(tableLayout);
        horizontalLayout.setExpandRatio(tableLayout , 0.3f);
        horizontalLayout.addComponent(moreLessButton);
        horizontalLayout.setComponentAlignment(moreLessButton, Alignment.MIDDLE_CENTER);

        horizontalLayout.addComponent(customMap.getGoogleMap());
        horizontalLayout.setSizeFull();
        horizontalLayout.setExpandRatio(customMap.getGoogleMap(), 1.0f);
        return horizontalLayout;
    }

    private Component getTableLayout() {
        VerticalLayout c = new VerticalLayout();
        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.setSelectable(true);
        c.addComponent(table);
        c.setExpandRatio(table, 1);
        table.setSizeFull();
        c.setSizeFull();
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
              long driverId = (Long)itemClickEvent.getItemId();
             Iterator<Driver> iterator = driverList.iterator();
               while(iterator.hasNext()){
                   Driver driver = iterator.next();
                   if(driver.getId() == driverId){
                       customMap.showMarkerOnMap(makeMarkerName(driver));
                       break;
                   }
               }
            }
        });

       return c;

    }

    private void addAllDrivers(ArrayList<Driver> drivers) {
        for (Driver driver : drivers) {
            addDriverToTable(driver);
        }
    }

    private void addDriverToTable(Driver driver) {
        table.addItem(new Object[]{
                driver.getFirstName(), driver.getLastName()}, driver.getId());
    }

    private String makeMarkerName(Driver driver){
           return driver.getFirstName() + " " + driver.getLastName();

    }
}
