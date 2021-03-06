package SKIPWebApplication.view;

import SKIPWebApplication.SkipapplicationService;
import SKIPWebApplication.receiveinformation.ReceiveDriver;
import SKIPWebApplication.window.AddDriverWindow;
import SKIPWebApplication.window.AssociateVehicleWindow;
import SKIPWebApplication.window.EditDriverWindow;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import custommap.CustomMap;
import returnobjects.Driver;
import returnobjects.Vehicle;

import java.util.ArrayList;

/**
 * @author Rafal Zawadzki
 */
public class DriversView extends VerticalLayout implements View {
    private static final String NO_REGISTRATION_NUMBER = "(brak)";
    private final String SHEETTAB_GENERAL_SIZE = "184px"; // constant
    private static String SHEETTAB_DETAIL_SIZE;
    private final Integer SIZE_PER_TAB = 46;
    private Table driversList = new Table();
    private TextField searchField = new TextField();
    private Button addNewDriverButton = new Button("Nowy kierowca");
    private VerticalLayout rightLayout = new VerticalLayout();
    private CustomMap customMap;
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    public static final String ID = "Id";
    public static final String FNAME = "Imię:";
    public static final String LNAME = "Nazwisko:";
    private static final String REGISTRATION_NR = "Nr rejestracyjny:";
    public static final String COMPANY_PHONE = "Tel. firmowy:";
    public static final String PRIVATE_PHONE = "Tel. prywatny:";
    public static final String ASSOCIATED_VEHICLE = "Powiązany pojazd:";
    public static final String E_MAIL = "E-mail:";
    private static final String LAST_DATE = "Zlokalizowano:";
    private static final String COORDINATES = "Współrzędne:";
    private static final String[] firstTab = new String[]{FNAME, LNAME,
            REGISTRATION_NR, COMPANY_PHONE};
    private static final String[] secondTab = new String[]{PRIVATE_PHONE, E_MAIL,
            LAST_DATE};

    private static final String[] notVisible = new String[]{ID, COORDINATES};
    private ArrayList<Driver> currentDrList = ReceiveDriver.getDriversList();
    private IndexedContainer driversContainer;
    MenuBar.MenuItem deleteAssociate;

    public DriversView() {
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
        initDriverList();
        initEditor();
        initMap();
        initSearch();
        initAddRemoveButtons();

        return splitPanel;
    }

    private MenuBar createDriverMenu() {
        MenuBar driverMenu = new MenuBar();
        MenuBar.MenuItem mainItem = driverMenu.addItem("Usuń", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Object contactId = driversList.getValue();
                String result = ReceiveDriver.deleteDriver((Long) driversList
                        .getContainerProperty(contactId, ID)
                        .getValue());
                if (result != null) {
                    Notification.show("Kierowca został usunięty");
                    refreshDataSource();
                } else {
                    Notification.show("Wystąpił błąd podczas usuwania kierowcy.");
                }
            }
        });
        driverMenu.addItem("Edytuj", new EditDriverCommand(this));
        driverMenu.addItem("Przypisz pojazd", new AssociateVehileCommand(this));
        deleteAssociate = driverMenu.addItem("Usuń przypisanie pojazdu", new DeleteAssociateVehileCommand());
        return driverMenu;
    }

    private void initEditor() {

        rightLayout.addComponent(editorLayout);
        editorLayout.addComponent(createDriverMenu());
        rightLayout.setVisible(false);
        TabSheet tabsheet = new TabSheet();

        final FormLayout verLayout1 = new FormLayout();
        verLayout1.setMargin(true);
        for (String fieldName : firstTab) {
            TextField field = new TextField(fieldName);
            field.setWidth("15em");
            verLayout1.addComponent(field);
            editorFields.bind(field, fieldName);
            editorFields.setBuffered(true);
        }

        FormLayout verLayout2 = new FormLayout();
        verLayout2.setMargin(true);
        for (String fieldName : secondTab) {
            TextField field = new TextField(fieldName);
            field.setWidth("15em");
            verLayout2.addComponent(field);

            verLayout2.addComponent(field);

            editorFields.bind(field, fieldName);
            editorFields.setBuffered(true);
        }


        SHEETTAB_DETAIL_SIZE = SkipapplicationService.createFormatForDetailSizeTab(SIZE_PER_TAB
                * secondTab.length);

        editorFields.setBuffered(true);
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
                if (Integer.parseInt(SHEETTAB_DETAIL_SIZE.substring(0, SHEETTAB_DETAIL_SIZE.indexOf("px")))
                        > Integer.parseInt(SHEETTAB_GENERAL_SIZE.substring(0, SHEETTAB_GENERAL_SIZE.indexOf("px")))) {
                    tabsheet.setHeight(SHEETTAB_DETAIL_SIZE);
                } else {
                    tabsheet.setHeight(SHEETTAB_GENERAL_SIZE);
                }
            }
        };
        return listener;
    }

    private void initMap() {
        customMap = new CustomMap();
        VerticalLayout mapLayut = (VerticalLayout) customMap.getCustomMap();
        rightLayout.addComponent(mapLayut);
        rightLayout.setExpandRatio(mapLayut, 1.0f);
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
        addNewDriverButton.addClickListener(new AddDriverClickListener(this));
    }

    private class AddDriverClickListener implements Button.ClickListener {
        DriversView dv;

        public AddDriverClickListener(DriversView dv) {
            this.dv = dv;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            AddDriverWindow window = new AddDriverWindow(dv);
            getUI().addWindow(window);
        }
    }

    private class EditDriverCommand implements MenuBar.Command {
        DriversView dv;

        public EditDriverCommand(DriversView dv) {
            this.dv = dv;
        }

        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Object currentDriver = driversList.getValue();
            Long driverId = (Long) driversList
                    .getContainerProperty(currentDriver, ID)
                    .getValue();
            EditDriverWindow window = new EditDriverWindow(dv, driverId, currentDrList);
            getUI().addWindow(window);
        }
    }

    private class AssociateVehileCommand implements MenuBar.Command {
        DriversView dv;

        public AssociateVehileCommand(DriversView dv) {
            this.dv = dv;
        }

        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Object currentDriver = driversList.getValue();
            Long driverId = (Long) driversList
                    .getContainerProperty(currentDriver, ID)
                    .getValue();
            AssociateVehicleWindow window = new AssociateVehicleWindow(dv, driverId);
            getUI().addWindow(window);
        }
    }

    private class DeleteAssociateVehileCommand implements MenuBar.Command {
        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Object currentDriver = driversList.getValue();
            Long driverId = (Long) driversList
                    .getContainerProperty(currentDriver, ID)
                    .getValue();
            boolean result = ReceiveDriver.deleteVehicleAssigment(driverId);
            if (result)
                Notification.show("Usunięto przypisanie pojazdu do kierowcy");
            else
                Notification.show("Błąd podczas usunięcia przypisania pojazdu");
            refreshDataSource();
        }
    }


    private void initDriverList() {
        driversList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object currentDriver = driversList.getValue();
                if (currentDriver != null) {

                    if(driversList.getContainerProperty(currentDriver, REGISTRATION_NR).getValue()
                            .equals(NO_REGISTRATION_NUMBER))
                        deleteAssociate.setVisible(false);
                    else
                        deleteAssociate.setVisible(true);

                    editorFields.setItemDataSource(driversList
                            .getItem(currentDriver));

                    editorFields.setReadOnly(true);
                    // mapa
                    String info = "Kierowca: " + (String) driversList
                            .getContainerProperty(currentDriver, FNAME)
                            .getValue() + " " + (String) driversList
                            .getContainerProperty(currentDriver, LNAME)
                            .getValue() + "\nPojazd: " + (String) driversList
                            .getContainerProperty(currentDriver, REGISTRATION_NR)
                            .getValue();
                    if (driversList.getContainerProperty(currentDriver, COORDINATES).getValue() != null) {
                        customMap.getCustomMap().setVisible(true);
                        customMap.clearMarkers();
                        customMap.addOneMarker(info, (LatLon) driversList
                                .getContainerProperty(currentDriver, COORDINATES)
                                .getValue());
                    } else {
                        customMap.getCustomMap().setVisible(false);
                    }
                }

                rightLayout.setVisible(currentDriver != null);
            }
        });
    }

    @SuppressWarnings("deprecation")
    private IndexedContainer prepareForDriversList(ArrayList<Driver> allDrivers) {
        IndexedContainer ic = new IndexedContainer();

        for (String p : notVisible) {
            if (p.equals(COORDINATES))
                ic.addContainerProperty(COORDINATES, LatLon.class, new LatLon());
            else if (p.equals(ID))
                ic.addContainerProperty(p, Long.class, new Long(-1));
            else
                ic.addContainerProperty(p, String.class, "");
        }
        for (String p : firstTab) {
            ic.addContainerProperty(p, String.class, "");
        }
        for (String p : secondTab) {
            if (p.equals(LAST_DATE))
                ic.addContainerProperty(p, String.class, "");
            else
                ic.addContainerProperty(p, String.class, "");
        }

        for (Driver driver : allDrivers) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, ID).setValue(driver.getId());
            ic.getContainerProperty(id, FNAME).setValue(driver.getFirstName());
            ic.getContainerProperty(id, LNAME).setValue(driver.getLastName());

            //wysłanie zapytania o przypisany pojazd
            Long driverID = driver.getId();

            Vehicle assignVehicle;
            try {
                assignVehicle = ReceiveDriver.getAssignedVehicle(driverID);
            } catch (Exception e) {
                assignVehicle = null;
            }

            if (assignVehicle != null) {
                ic.getContainerProperty(id, REGISTRATION_NR)
                        .setValue(assignVehicle.getRegistrationNumber());
            } else {
                ic.getContainerProperty(id, REGISTRATION_NR)
                        .setValue(NO_REGISTRATION_NUMBER);
            }

            if (driver.getLatestCoordinates() != null &&
                    (driver.getLatestCoordinates().getLatitude() != 0
                            && driver.getLatestCoordinates().getLongitude() != 0))
                ic.getContainerProperty(id, COORDINATES).setValue(new LatLon(driver.getLatestCoordinates().getLatitude(), driver.getLatestCoordinates().getLongitude()));
            else
            ic.getContainerProperty(id, COORDINATES).setValue(null);
            ic.getContainerProperty(id, COMPANY_PHONE).setValue(driver.getPhoneNumber() != null ? driver.getPhoneNumber() : "(brak)");
            ic.getContainerProperty(id, PRIVATE_PHONE).setValue(driver.getPhoneNumber2() != null ? driver.getPhoneNumber2() : "(brak)");
            ic.getContainerProperty(id, E_MAIL).setValue(driver.getEmail() != null ? driver.getEmail() : "(brak)");
            ic.getContainerProperty(id, LAST_DATE).setValue(driver.getCoordinatesUpdateDate() != null ? driver.getCoordinatesUpdateDate().toLocaleString() : "(brak)");
        }

        return ic;
    }

    public void refreshDataSource() {
        ArrayList<Driver> newListOfDrivers = ReceiveDriver.getDriversList();
        currentDrList = newListOfDrivers;
        driversContainer = prepareForDriversList(newListOfDrivers);
        driversList.setContainerDataSource(driversContainer);
        driversList.setColumnReorderingAllowed(false);
        driversList.setVisibleColumns(new Object[]{FNAME, LNAME,
                REGISTRATION_NR});
        driversList.setSelectable(true);
        driversList.setImmediate(true);
    }

    public Table getDriversList() {
        return driversList;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        DefaultViewBuilderHelper.checkLogin();
        refreshDataSource();
    }

}
