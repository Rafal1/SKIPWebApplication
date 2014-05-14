package SKIPWebApplication.view;

import SKIPWebApplication.receiveinformation.ReceiveVehicle;
import SKIPWebApplication.window.EditVehicleWindow;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import custommap.CustomMap;
import custommap.Marker;
import returnobjects.Vehicle;

import java.util.ArrayList;

/**
 * @author Rafal Zawadzki
 */
public class VehicleView extends VerticalLayout implements View {

    private final String SHEETTAB_GENERAL_SIZE = "200px"; // constant
    private final Integer SIZE_PER_TAB = 46;
    private Table vehiclesList = new Table();
    private TextField searchField = new TextField();
    private Button addNewVehicleButton = new Button("Nowy");
    private VerticalLayout rightLayout = new VerticalLayout();
    private CustomMap customMap;
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    public static final String BRAND = "Marka";
    public static final String COLOUR = "Kolor nadwozia";
    public static final String REGISTRATION_NR = "Nr rejestracyjny";
    public static final String MAX_LOAD = "Ładowność (kg)";
    public static final String ID = "Id";
    private static final String[] firstTab = new String[]{BRAND, REGISTRATION_NR,
            COLOUR, MAX_LOAD};
    private static final String[] notVisible = new String[]{ID};
    IndexedContainer vehiclesContainer;

    public VehicleView() {
        setSizeFull();
        initLayout();
    }

    private void initLayout() {
        Component navigationBar = DefaultViewBuilderHelper.getDefaultMenuPanel();
        Component bodyContent = getBodyContent();

        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.setSizeFull();
        mainPanel.setWidth(DefaultViewBuilderHelper.PAGE_WIDTH);
        mainPanel.addComponent(navigationBar);
        mainPanel.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
        mainPanel.addComponent(bodyContent);
        mainPanel.setExpandRatio(bodyContent, 1.0f);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(mainPanel);
        layout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
        layout.setSizeFull();

        addComponent(layout);

    }

    private Component getBodyContent() {
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        splitPanel.setSplitPosition(DefaultViewBuilderHelper.DEFAULT_SPLIT_POSITION);
        splitPanel.setLocked(true);

        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(rightLayout);
        leftLayout.addComponent(vehiclesList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewVehicleButton);

        rightLayout.setSizeFull();

        leftLayout.setSizeFull();

        leftLayout.setExpandRatio(vehiclesList, 1);
        vehiclesList.setSizeFull();

        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);

        editorLayout.setMargin(true);
        initVehiclesList();
        initEditor();
        initMap();
        initSearch();
        initAddRemoveButtons();

        return splitPanel;
    }

    private void initEditor() {

        rightLayout.addComponent(editorLayout);
        rightLayout.setVisible(false);
        editorLayout.addComponent(getVehicleMenu());
        TabSheet tabsheet = new TabSheet();

        final FormLayout verLayout1 = new FormLayout();
        verLayout1.setMargin(true);
        for (String fieldName : firstTab) {
            TextField field = new TextField(fieldName);
            field.setWidth("20em");

            verLayout1.addComponent(field);
            editorFields.bind(field, fieldName);
            editorFields.setBuffered(false); // narazie może być, ale musi być
            // true aby podłączone
            // walidatory działały
        }

        editorFields.setBuffered(false);
        editorFields.setEnabled(false);
        tabsheet.addTab(verLayout1, "Informacje ogólne", null);
        editorLayout.addComponent(tabsheet);

    }

    private void initMap() {
        customMap = new CustomMap();
        customMap.clearMarkers();
        rightLayout.addComponent(customMap.getCustomMap());
        rightLayout.setExpandRatio(customMap.getCustomMap(), 1.0f);
    }

    private void initSearch() {

        searchField.setInputPrompt("Wyszukaj");

        searchField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);

        searchField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            public void textChange(final FieldEvents.TextChangeEvent event) {

                vehiclesContainer.removeAllContainerFilters();
                vehiclesContainer.addContainerFilter(new DefaultFilter(event
                        .getText()));
            }
        });
    }

    private class DefaultFilter implements Container.Filter {
        private String needle;

        public DefaultFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        public boolean appliesToProperty(Object id) {
            return true;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item)
                throws UnsupportedOperationException {
            String haystack = ("" + item.getItemProperty(BRAND).getValue()
                    + item.getItemProperty(COLOUR).getValue() + item
                    .getItemProperty(REGISTRATION_NR).getValue()).toLowerCase();
            return haystack.contains(needle);
        }
    }

    //TODO
    private void initAddRemoveButtons() {
        addNewVehicleButton.addClickListener(new Button.ClickListener() {
            @SuppressWarnings("unchecked")
            public void buttonClick(Button.ClickEvent event) {

                vehiclesContainer.removeAllContainerFilters();
                Object contactId = vehiclesContainer.addItemAt(0);

                vehiclesList.getContainerProperty(contactId, BRAND).setValue(
                        "Nowy");
                vehiclesList.getContainerProperty(contactId, COLOUR).setValue(
                        "Pojazd");

                vehiclesList.select(contactId);
            }
        });
    }

    private void initVehiclesList() {
        vehiclesList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object currentVehicle = vehiclesList.getValue();
                if (currentVehicle != null) {
                    editorFields.setItemDataSource(vehiclesList
                            .getItem(currentVehicle));
                    editorFields.setEnabled(false);
                }

                rightLayout.setVisible(currentVehicle != null);
            }
        });
    }

    private MenuBar getVehicleMenu() {
        MenuBar vehicleMenu = new MenuBar();
        MenuBar.MenuItem mainItem = vehicleMenu.addItem("Usuń", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Object currentVehicle = vehiclesList.getValue();
                String result = ReceiveVehicle.deleteVehicle((Long) vehiclesList
                        .getContainerProperty(currentVehicle, ID)
                        .getValue());
                Notification.show("Kierowca został usunięty");
                refreshDataSource();
            }
        });

        vehicleMenu.addItem("Edytuj", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                EditVehicleWindow window = new EditVehicleWindow(vehiclesList);
                getUI().addWindow(window);
                refreshDataSource();
            }
        });

        return vehicleMenu;
    }

    private IndexedContainer prepareForVehiclesList(ArrayList<Vehicle> allVehicles) {
        IndexedContainer ic = new IndexedContainer();

        for (String p : notVisible) {
            if (p.equals(ID))
                ic.addContainerProperty(p, Long.class, new Long(-1));
            else
                ic.addContainerProperty(p, String.class, "");
        }
        for (String p : firstTab) {
            if (p.equals(MAX_LOAD))
                ic.addContainerProperty(p, Integer.class, null);
            else
                ic.addContainerProperty(p, String.class, "");
        }

        for (Vehicle vehicle : allVehicles) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, ID).setValue(vehicle.getId());
            ic.getContainerProperty(id, BRAND).setValue(vehicle.getBrand() != null ? vehicle.getBrand() : "");
            ic.getContainerProperty(id, COLOUR).setValue(vehicle.getColour() != null ? vehicle.getColour() : "");
            ic.getContainerProperty(id, REGISTRATION_NR).setValue(vehicle.getRegistrationNumber() != null ? vehicle.getRegistrationNumber() : "");
            ic.getContainerProperty(id, MAX_LOAD).setValue(vehicle.getTruckload());
        }

        return ic;
    }

    private void refreshDataSource() {
        vehiclesContainer = prepareForVehiclesList(ReceiveVehicle.getVehiclesList());
        vehiclesList.setContainerDataSource(vehiclesContainer);
        vehiclesList.setColumnReorderingAllowed(false);
        vehiclesList.setVisibleColumns(new Object[]{BRAND,
                REGISTRATION_NR});
        vehiclesList.setSelectable(true);
        vehiclesList.setImmediate(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        DefaultViewBuilderHelper.checkLogin();
        refreshDataSource();
    }


}
