package SKIPWebApplication;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;

/**
 * @author Rafal Zawadzki
 */
public class DriversView extends VerticalLayout implements View {
    private static String PAGE_WIDTH = "1024px";
    private final String SHEETTAB_GENERAL_SIZE = "153px"; // constant
    private static String SHEETTAB_DETAIL_SIZE;
    private final Integer SIZE_PER_TAB = 46;
    private final Integer SPLIT_POSITION = 30;
    private Table driversList = new Table();
    private TextField searchField = new TextField();
    private Button addNewDriverButton = new Button("Nowy");
    private Button removeDriverButton = new Button("Usuń");
    private VerticalLayout rightLayout = new VerticalLayout();
    private GoogleMap googleMap;
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    private static final String FNAME = "Imie";
    private static final String LNAME = "Nazwisko";
    private static final String REGISTRATION_NR = "Nr rejestracyjny";
    private static final String COORDINATES = "Współrzędne";
    private static final String[] fieldNames = new String[]{FNAME, LNAME,
            REGISTRATION_NR, "Tel. firmowy", "Tel. prywatny", "E-mail",
            "Data lokalizacji"};

    IndexedContainer driversContainer = createDummyDatasource();

    public DriversView() {
        setSizeFull();
        initLayout();
    }

    private void initLayout() {
        Component navigationBar = createMenuPanel();
        Component bodyContent = getBodyContent();

        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.setSizeFull();
        mainPanel.setWidth(PAGE_WIDTH);
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
        splitPanel.setSplitPosition(SPLIT_POSITION);
        splitPanel.setLocked(true);

        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(rightLayout); // zmiana z eitorLayout na
        // rightLayout
        MenuBar testMenu = createTestMenu();
        leftLayout.addComponent(testMenu);
        leftLayout.addComponent(driversList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewDriverButton);

        rightLayout.setSizeFull();

        leftLayout.setSizeFull();

        leftLayout.setExpandRatio(driversList, 1);
        driversList.setSizeFull();

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

    private MenuBar createTestMenu() {
        MenuBar testMenu = new MenuBar();
        MenuBar.MenuItem mainItem = testMenu.addItem("Menu", null);
        MenuBar.Command cmd = new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Notification.show("Wybrales " + selectedItem.getText()
                        + " z menu");
            }
        };
        mainItem.addItem("Akcja1", cmd);
        mainItem.addItem("Akcja2", cmd);
        mainItem.addItem("Akcja3", cmd);
        return testMenu;
    }

    private Component createMenuPanel() {
        HorizontalLayout hl = new HorizontalLayout();
        ThemeResource logo = new ThemeResource("../../resources/icons/skip.png");
        Image logoComp = new Image(null, logo);
        hl.addComponent(logoComp);
        MenuBar.Command cmd = new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Notification.show("Wybrales " + selectedItem.getText()
                        + " z menu");

                //TODO conditions for other views
                if (selectedItem.getText().equals("Wyloguj się")) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.LOGIN_VIEW);
                }
            }
        };
        MenuBar menu = new MenuBar();
        menu.addItem("Główna", new ThemeResource(
                "../../resources/icons/home.png"), cmd);
        menu.addItem("Kierowcy", new ThemeResource(
                "../../resources/icons/drivers.png"), cmd);
        menu.addItem("Pojazdy", new ThemeResource(
                "../../resources/icons/car.png"), cmd);
        menu.addItem("Wiadomości", new ThemeResource(
                "../../resources/icons/messages.png"), cmd);
        menu.addItem("Wyloguj się", new ThemeResource(
                "../../resources/icons/log_out.png"), cmd);
        menu.setHeight("73px");
        hl.addComponent(menu);
        hl.setComponentAlignment(logoComp, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(menu, Alignment.MIDDLE_CENTER);
        hl.setHeight("80px");
        return hl;
    }

    private void initEditor() {

        rightLayout.addComponent(editorLayout);
        editorLayout.addComponent(removeDriverButton);
        TabSheet tabsheet = new TabSheet();

        final FormLayout verLayout1 = new FormLayout();
        verLayout1.setMargin(true);
        if (SkipapplicationService.fieldNamesIfConstains(FNAME)) {
            TextField FNAMEfield = new TextField(FNAME);
            FNAMEfield.setWidth("15em");
            verLayout1.addComponent(FNAMEfield);
            editorFields.bind(FNAMEfield, FNAME);
        }
        if (SkipapplicationService.fieldNamesIfConstains(LNAME)) {
            TextField LNAMEfield = new TextField(LNAME);
            LNAMEfield.setWidth("15em");
            verLayout1.addComponent(LNAMEfield);
            editorFields.bind(LNAMEfield, LNAME);
        }
        if (SkipapplicationService.fieldNamesIfConstains(REGISTRATION_NR)) {
            TextField REGfield = new TextField(REGISTRATION_NR);
            REGfield.setWidth("15em");
            verLayout1.addComponent(REGfield);
            editorFields.bind(REGfield, REGISTRATION_NR);
        }

        FormLayout verLayout2 = new FormLayout();
        verLayout2.setMargin(true);
        // todo 9.4.4 VaadinBook (validation)
        Integer count = 0;
        for (String fieldName : fieldNames) {
            if (fieldName != FNAME && fieldName != LNAME
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

                driversContainer.removeAllContainerFilters();
                driversContainer.addContainerFilter(new ContactFilter(event
                        .getText()));
            }
        });
    }

    private class ContactFilter implements Container.Filter {
        private String needle;

        public ContactFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        public boolean appliesToProperty(Object id) {
            return true;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item)
                throws UnsupportedOperationException {
            String haystack = ("" + item.getItemProperty(FNAME).getValue()
                    + item.getItemProperty(LNAME).getValue() + item
                    .getItemProperty(REGISTRATION_NR).getValue()).toLowerCase();
            return haystack.contains(needle);
        }
    }

    private void initAddRemoveButtons() {
        addNewDriverButton.addClickListener(new Button.ClickListener() {
            @SuppressWarnings("unchecked")
            public void buttonClick(Button.ClickEvent event) {

                driversContainer.removeAllContainerFilters();
                Object contactId = driversContainer.addItemAt(0);

                driversList.getContainerProperty(contactId, FNAME).setValue(
                        "Nowy");
                driversList.getContainerProperty(contactId, LNAME).setValue(
                        "Kierowca");

                driversList.select(contactId);
            }
        });

        removeDriverButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object contactId = driversList.getValue();
                driversList.removeItem(contactId);
            }
        });
    }

    private void initContactList() {
        driversList.setContainerDataSource(driversContainer);
        driversList.setColumnReorderingAllowed(false);
        driversList.setVisibleColumns(new Object[]{FNAME, LNAME,
                REGISTRATION_NR});
        driversList.setSelectable(true);
        driversList.setImmediate(true);

        driversList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object contactId = driversList.getValue();

                if (contactId != null) {
                    editorFields.setItemDataSource(driversList
                            .getItem(contactId));
                    // mapa
                    googleMap.clearMarkers();
                    googleMap.setCenter((LatLon) driversList
                            .getContainerProperty(contactId, COORDINATES)
                            .getValue());
                    googleMap.addMarker("Przykład", (LatLon) driversList
                            .getContainerProperty(contactId, COORDINATES)
                            .getValue(), false,
                            "VAADIN/resources/icons/drivers.png");

                }

                rightLayout.setVisible(contactId != null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static IndexedContainer createDummyDatasource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

        ic.addContainerProperty(COORDINATES, LatLon.class, new LatLon());

        String[] fnames = {"Peter", "Alice", "Joshua", "Mike", "Olivia",
                "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
                "Lisa", "Marge"};
        String[] lnames = {"Smith", "Gordon", "Simpson", "Brown", "Clavel",
                "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
                "Barks", "Ross", "Schneider", "Tate"};
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
            ic.getContainerProperty(id, FNAME).setValue(
                    fnames[(int) (fnames.length * Math.random())]);
            ic.getContainerProperty(id, LNAME).setValue(
                    lnames[(int) (lnames.length * Math.random())]);
            ic.getContainerProperty(id, REGISTRATION_NR).setValue(
                    regNrs[(int) (regNrs.length * Math.random())]);
            ic.getContainerProperty(id, COORDINATES).setValue(
                    coords[(int) (coords.length * Math.random())]);
        }

        return ic;
    }

    public static String[] getFieldnames() {
        return fieldNames;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Notification.show("Widok kierowców");
    }

}
