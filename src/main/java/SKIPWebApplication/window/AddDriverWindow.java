package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveAccountUser;
import SKIPWebApplication.receiveinformation.ReceiveDriver;
import SKIPWebApplication.view.DriversView;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import returnobjects.Account;
import returnobjects.Driver;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Kamil
 * Date: 10.02.14
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public class AddDriverWindow extends Window {

    public AddDriverWindow(final DriversView parent) {
        super("Nowy kierowca");
        FormLayout newDriverLayout = new FormLayout();
        final FieldGroup fields = new FieldGroup();
        fields.setBuffered(true);

        String fieldName = "";
        fieldName = AddAccountWindow.USERNAME;
        TextField fieldUSERNAME = new TextField(fieldName);
        fieldUSERNAME.addValidator(new StringLengthValidator("Niepoprawna długość loginu", 3, 50, false));
        fieldUSERNAME.addValidator(new RegexpValidator("^[a-zA-Z-]*$", "Login zawiera nie właściwe znaki"));
        fieldUSERNAME.setWidth("20em");
        fieldUSERNAME.setImmediate(true);
        fieldUSERNAME.setRequired(true);
        fieldUSERNAME.setRequiredError("Pole login jest wymagane.");
        newDriverLayout.addComponent(fieldUSERNAME);
        fields.bind(fieldUSERNAME, fieldName);

        fieldName = AddAccountWindow.PASSWORD;
        PasswordField fieldPASSWORD = new PasswordField(fieldName);
        fieldPASSWORD.addValidator(new StringLengthValidator("Niepoprawna długość hasła", 3, 64, false));
        fieldPASSWORD.setWidth("20em");
        fieldPASSWORD.setImmediate(true);
        fieldPASSWORD.setRequired(true);
        fieldPASSWORD.setRequiredError("Pole hasło jest wymagane.");
        newDriverLayout.addComponent(fieldPASSWORD);
        fields.bind(fieldPASSWORD, fieldName);

        fieldName = DriversView.FNAME;
        TextField fieldFNAME = new TextField(fieldName);
        fieldFNAME.addValidator(new StringLengthValidator("Niepoprawna długość imienia", 3, 64, false));
        fieldFNAME.addValidator(new RegexpValidator("^[a-zA-Z-]*$", "Imie zawiera nie właściwe znaki"));
        fieldFNAME.setWidth("20em");
        fieldFNAME.setImmediate(true);
        fieldFNAME.setRequired(true);
        fieldFNAME.setRequiredError("Pole Imię jest wymagane.");
        newDriverLayout.addComponent(fieldFNAME);
        fields.bind(fieldFNAME, fieldName);

        fieldName = DriversView.LNAME;
        TextField fieldLNAME = new TextField(fieldName);
        fieldLNAME.addValidator(new StringLengthValidator("Niepoprawna długość nazwiska", 3, 64, false));
        fieldLNAME.addValidator(new RegexpValidator("^[a-zA-Z-]*$", "Nazwisko zawiera nie właściwe znaki."));
        fieldLNAME.setWidth("20em");
        fieldLNAME.setImmediate(true);
        fieldLNAME.setRequired(true);
        fieldLNAME.setRequiredError("Pole Nazwisko jest wymagane.");
        newDriverLayout.addComponent(fieldLNAME);
        fields.bind(fieldLNAME, fieldName);

        fieldName = DriversView.COMPANY_PHONE;
        TextField fieldCOMPANY_PHONE = new TextField(fieldName);
        fieldCOMPANY_PHONE.addValidator(new RegexpValidator("\\d{3,12}", "Telefon firmowy zawiera nie właściwe znaki."));
        fieldCOMPANY_PHONE.setWidth("20em");
        fieldCOMPANY_PHONE.setImmediate(true);
        fieldCOMPANY_PHONE.setRequired(true);
        fieldCOMPANY_PHONE.setRequiredError("Pole Tel. firmowy wymagane.");
        newDriverLayout.addComponent(fieldCOMPANY_PHONE);
        fields.bind(fieldCOMPANY_PHONE, fieldName);

        fieldName = DriversView.PRIVATE_PHONE;
        TextField fieldPRIVATE_PHONE = new TextField(fieldName);
        fieldPRIVATE_PHONE.addValidator(new RegexpValidator("\\d{3,12}", "Telefon prywatny zawiera nie właściwe znaki."));
        fieldPRIVATE_PHONE.setWidth("20em");
        fieldPRIVATE_PHONE.setImmediate(true);
        newDriverLayout.addComponent(fieldPRIVATE_PHONE);
        fields.bind(fieldPRIVATE_PHONE, fieldName);

        fieldName = DriversView.E_MAIL;
        TextField fieldE_MAIL = new TextField(fieldName);
        fieldE_MAIL.addValidator(new EmailValidator("Niepoprawny adres email."));
        fieldE_MAIL.setWidth("20em");
        fieldE_MAIL.setImmediate(true);
        fieldE_MAIL.setRequired(true);
        fieldE_MAIL.setRequiredError("Pole E-mail jest wymagane.");
        newDriverLayout.addComponent(fieldE_MAIL);
        fields.bind(fieldE_MAIL, fieldName);

        Button addDriver = new Button("Dodaj");
        Button cancel = new Button("Anuluj");
        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(addDriver);
        hl.addComponent(cancel);
        newDriverLayout.addComponent(hl);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        addDriver.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Driver driver = new Driver();
                driver.setFirstName((String) fields.getField(DriversView.FNAME).getValue());
                driver.setLastName((String) fields.getField(DriversView.LNAME).getValue());
                driver.setPhoneNumber((String) fields.getField(DriversView.COMPANY_PHONE).getValue());
                driver.setPhoneNumber2((String) fields.getField(DriversView.PRIVATE_PHONE).getValue());
                driver.setEmail((String) fields.getField(DriversView.E_MAIL).getValue());

                Account usr = new Account();
                usr.setEnabled(true);
                usr.setUsername((String) fields.getField(AddAccountWindow.USERNAME).getValue());
                usr.setPassword((String) fields.getField(AddAccountWindow.PASSWORD).getValue());

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
                    Account userAcc = ReceiveAccountUser.addAccount(usr);
                    Driver driverToChange = ReceiveDriver.getDriver(userAcc.getEntity());
                    driver.setId(userAcc.getEntity());
                    driver.setCoordinatesUpdateDate(driverToChange.getCoordinatesUpdateDate());
                    driver.setLatestCoordinates(driverToChange.getLatestCoordinates());
                    Driver Dchanged = ReceiveDriver.changeDriver(driver);
                    if(Dchanged == null){
                        String warning = "Dodanie kierowcy nie powiodło się";
                        Notification.show(warning);
                        System.out.println(warning);
                        return;
                    }
                    Notification.show("Dodano nowego kierowcę");
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
}
