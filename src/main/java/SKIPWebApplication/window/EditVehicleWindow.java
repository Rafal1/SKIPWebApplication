package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveVehicle;
import SKIPWebApplication.view.VehicleView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import returnobjects.Vehicle;

import java.util.Collection;

/**
 * @author Rafal Zawadzki
 */
public class EditVehicleWindow extends Window {

    public EditVehicleWindow(final VehicleView parent, final long vehID) {
        super("Edycja pojazdu");
        FormLayout newVehicleLayout = new FormLayout();
        final FieldGroup fields = new FieldGroup();
        Table vehiclesList = parent.getVehiclesList();
        fields.setBuffered(true);

        String fieldName = "";
        fieldName = VehicleView.BRAND;
        TextField fieldBRAND = new TextField(fieldName);
        fieldBRAND.setValue((String) vehiclesList.getContainerProperty(vehiclesList.getValue(), VehicleView.BRAND).getValue());
        fieldBRAND.addValidator(new StringLengthValidator("Niepoprawna długość pola marka", 3, 64, false));
        fieldBRAND.addValidator(new RegexpValidator("^[a-zA-Z-]+$", "Marka zawiera nie właściwe znaki"));
        fieldBRAND.setWidth("20em");
        fieldBRAND.setRequired(true);
        fieldBRAND.setRequiredError("Pole Marka jest wymagane");
        fieldBRAND.setImmediate(true);
        newVehicleLayout.addComponent(fieldBRAND);
        fields.bind(fieldBRAND, fieldName);

        fieldName = VehicleView.COLOUR;
        TextField fieldCOLOUR = new TextField(fieldName);
        fieldCOLOUR.setValue((String) vehiclesList.getContainerProperty(vehiclesList.getValue(), VehicleView.COLOUR).getValue());
        fieldCOLOUR.addValidator(new StringLengthValidator("Niepoprawna długość pola Kolor nadwozia", 3, 64, false));
        fieldCOLOUR.addValidator(new RegexpValidator("^[a-zA-Z-]+$", "pole Kolor nadwozia zawiera niewłaściwe znaki"));
        fieldCOLOUR.setWidth("20em");
        fieldCOLOUR.setRequired(true);
        fieldCOLOUR.setRequiredError("Pole Kolor nadwozia jest wymagane");
        fieldCOLOUR.setImmediate(true);
        newVehicleLayout.addComponent(fieldCOLOUR);
        fields.bind(fieldCOLOUR, fieldName);

        fieldName = VehicleView.MAX_LOAD;
        TextField fieldMAX_LOAD = new TextField(fieldName);
        fieldMAX_LOAD.setValue(vehiclesList.getContainerProperty(vehiclesList.getValue(), VehicleView.MAX_LOAD).getValue().toString());
        fieldMAX_LOAD.setConverter(Integer.class);
        fieldMAX_LOAD.setConversionError("Wprowadzona wartość nie jest liczbą");
        fieldMAX_LOAD.addValidator(new IntegerRangeValidator("Niewłaściwa wartość ładowności", 0, Integer.MAX_VALUE));
        fieldMAX_LOAD.setWidth("20em");
        fieldMAX_LOAD.setRequired(true);
        fieldMAX_LOAD.setRequiredError("Pole Ładowność jest wymagane");
        fieldMAX_LOAD.setImmediate(true);
        newVehicleLayout.addComponent(fieldMAX_LOAD);
        fields.bind(fieldMAX_LOAD, fieldName);

        fieldName = VehicleView.REGISTRATION_NR;
        TextField fieldREGISTRATION_NR = new TextField(fieldName);
        fieldREGISTRATION_NR.setValue((String) vehiclesList.getContainerProperty(vehiclesList.getValue(), VehicleView.REGISTRATION_NR).getValue());
        fieldREGISTRATION_NR.addValidator(new StringLengthValidator("Niepoprawna długość pola Nr rejestracyjny", 4, 12, false));
        fieldREGISTRATION_NR.addValidator(new RegexpValidator("^[A-Z0-9-]+$", "pole Kolor zawiera nie właściwe znaki"));
        fieldREGISTRATION_NR.setWidth("20em");
        fieldREGISTRATION_NR.setRequired(true);
        fieldREGISTRATION_NR.setRequiredError("Pole Nr rejestracyjny jest wymagane");
        fieldREGISTRATION_NR.setImmediate(true);
        newVehicleLayout.addComponent(fieldREGISTRATION_NR);
        fields.bind(fieldREGISTRATION_NR, fieldName);

        Button changeDriver = new Button("Zatwierdź");
        Button cancel = new Button("Anuluj");
        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(changeDriver);
        hl.addComponent(cancel);
        newVehicleLayout.addComponent(hl);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        changeDriver.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                Vehicle veh = new Vehicle();
                veh.setId(vehID);
                veh.setBrand((String) fields.getField(VehicleView.BRAND).getValue());
                veh.setColour((String) fields.getField(VehicleView.COLOUR).getValue());

                Integer truckLoad = null;
                try{
                   truckLoad = Integer.parseInt(fields.getField(VehicleView.MAX_LOAD).getValue().toString());
                } catch(NumberFormatException e) {
                    Notification delayNot = new Notification("Proszę wypełnić pola poprawnie");
                    delayNot.setDelayMsec(1000);
                    delayNot.show(Page.getCurrent());
                    return;
                }
                veh.setTruckload(truckLoad);
                veh.setRegistrationNumber((String) fields.getField(VehicleView.REGISTRATION_NR).getValue());

                Boolean valOk = true;
                Collection colFields = fields.getFields();
                for (Object o : colFields) {
                    Field fi = (Field) o;
                    try {
                        fi.validate();
                    } catch (Validator.InvalidValueException e) {
                        Notification delayNot = new Notification("Proszę wypełnić pola poprawnie");
                        delayNot.setDelayMsec(1000);
                        delayNot.show(Page.getCurrent());
                        valOk = false;
                        break;
                    }
                }

                if (valOk) {
                    veh = ReceiveVehicle.changeVehicle(veh);
                    if(veh == null){
                        Notification.show("Nie wprowadzono zmian");
                        close();
                        return;
                    }
                    Notification.show("Wprowadzono zmiany");
                    parent.refreshDataSource();
                    close();
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

        setContent(newVehicleLayout);

        setResizable(false);

        setDraggable(false);

        center();
    }
}