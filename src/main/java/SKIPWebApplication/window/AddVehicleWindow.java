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
public class AddVehicleWindow extends Window {

    public AddVehicleWindow(final VehicleView parent) {
        super("Nowy pojazd");
        FormLayout newVehicleLayout = new FormLayout();
        final FieldGroup fields = new FieldGroup();
        fields.setBuffered(true);

        String fieldName = "";
        fieldName = VehicleView.BRAND;
        TextField fieldBRAND = new TextField(fieldName);
        fieldBRAND.addValidator(new StringLengthValidator("Niepoprawna długość pola marka", 3, 64, false));
        fieldBRAND.addValidator(new RegexpValidator("^[\\p{L}0-9- ]*$", "Model zawiera nie właściwe znaki"));
        fieldBRAND.setWidth("20em");
        fieldBRAND.setRequired(true);
        fieldBRAND.setRequiredError("Pole Model jest wymagane");
        fieldBRAND.setImmediate(true);
        newVehicleLayout.addComponent(fieldBRAND);
        fields.bind(fieldBRAND, fieldName);

        fieldName = VehicleView.COLOUR;
        TextField fieldCOLOUR = new TextField(fieldName);
        fieldCOLOUR.addValidator(new StringLengthValidator("Niepoprawna długość pola Kolor nadwozia", 3, 64, false));
        fieldCOLOUR.addValidator(new RegexpValidator("^[\\p{L}]*$", "pole Kolor nadwozia zawiera niewłaściwe znaki"));
        fieldCOLOUR.setWidth("20em");
        fieldCOLOUR.setRequired(true);
        fieldCOLOUR.setRequiredError("Pole Kolor nadwozia jest wymagane");
        fieldCOLOUR.setImmediate(true);
        newVehicleLayout.addComponent(fieldCOLOUR);
        fields.bind(fieldCOLOUR, fieldName);

        fieldName = VehicleView.MAX_LOAD;
        TextField fieldMAX_LOAD = new TextField(fieldName);
        fieldMAX_LOAD.setConverter(Integer.class);
        fieldMAX_LOAD.setNullSettingAllowed(true);
        fieldMAX_LOAD.setNullRepresentation("");
        fieldMAX_LOAD.addValidator(new IntegerRangeValidator("Niewłaściwa wartość ładowności", 0, Integer.MAX_VALUE));
        fieldMAX_LOAD.setWidth("20em");
        fieldMAX_LOAD.setImmediate(true);
        fieldMAX_LOAD.setRequired(true);
        fieldMAX_LOAD.setRequiredError("Pole Ładowność jest wymagane");
        newVehicleLayout.addComponent(fieldMAX_LOAD);
        fields.bind(fieldMAX_LOAD, fieldName);

        fieldName = VehicleView.REGISTRATION_NR;
        TextField fieldREGISTRATION_NR = new TextField(fieldName);
        fieldREGISTRATION_NR.addValidator(new StringLengthValidator("Niepoprawna długość pola Nr rejestracyjny", 4, 12, false));
        fieldREGISTRATION_NR.addValidator(new RegexpValidator("^[A-Z0-9-]+$", "pole Nr rejestracyjny zawiera nie właściwe znaki"));
        fieldREGISTRATION_NR.setWidth("20em");
        fieldREGISTRATION_NR.setImmediate(true);
        fieldREGISTRATION_NR.setRequired(true);
        fieldREGISTRATION_NR.setRequiredError("Pole Nr rejestracyjny jest wymagane");
        newVehicleLayout.addComponent(fieldREGISTRATION_NR);
        fields.bind(fieldREGISTRATION_NR, fieldName);

        Button addVehicle = new Button("Dodaj");
        Button cancel = new Button("Anuluj");
        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(addVehicle);
        hl.addComponent(cancel);
        newVehicleLayout.addComponent(hl);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        addVehicle.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                Vehicle veh = new Vehicle();
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
                    ReceiveVehicle.addVehicle(veh);
                    Notification.show("Dodano nowy pojazd");
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
