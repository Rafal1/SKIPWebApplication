package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveDriver;
import SKIPWebApplication.view.DriversView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import returnobjects.Driver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Rafal Zawadzki
 */
public class EditDriverWindow extends Window {

    public EditDriverWindow(final DriversView parent, final long driverId, final ArrayList<Driver> drListArray) {
        super("Edycja kierowcy");
        FormLayout newDriverLayout = new FormLayout();
        final FieldGroup fields = new FieldGroup();
        Table driversList = parent.getDriversList();
        fields.setBuffered(true);

        String fieldName = "";
        fieldName = DriversView.FNAME;
        TextField fieldFNAME = new TextField(fieldName);
        fieldFNAME.setValue((String) driversList.getContainerProperty(driversList.getValue(), DriversView.FNAME).getValue());
        fieldFNAME.addValidator(new StringLengthValidator("Niepoprawna długość imienia", 3, 64, false));
        fieldFNAME.addValidator(new RegexpValidator("^[a-zA-Z-łóńźżćśęą]*$", "Imię zawiera niewłaściwe znaki"));
        fieldFNAME.setWidth("20em");
        fieldFNAME.setImmediate(true);
        newDriverLayout.addComponent(fieldFNAME);
        fields.bind(fieldFNAME, fieldName);

        fieldName = DriversView.LNAME;
        TextField fieldLNAME = new TextField(fieldName);
        fieldLNAME.setValue((String) driversList.getContainerProperty(driversList.getValue(), DriversView.LNAME).getValue());
        fieldLNAME.addValidator(new StringLengthValidator("Niepoprawna długość nazwiska", 3, 64, false));
        fieldLNAME.addValidator(new RegexpValidator("^[a-zA-Z-łóńźżćśęą]*$", "Nazwisko zawiera niewłaściwe znaki."));
        fieldLNAME.setWidth("20em");
        fieldLNAME.setImmediate(true);
        newDriverLayout.addComponent(fieldLNAME);
        fields.bind(fieldLNAME, fieldName);

        fieldName = DriversView.COMPANY_PHONE;
        TextField fieldCOMPANY_PHONE = new TextField(fieldName);
        fieldCOMPANY_PHONE.setValue((String) driversList.getContainerProperty(driversList.getValue(), DriversView.COMPANY_PHONE).getValue());
        fieldCOMPANY_PHONE.addValidator(new RegexpValidator("\\d{3,12}", "Telefon firmowy zawiera niewłaściwe znaki."));
        fieldCOMPANY_PHONE.setWidth("20em");
        fieldCOMPANY_PHONE.setImmediate(true);
        newDriverLayout.addComponent(fieldCOMPANY_PHONE);
        fields.bind(fieldCOMPANY_PHONE, fieldName);

        fieldName = DriversView.PRIVATE_PHONE;
        TextField fieldPRIVATE_PHONE = new TextField(fieldName);
        fieldPRIVATE_PHONE.setValue((String) driversList.getContainerProperty(driversList.getValue(), DriversView.PRIVATE_PHONE).getValue());
        fieldPRIVATE_PHONE.addValidator(new RegexpValidator("\\d{3,12}", "Telefon prywatny zawiera niewłaściwe znaki."));
        fieldPRIVATE_PHONE.setWidth("20em");
        fieldPRIVATE_PHONE.setImmediate(true);
        newDriverLayout.addComponent(fieldPRIVATE_PHONE);
        fields.bind(fieldPRIVATE_PHONE, fieldName);

        fieldName = DriversView.E_MAIL;
        TextField fieldE_MAIL = new TextField(fieldName);
        fieldE_MAIL.setValue((String) driversList.getContainerProperty(driversList.getValue(), DriversView.E_MAIL).getValue());
        fieldE_MAIL.addValidator(new EmailValidator("Niepoprawny adres email."));
        fieldE_MAIL.setWidth("20em");
        fieldE_MAIL.setImmediate(true);
        newDriverLayout.addComponent(fieldE_MAIL);
        fields.bind(fieldE_MAIL, fieldName);

        Button changeDriver = new Button("Zatwierdź");
        Button cancel = new Button("Anuluj");
        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(changeDriver);
        hl.addComponent(cancel);
        newDriverLayout.addComponent(hl);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        changeDriver.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                Driver driver = new Driver();
                driver.setId(driverId);
                driver.setFirstName((String) fields.getField(DriversView.FNAME).getValue());
                driver.setLastName((String) fields.getField(DriversView.LNAME).getValue());
                driver.setPhoneNumber((String) fields.getField(DriversView.COMPANY_PHONE).getValue());
                driver.setPhoneNumber2((String) fields.getField(DriversView.PRIVATE_PHONE).getValue());
                driver.setEmail((String) fields.getField(DriversView.E_MAIL).getValue());

                Driver tmp = searchDrID(driverId, drListArray);
                driver.setCoordinatesUpdateDate(tmp.getCoordinatesUpdateDate());
                driver.setLatestCoordinates(tmp.getLatestCoordinates());

                Boolean valOk = true;
                Collection colFields = fields.getFields();
                for (Object o : colFields) {
                    Field fi = (Field) o;
                    try {
                        fi.validate();
                    } catch (Validator.InvalidValueException e) {
                        Notification.show("Proszę wypełnić pola poprawnie");
                        valOk = false;
                    }
                }

                if (valOk) {
                    Long idVeh = ReceiveDriver.getAssignedVehicle(driverId).getId();
//                    Boolean controlAssignment = true;
                    Boolean assignVeh = false;
//                    if (idVeh == null) {
//                        controlAssignment = false;
//                    }
                    driver = ReceiveDriver.changeDriver(driver);
//                    if (controlAssignment) {
                    assignVeh = ReceiveDriver.assignVehicle(driverId, idVeh);
                    if (!assignVeh) {
                        Notification.show("Nie wprowadzono zmian");
                        return;
                    }
//                }

                    if (driver == null) {
                        Notification.show("Nie wprowadzono zmian");
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
        }

        );

        parent.setEnabled(false);

        setContent(newDriverLayout);

        setResizable(false);

        setDraggable(false);

        center();

    }

    private Driver searchDrID(long id, ArrayList<Driver> li) {
        for (int i = 0; i < li.size(); i++) {
            Driver tmp = li.get(i);
            if (tmp.getId() == id) {
                return tmp;
            }
        }
        return null;
    }
}
