package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveAccount;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import returnobjects.Account;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Kamil
 * Date: 10.02.14
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public class AddAccountWindow extends Window {

    public static final String USERNAME = "Login";
    public static final String PASSWORD = "Hasło";

    public AddAccountWindow(final ManageAccountWindow parent) {
        super("Nowy użytkownik systemu");
        FormLayout newAccountLayout =  new FormLayout();
        final FieldGroup fields = new FieldGroup();
        fields.setBuffered(true);

        String fieldName = "";
        fieldName = USERNAME;
        TextField fieldUSERNAME = new TextField(fieldName);
        fieldUSERNAME.addValidator(new StringLengthValidator("Niepoprawna długość loginu", 3, 50, false));
        fieldUSERNAME.addValidator(new RegexpValidator("^[a-zA-Z-]*$", "Login zawiera nie właściwe znaki"));
        fieldUSERNAME.setWidth("20em");
        fieldUSERNAME.setImmediate(true);
        fieldUSERNAME.setRequired(true);
        fieldUSERNAME.setRequiredError("Pole login jest wymagane.");
        newAccountLayout.addComponent(fieldUSERNAME);
        fields.bind(fieldUSERNAME, fieldName);

        fieldName = PASSWORD;
        PasswordField fieldPASSWORD = new PasswordField(fieldName);
        fieldPASSWORD.addValidator(new StringLengthValidator("Niepoprawna długość hasła", 3, 64, false));
        fieldPASSWORD.setWidth("20em");
        fieldPASSWORD.setImmediate(true);
        fieldPASSWORD.setRequired(true);
        fieldPASSWORD.setRequiredError("Pole hasło jest wymagane.");
        newAccountLayout.addComponent(fieldPASSWORD);
        fields.bind(fieldPASSWORD, fieldName);

        Button addAccount = new Button("Dodaj");
        Button cancel = new Button("Anuluj");
        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(addAccount);
        hl.addComponent(cancel);
        newAccountLayout.addComponent(hl);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        addAccount.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Account account = new Account();
                account.setUsername((String) fields.getField(USERNAME).getValue());
                account.setPassword((String) fields.getField(PASSWORD).getValue());
                account.setEnabled(true);

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
                    Account result =  ReceiveAccount.addAccount(account);
                    if(result != null)
                        Notification.show("Dodano nowego użytkownika");
                    else
                        Notification.show("Wystąpił błąd podczas dodawania nowego użytkownika");

                    close();
                }
            }
        }
        );

        addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                parent.refreshDataSource();
                parent.setVisible(true);
            }
        }
        );
        parent.setVisible(false);

        setContent(newAccountLayout);

        setResizable(false);

        setDraggable(false);

        center();
    }
}
