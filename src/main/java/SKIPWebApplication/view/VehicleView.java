package SKIPWebApplication.view;

import SKIPWebApplication.SkipapplicationService;
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

/**
 * @author Rafal Zawadzki
 */
public class VehicleView extends VerticalLayout implements View {

    private final String SHEETTAB_GENERAL_SIZE = "153px"; // constant
    private static String SHEETTAB_DETAIL_SIZE;
    private final Integer SIZE_PER_TAB = 46;
    private Table vehiclesList = new Table();
    private TextField searchField = new TextField();
    private Button addNewVehicleButton = new Button("Nowy");
    private VerticalLayout rightLayout = new VerticalLayout();
    private GoogleMap googleMap;
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    private static final String BRAND = "Marka";
    private static final String MODEL = "Model";
    private static final String REGISTRATION_NR = "Nr rejestracyjny";
    private static final String COORDINATES = "Współrzędne";
    private static final String[] fieldNames = new String[]{BRAND, MODEL,
            REGISTRATION_NR, "Poj. silnika", "Przebieg", "Rok produkcji",
            "Data lokalizacji"};
    IndexedContainer vehiclesContainer = createDummyDatasource();

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
        initContactList();
        initEditor();
        initMap();
        initSearch();
        initAddRemoveButtons();

        return splitPanel;
    }

    private static IndexedContainer createDummyDatasource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

        ic.addContainerProperty(COORDINATES, LatLon.class, new LatLon());

        String[] brands = {"Mercedes", "Renault", "Volvo", "Man", "Scania", "Daf"};
        String[] models = {"Actross", "Magnum", "C1230", "A15002900"};
        String[] regNrs = {"WM2352", "WA32452", "SA35235", "WW34235",
                "WTE3245", "AW32423", "KR32423", "WE23423", "WW34242",
                "WA32423", "RE23424", "WE32423", "WE34234", "WWA4324",
                "WWT4323"};
        LatLon[] coords = {new LatLon(52.0922474, 21.0249287),
                new LatLon(52.0922474, 22.0249287),
                new LatLon(52.1922474, 21.0249287),
                new LatLon(52.2922474, 21.0949287),
                new LatLon(52.3922474, 21.0589287),
                new LatLon(52.0922474, 21.0000287),
                new LatLon(52.1232474, 21.0249287),
                new LatLon(52.0982474, 21.0249287),
                new LatLon(52.9082474, 21.1239287),
                new LatLon(52.0922474, 21.5559287),
                new LatLon(52.1111174, 21.2229287),
                new LatLon(53.0922474, 22.0249287),
                new LatLon(55.0922474, 21.0249287)};
        for (int i = 0; i < 100; i++) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, BRAND).setValue(
                    brands[(int) (brands.length * Math.random())]);
            ic.getContainerProperty(id, MODEL).setValue(
                    models[(int) (models.length * Math.random())]);
            ic.getContainerProperty(id, REGISTRATION_NR).setValue(
                    regNrs[(int) (regNrs.length * Math.random())]);
            ic.getContainerProperty(id, COORDINATES).setValue(
                    coords[(int) (coords.length * Math.random())]);
        }

        return ic;
    }

    private void initEditor() {

        rightLayout.addComponent(editorLayout);
        editorLayout.addComponent(getVehicleMenu());
        TabSheet tabsheet = new TabSheet();

        final FormLayout verLayout1 = new FormLayout();
        verLayout1.setMargin(true);
        if (SkipapplicationService.fieldNamesIfConstains(fieldNames, BRAND)) {
            TextField brandField = new TextField(BRAND);
            brandField.setWidth("15em");
            verLayout1.addComponent(brandField);
            editorFields.bind(brandField, BRAND);
        }
        if (SkipapplicationService.fieldNamesIfConstains(fieldNames, MODEL)) {
            TextField modelField = new TextField(MODEL);
            modelField.setWidth("15em");
            verLayout1.addComponent(modelField);
            editorFields.bind(modelField, MODEL);
        }
        if (SkipapplicationService.fieldNamesIfConstains(fieldNames, REGISTRATION_NR)) {
            TextField regField = new TextField(REGISTRATION_NR);
            regField.setWidth("15em");
            verLayout1.addComponent(regField);
            editorFields.bind(regField, REGISTRATION_NR);
        }

        FormLayout verLayout2 = new FormLayout();
        verLayout2.setMargin(true);
        // todo 9.4.4 VaadinBook (validation)
        Integer count = 0;
        for (String fieldName : fieldNames) {
            if (fieldName != BRAND && fieldName != MODEL
                    && fieldName != REGISTRATION_NR) {
                count++;
                TextField field = new TextField(fieldName);
                if (fieldName.equals("E-mail")) {
                    field.setWidth("20em");
                } else {
                    field.setWidth("10em");
                }
                verLayout2.addComponent(field);
                editorFields.bind(field, fieldName);
            }
            editorFields.setBuffered(false); // narazie może być, ale musi być
            // true aby podłączone
            // walidatory działały
        }

        SHEETTAB_DETAIL_SIZE = SkipapplicationService.createFormatForDetailSizeTab(SIZE_PER_TAB
                * count);

        editorFields.setBuffered(false);
        tabsheet.addTab(verLayout1, "Informacje ogólne", null);
        tabsheet.addTab(verLayout2, "Informacje szczegółowe", null);
        tabsheet.addSelectedTabChangeListener(listenerForTab());

        editorLayout.addComponent(tabsheet);

    }

    public TabSheet.SelectedTabChangeListener listenerForTab() {
        TabSheet.SelectedTabChangeListener listener = new TabSheet.SelectedTabChangeListener() {
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                TabSheet tabsheet = event.getTabSheet();
                TabSheet.Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
                if (tab.getCaption().equals("Informacje szczegółowe")) {
                    tabsheet.setHeight(SHEETTAB_DETAIL_SIZE);
                } else {
                    tabsheet.setHeight(SHEETTAB_GENERAL_SIZE);
                }
            }
        };
        return listener;
    }

    private void initMap() {
        googleMap = new GoogleMap(new LatLon(52.0922474, 21.0249287), 12.0, "");
        googleMap.setSizeFull();
        googleMap.setImmediate(true);
        googleMap.setMinZoom(4.0);
        rightLayout.addComponent(googleMap);
        rightLayout.setExpandRatio(googleMap, 1.0f);
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
                    + item.getItemProperty(MODEL).getValue() + item
                    .getItemProperty(REGISTRATION_NR).getValue()).toLowerCase();
            return haystack.contains(needle);
        }
    }

    private void initAddRemoveButtons() {
        addNewVehicleButton.addClickListener(new Button.ClickListener() {
            @SuppressWarnings("unchecked")
            public void buttonClick(Button.ClickEvent event) {

                vehiclesContainer.removeAllContainerFilters();
                Object contactId = vehiclesContainer.addItemAt(0);

                vehiclesList.getContainerProperty(contactId, BRAND).setValue(
                        "Nowy");
                vehiclesList.getContainerProperty(contactId, MODEL).setValue(
                        "Pojazd");

                vehiclesList.select(contactId);
            }
        });
    }

    private void initContactList() {
        vehiclesList.setContainerDataSource(vehiclesContainer);
        vehiclesList.setColumnReorderingAllowed(false);
        vehiclesList.setVisibleColumns(new Object[]{BRAND, MODEL,
                REGISTRATION_NR});
        vehiclesList.setSelectable(true);
        vehiclesList.setImmediate(true);

        vehiclesList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object contactId = vehiclesList.getValue();

                if (contactId != null) {
                    editorFields.setItemDataSource(vehiclesList
                            .getItem(contactId));
                    // mapa
                    googleMap.clearMarkers();
                    googleMap.setCenter((LatLon) vehiclesList
                            .getContainerProperty(contactId, COORDINATES)
                            .getValue());
                    googleMap.addMarker("Przykład", (LatLon) vehiclesList
                            .getContainerProperty(contactId, COORDINATES)
                            .getValue(), false,
                            "VAADIN/resources/icons/car.png");

                }

                rightLayout.setVisible(contactId != null);
            }
        });
    }

    private MenuBar getVehicleMenu() {
        MenuBar driverMenu = new MenuBar();
        MenuBar.MenuItem mainItem = driverMenu.addItem("Menu", null);
        mainItem.addItem("Usuń", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Object id = vehiclesList.getValue();
                vehiclesList.removeItem(id);
            }
        });
        return driverMenu;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
