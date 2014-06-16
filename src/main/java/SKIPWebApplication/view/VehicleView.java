package SKIPWebApplication.view;

import SKIPWebApplication.receiveinformation.ReceiveDriver;
import SKIPWebApplication.receiveinformation.ReceiveVehicle;
import SKIPWebApplication.window.AddVehicleWindow;
import SKIPWebApplication.window.EditVehicleWindow;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import returnobjects.Driver;
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
    private Button addNewVehicleButton = new Button("Nowy pojazd");
    private VerticalLayout rightLayout = new VerticalLayout();
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    public static final String BRAND = "Model:";
    public static final String COLOUR = "Kolor nadwozia:";
    public static final String REGISTRATION_NR = "Nr rejestracyjny:";
    public static final String MAX_LOAD = "Ładowność (kg):";
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
            editorFields.setBuffered(false);
        }

        editorFields.setBuffered(false);

        tabsheet.addTab(verLayout1, "Informacje", null);
        editorLayout.addComponent(tabsheet);

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

    private void initAddRemoveButtons() {
        addNewVehicleButton.addClickListener(new AddVehicleClickListener(this));
    }

    private void initVehiclesList() {
        vehiclesList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object currentVehicle = vehiclesList.getValue();
                if (currentVehicle != null) {
                    editorFields.setItemDataSource(vehiclesList
                            .getItem(currentVehicle));
                    editorFields.setReadOnly(true);
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
                ArrayList<Driver> ListToFind = ReceiveDriver.getDriversList();

                Long driverID = null;
                for (Driver d : ListToFind) {
                    if(d.getAssignedVehicle()==null){
                        //System.out.println("JEST");
                        continue;
                    }
                    Long curID = d.getAssignedVehicle().getId();
                    if (curID == vehiclesList
                            .getContainerProperty(currentVehicle, ID)
                            .getValue()) {
                        driverID = d.getId();
                    }
                }
                if (driverID != null) {
                    ReceiveDriver.deleteVehicleAssigment(driverID);
                }
                String result = ReceiveVehicle.deleteVehicle((Long) vehiclesList
                        .getContainerProperty(currentVehicle, ID)
                        .getValue());
                Notification.show("Pojazd został usunięty");
                refreshDataSource();
            }
        });

        vehicleMenu.addItem("Edytuj", new EditVehicleCommand(this));

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
            ic.getContainerProperty(id, BRAND).setValue(vehicle.getBrand() != null ? vehicle.getBrand() : "(brak)");
            ic.getContainerProperty(id, COLOUR).setValue(vehicle.getColour() != null ? vehicle.getColour() : "(brak)");
            ic.getContainerProperty(id, REGISTRATION_NR).setValue(vehicle.getRegistrationNumber() != null ? vehicle.getRegistrationNumber() : "(brak)");
            ic.getContainerProperty(id, MAX_LOAD).setValue(vehicle.getTruckload());
        }

        return ic;
    }

    public void refreshDataSource() {
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

    private class AddVehicleClickListener implements Button.ClickListener {
        VehicleView vv;

        public AddVehicleClickListener(VehicleView vv) {
            this.vv = vv;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            AddVehicleWindow window = new AddVehicleWindow(vv);
            getUI().addWindow(window);
        }

    }

    public Table getVehiclesList() {
        return vehiclesList;
    }

    private class EditVehicleCommand implements MenuBar.Command {
        VehicleView vv;

        public EditVehicleCommand(VehicleView vv) {
            this.vv = vv;
        }

        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Object currentDriver = vehiclesList.getValue();
            Long driverID = (Long) vehiclesList
                    .getContainerProperty(currentDriver, ID)
                    .getValue();
            EditVehicleWindow window = new EditVehicleWindow(vv, driverID);
            getUI().addWindow(window);
        }
    }

}
