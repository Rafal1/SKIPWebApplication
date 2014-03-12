package SKIPWebApplication.view;

import SKIPWebApplication.SkipapplicationUI;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kamil
 * Date: 01.02.14
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
public class DefaultViewBuilderHelper {

    public static String PAGE_WIDTH = "1024px";
    public static Integer DEFAULT_SPLIT_POSITION = 30;

    public static Component getDefaultMenuPanel() {
        HorizontalLayout hl = new HorizontalLayout();
        ThemeResource logo = new ThemeResource("../../resources/icons/skip.png");
        Image logoComp = new Image(null, logo);
        hl.addComponent(logoComp);
        MenuBar.Command cmd = new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                //TODO conditions for other views
                if (selectedItem.getText().equals("Wyloguj się")) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.LOGIN_VIEW);
                }
                if (selectedItem.getText().equals("Wiadomości")) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.COMMUNIQUE_VIEW);
                }
                if (selectedItem.getText().equals("Kierowcy")) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.DRIVERS_VIEW);
                }
                if (selectedItem.getText().equals("Pojazdy")) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.VEHICLE_VIEW);
                }
            }
        };
        MenuBar menu = new MenuBar();
        menu.addItem("Główna", new ThemeResource(
                "../../resources/icons/home.png"), cmd);
        menu.addItem("Kierowcy", new ThemeResource(
                "../../resources/icons/drivers.png"), cmd);
        menu.addItem("Pojazdy", new ThemeResource(
                "../../resources/icons/car.png"), cmd);
        menu.addItem("Wiadomości", new ThemeResource(
                "../../resources/icons/messages.png"), cmd);
        menu.addItem("Wyloguj się", new ThemeResource(
                "../../resources/icons/log_out.png"), cmd);
        menu.setHeight("73px");
        hl.addComponent(menu);
        hl.setComponentAlignment(logoComp, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(menu, Alignment.MIDDLE_CENTER);
        hl.setHeight("80px");
        return hl;
    }
}
