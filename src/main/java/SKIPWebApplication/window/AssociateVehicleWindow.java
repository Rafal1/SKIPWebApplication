package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveDriver;
import SKIPWebApplication.receiveinformation.ReceiveVehicle;
import SKIPWebApplication.view.DriversView;
import SKIPWebApplication.view.VehicleView;
import com.vaadin.ui.*;
import returnobjects.Vehicle;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz
 * Date: 25.05.14
 * Time: 18:00

 */
public class AssociateVehicleWindow extends Window {
    Table vehicleTable;
    Long driverId;

    public AssociateVehicleWindow(final DriversView parent, final Long driverId) {
        super("Przypisanie pojazdu");
        this.driverId = driverId;
        FormLayout associateLayout = new FormLayout();
        createVehicleTable();
        associateLayout.addComponent(vehicleTable);
        associateLayout.setSpacing(true);

        Button changeDriver = new Button("Zatwierdź");
        Button cancel = new Button("Anuluj");

        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(changeDriver);
        hl.addComponent(cancel);

        associateLayout.addComponent(hl);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        changeDriver.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                Object rowId = vehicleTable.getValue(); // get the selected rows id
                if (rowId != null) {
                    if (ReceiveDriver.assignVehicle(driverId, (Long) rowId)) {
                        Notification.show("Przypisano nowy pojazd do kierowcy");
                        parent.refreshDataSource();
                    } else {
                        Notification.show("Błąd: Nie udało się przypisać pojazdu.");
                    }
                    close();
                } else {
                    Notification.show("Prosze zaznaczyć pojazd który ma zostać przypisany.");
                }
            }
        }
        );


        addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                parent.setEnabled(true);
            }
        });

        parent.setEnabled(false);

        setContent(associateLayout);

        setResizable(false);

        setDraggable(false);

        center();
    }

    private void createVehicleTable() {
        vehicleTable = new Table();
        vehicleTable.setWidth("40em");
        vehicleTable.addContainerProperty(VehicleView.BRAND, String.class, null);
        vehicleTable.addContainerProperty(VehicleView.REGISTRATION_NR, String.class, null);
        getDateSource();

    }

    private void addDataToVehicleTable(ArrayList<Vehicle> allVehicles) {
        for (Vehicle vehicle : allVehicles) {
            vehicleTable.addItem(new Object[]{
                    vehicle.getBrand(), vehicle.getRegistrationNumber()}, vehicle.getId());
        }
    }

    protected void getDateSource() {
        addDataToVehicleTable(ReceiveVehicle.getVehiclesList());
        vehicleTable.setVisibleColumns(new Object[]{VehicleView.BRAND,
                VehicleView.REGISTRATION_NR});
        vehicleTable.setSelectable(true);
        vehicleTable.setImmediate(true);
    }

}


