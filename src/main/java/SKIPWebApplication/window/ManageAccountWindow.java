package SKIPWebApplication.window;

import SKIPWebApplication.receiveinformation.ReceiveAccount;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import returnobjects.Account;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kamil
 * Date: 02.06.14
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 */
public class ManageAccountWindow extends Window {

    private static final String LOGIN = "Login";
    private static final String PASSWORD = "Hasło";

    private Table accountsList = new Table();

    private IndexedContainer accountsContainer;

    private VerticalLayout layout ;

    public ManageAccountWindow(final Component parent) {
        super("Zarządzanie użytkownikami systemu");

        layout = new VerticalLayout();

        refreshDataSource();

        layout.addComponent(accountsList);
        layout.setExpandRatio(accountsList, 1);
        accountsList.setSizeFull();

        Button deleteAccount = new Button("Usuń zaznaczone konto");
        Button addAccount = new Button("Dodaj nowe konto");
        Button close = new Button("Zamknij");
        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(deleteAccount);
        hl.addComponent(addAccount);
        hl.addComponent(close);
        layout.addComponent(hl);

        deleteAccount.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object currentAccount = accountsList.getValue();
                String result = ReceiveAccount.deleteAccount((String) accountsList
                        .getContainerProperty(currentAccount, LOGIN)
                        .getValue());
                if(result != null)
                    Notification.show("Konto zostało usunięte");
                else
                    Notification.show("Wystąpił błąd podczas usuwania konta");
                refreshDataSource();
            }
        });

        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        addAccount.addClickListener(new AddAccountClickListener(this));

        addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                parent.setEnabled(true);
            }
        }
        );
        parent.setEnabled(false);

        setContent(layout);

        setResizable(false);

        setDraggable(false);

        center();
    }

    public void refreshDataSource() {
        accountsContainer = prepareForAccountsList(ReceiveAccount.prepareForAccountsList());
        accountsList.setContainerDataSource(accountsContainer);
        accountsList.setColumnReorderingAllowed(false);
        accountsList.setVisibleColumns(new Object[]{LOGIN});
        accountsList.setSelectable(true);
        accountsList.setImmediate(true);
    }

    private IndexedContainer prepareForAccountsList(List<Account> accounts) {
        IndexedContainer ic = new IndexedContainer();

        ic.addContainerProperty(LOGIN, String.class, "");
        ic.addContainerProperty(PASSWORD, String.class, "");

        for (Account account : accounts) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, LOGIN).setValue(account.getUsername());
            ic.getContainerProperty(id, PASSWORD).setValue(account.getPassword());
        }

        return ic;
    }

    private class AddAccountClickListener implements Button.ClickListener {
        ManageAccountWindow parent;

        public AddAccountClickListener(ManageAccountWindow parent) {
            this.parent = parent;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            AddAccountWindow window = new AddAccountWindow(parent);
            getUI().addWindow(window);
        }
    }
}
