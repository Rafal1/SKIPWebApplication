package SKIPWebApplication.view;

import SKIPWebApplication.SkipapplicationUI;
import SKIPWebApplication.receiveinformation.LoginErrorException;
import SKIPWebApplication.receiveinformation.LoginService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;

/**
 * @author Rafal Zawadzki
 */
public class LoginView extends VerticalLayout implements View {
    private TextField LOGIN = new TextField("Login");
    private PasswordField PASSWORD = new PasswordField("Hasło");

    public LoginView() {
        setSizeFull();
        initLoginView();
    }

    public void initLoginView() {
        Panel panel = new Panel();
        panel.setSizeUndefined();
        addComponent(panel);
        ThemeResource logo = new ThemeResource("../../resources/icons/skip.jpg");
        Image logoComp = new Image(null, logo);
        FormLayout content = new FormLayout();
        content.addComponent(logoComp);
        content.setComponentAlignment(logoComp, Alignment.MIDDLE_CENTER);
        content.addComponent(LOGIN);
        content.addComponent(PASSWORD);
        content.setSizeUndefined();
        content.setMargin(true);
        HorizontalLayout serviceButtons = new HorizontalLayout();
        serviceButtons.setSpacing(true);

        Button buttonLogIn = new Button("Zaloguj",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            boolean isLogged = LoginService.login(LOGIN.getValue(), PASSWORD.getValue());
                            if(isLogged) {
                                UI.getCurrent().getNavigator().addView(SkipapplicationUI.DRIVERS_VIEW, new DriversView());
                                UI.getCurrent().getNavigator().addView(SkipapplicationUI.VEHICLE_VIEW, new VehicleView());
                                UI.getCurrent().getNavigator().addView(SkipapplicationUI.COMMUNIQUE_VIEW, new CommuniqueView());
                                UI.getCurrent().getNavigator().addView(SkipapplicationUI.MAIN_VIEW, new MainView());
                                VaadinSession.getCurrent().setAttribute("login", true);
                                UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.MAIN_VIEW);
                            }

                            LOGIN.setValue(""); //inaczej po wylogowaniu zostana pokazane wpisane dane z ostatniego logowania
                            PASSWORD.setValue("");

                        } catch (LoginErrorException e) {
                            Notification.show(e.getMessage());
                        }
                    }

                });
        buttonLogIn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        serviceButtons.addComponent(buttonLogIn);
        serviceButtons.setComponentAlignment(buttonLogIn, Alignment.MIDDLE_CENTER);

        content.addComponent(serviceButtons);

        panel.setContent(content);
        setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
