package SKIPWebApplication.view;

import SKIPWebApplication.SkipapplicationUI;
import SKIPWebApplication.consts.StringConsts;
import SKIPWebApplication.receiveinformation.LoginService;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
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
        MenuBar.Command cmd = new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                //TODO conditions for other views
                if (selectedItem.getText().equals(StringConsts.LOGOUT_STRING)) {
                    if(LoginService.logout())
                        UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.LOGIN_VIEW);
                    else
                        Notification.show("Nie udało sie wylogować, spróbuj jeszcze raz");
                }
                if (selectedItem.getText().equals(StringConsts.COMMUNIQUE_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.COMMUNIQUE_VIEW);
                }
                if (selectedItem.getText().equals(StringConsts.DRIVERS_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.DRIVERS_VIEW);
                }
                if (selectedItem.getText().equals(StringConsts.VEHICLE_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.VEHICLE_VIEW);
                }
                if (selectedItem.getText().equals(StringConsts.MAIN_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.MAIN_VIEW);
                }
            }
        };
        MenuBar menu = new MenuBar();
        menu.addItem("", new ThemeResource(
                "../../resources/icons/skip.jpg"), null);
        menu.addItem(StringConsts.MAIN_VIEW_NAME, new ThemeResource(
                "../../resources/icons/home.png"), cmd);
        menu.addItem(StringConsts.DRIVERS_VIEW_NAME, new ThemeResource(
                "../../resources/icons/drivers.png"), cmd);
        menu.addItem(StringConsts.VEHICLE_VIEW_NAME, new ThemeResource(
                "../../resources/icons/car.png"), cmd);
        menu.addItem(StringConsts.COMMUNIQUE_VIEW_NAME, new ThemeResource(
                "../../resources/icons/messages.png"), cmd);
        menu.addItem(StringConsts.LOGOUT_STRING, new ThemeResource(
                "../../resources/icons/log_out.png"), cmd);
        menu.setHeight("73px");
        menu.setWidth(PAGE_WIDTH);
        hl.addComponent(menu);
        hl.setComponentAlignment(menu, Alignment.MIDDLE_CENTER);
        hl.setHeight("80px");
        return hl;
    }

    public static boolean checkLogin(){
        Object isLoged =VaadinSession.getCurrent().getAttribute("login");
        if(isLoged != null && ((Boolean)isLoged) == true ){
            return true;
        }else{
            UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.LOGIN_VIEW);
        }
        return false;
    }
}
