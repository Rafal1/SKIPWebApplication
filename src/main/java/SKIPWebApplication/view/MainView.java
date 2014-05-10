package SKIPWebApplication.view;

import SKIPWebApplication.receiveinformation.ReceiveDriver;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import custommap.CustomMap;
import custommap.Marker;
import org.vaadin.jouni.animator.Animator;
import org.vaadin.jouni.dom.client.Css;
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


    private  CustomMap customMap      ;
    Table table = new Table();

    public MainView() {
        setSizeFull();
        initLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        refreshData();
    }

    public void refreshData(){
        ArrayList<Driver> driverList  = ReceiveDriver.getDriversList();
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

    private ArrayList<Marker> makeMarkerListfromDrivers (ArrayList<Driver> allDrivers){
           Iterator<Driver> driversIterator =  allDrivers.iterator();
            ArrayList<Marker> markersList = new ArrayList<Marker>();
           while(driversIterator.hasNext()){
               Driver tmpDriver = driversIterator.next();
               markersList.add(new Marker(tmpDriver.getFirstName() + " " + tmpDriver.getLastName(),
                       new LatLon(tmpDriver.getLatestCoordinates().getLatitude(),
                               tmpDriver.getLatestCoordinates().getLongitude() ), tmpDriver.getId()));
           }
        return markersList;
    }

    private Component getBodyContent() {
        customMap = new CustomMap();
        initTable();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(table);
        Button moreLessButton = new Button("Mniej");
        moreLessButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Notification.show("buttonClick"); //TEST
                Animator.animate(table, new Css().scaleX(0)).duration(1000);   // na razie wyłączone trzeba
                //skompilowac widgetset
                //TODO
            }
        });
        horizontalLayout.addComponent(moreLessButton);
        horizontalLayout.addComponent(customMap.getGoogleMap());
        horizontalLayout.setSizeFull();
        horizontalLayout.setExpandRatio(customMap.getGoogleMap(), 1.0f);
        return horizontalLayout;
    }

    private void initTable(){
        table.addContainerProperty("First Name", String.class,  null);
        table.addContainerProperty("Last Name",  String.class,  null);
        //table.addContainerProperty("Year",       Integer.class, null);
    }
    private void addAllDrivers(ArrayList<Driver> drivers){
        for (Driver driver : drivers) {
            addDriverToTable(driver);
        }
    }
    private void addDriverToTable(Driver driver){
        table.addItem(new Object[] {
                driver.getFirstName(),driver.getLastName()}, driver.getId());
    }
}
