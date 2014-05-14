package SKIPWebApplication.view;

import SKIPWebApplication.consts.StringConsts;
import SKIPWebApplication.receiveinformation.ReceiveDriver;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.ui.*;
import custommap.CustomMap;
import custommap.SKIPMarkerClickListener;
import returnobjects.Driver;

import java.util.ArrayList;

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

    //AnimatorProxy proxy = new AnimatorProxy();
    public MainView() {
        setSizeFull();
        initLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        DefaultViewBuilderHelper.checkLogin();
        refreshData();
    }

    public void refreshData() {
        driverList = ReceiveDriver.getDriversList();
        addDriversToView(driverList);
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

    private Component getBodyContent() {
        customMap = new CustomMap();
        final VerticalLayout tableLayout = (VerticalLayout) getTableLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        final Button moreLessButton = new Button(StringConsts.LESS_LABEL);
        moreLessButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {


                if (isTableVisible) {
                    // proxy.animate(table, AnimType.FADE_OUT).setDuration(200);
                    isTableVisible = false;
                    tableLayout.setVisible(false);
                    moreLessButton.setCaption(StringConsts.MORE_LABEL);
                } else {
                    //proxy.animate(table, AnimType.FADE_IN).setDuration(300);
                    //TODO animacja pojawiania i zanikania

                    isTableVisible = true;
                    tableLayout.setVisible(true);
                    moreLessButton.setCaption(StringConsts.LESS_LABEL);
                }

            }
        });
        horizontalLayout.addComponent(tableLayout);
        horizontalLayout.setExpandRatio(tableLayout, 0.3f);
        horizontalLayout.addComponent(moreLessButton);
        horizontalLayout.setComponentAlignment(moreLessButton, Alignment.MIDDLE_CENTER);

        horizontalLayout.addComponent(customMap.getGoogleMap());
        horizontalLayout.setSizeFull();
        horizontalLayout.setExpandRatio(customMap.getGoogleMap(), 1.0f);
        return horizontalLayout;
    }

    private Component getTableLayout() {
        VerticalLayout c = new VerticalLayout();
        table.addContainerProperty("Imię", String.class, null);
        table.addContainerProperty("Nazwisko", String.class, null);
        table.setSelectable(true);
        c.addComponent(table);
        c.setExpandRatio(table, 1);
        table.setSizeFull();
        c.setSizeFull();
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                long googleMapId = (Long) itemClickEvent.getItemId();
                customMap.showMarkerOnMap(googleMapId);
            }
        }

        );

        return c;

    }

    private void addDriversToView(ArrayList<Driver> drivers) {
        addListenerToCustomMap();
        customMap.clearMarkers();
        table.removeAllItems();
        for (Driver driver : drivers) {
            long markerId = customMap.addMarkerFromDriver(driver);
            addDriverToTable(driver, markerId);
        }
    }

    private void addListenerToCustomMap() {
        SKIPMarkerClickListener markerListener = new SKIPMarkerClickListener() {
            @Override
            public void onMarkerClick(GoogleMapMarker googleMarker) {
                table.select(googleMarker.getId());
            }
        };
        customMap.setSKIPMarkerClickListener(markerListener);
    }

    private void addDriverToTable(Driver driver, long markerId) {
        table.addItem(new Object[]{
                driver.getFirstName(), driver.getLastName()}, markerId);
    }

    public static String makeMarkerName(Driver driver) {
        return driver.getFirstName() + " " + driver.getLastName();

    }
}
