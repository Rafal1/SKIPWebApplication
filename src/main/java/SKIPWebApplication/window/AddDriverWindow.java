package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveDriver;
import SKIPWebApplication.view.DriversView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import returnobjects.Driver;

/**
 * Created with IntelliJ IDEA.
 * User: Kamil
 * Date: 10.02.14
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public class AddDriverWindow extends Window {
    DriversView parent;
    private static final String[] editFields = new String[]{DriversView.FNAME, DriversView.LNAME,
            DriversView.COMPANY_PHONE, DriversView.PRIVATE_PHONE, DriversView.E_MAIL };

    public AddDriverWindow(final DriversView parent) {
        super("Nowy kierowca");
        FormLayout newDriverLayout = new FormLayout();
        final FieldGroup fields = new FieldGroup();
        for(String fieldName : editFields) {
            TextField field = new TextField(fieldName);
            field.setWidth("20em");
            newDriverLayout.addComponent(field);
            fields.bind(field, fieldName);
        }

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
                boolean fieldsFilled = true;
                for (Field<?> field : fields.getFields()) {
                    if (field.getValue() == null || ((String) field.getValue()).isEmpty()) {
                        fieldsFilled = false;
                        break;
                    }
                }
                if (!fieldsFilled)
                    Notification.show("Proszę wypełnić wszystkie pola");
                else {
                    Driver driver = new Driver();
                    driver.setFirstName((String) fields.getField(DriversView.FNAME).getValue());
                    driver.setLastName((String) fields.getField(DriversView.LNAME).getValue());
                    driver.setPhoneNumber((String) fields.getField(DriversView.COMPANY_PHONE).getValue());
                    driver.setPhoneNumber2((String) fields.getField(DriversView.PRIVATE_PHONE).getValue());
                    driver.setEmail((String) fields.getField(DriversView.E_MAIL).getValue());
                    ReceiveDriver.addDriver(driver);
                    Notification.show("Dodano nowego kierowcę");
                    parent.refreshDataSource();
                    close();
                }
            }
        });
        addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                parent.setEnabled(true);
            }
        });
        parent.setEnabled(false);
        setContent(newDriverLayout);
        setResizable(false);
        setDraggable(false);
        center();
    }
}
