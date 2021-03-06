package SKIPWebApplication.view;

import SKIPWebApplication.SkipapplicationUI;
import SKIPWebApplication.consts.StringConsts;
import SKIPWebApplication.receiveinformation.HttpClientHelper;
import SKIPWebApplication.receiveinformation.LoginService;
import SKIPWebApplication.window.ManageAccountWindow;
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
                        UI.getCurrent().getPage().setLocation( "/" );
                    else
                        Notification.show("Nie udało sie wylogować, spróbuj jeszcze raz");
                }
                else if (selectedItem.getText().equals(StringConsts.COMMUNIQUE_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.COMMUNIQUE_VIEW);
                }
                else if (selectedItem.getText().equals(StringConsts.DRIVERS_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.DRIVERS_VIEW);
                }
                else if (selectedItem.getText().equals(StringConsts.VEHICLE_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.VEHICLE_VIEW);
                }
                else if (selectedItem.getText().equals(StringConsts.MAIN_VIEW_NAME)) {
                    UI.getCurrent().getNavigator().navigateTo(SkipapplicationUI.MAIN_VIEW);
                }
                else if (selectedItem.getText().equals(StringConsts.MANAGE_ACCOUNTS) &&
                        VaadinSession.getCurrent().getAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG)
                                .equals(StringConsts.ROLE_MASTER)
                        ) {
                    UI.getCurrent().addWindow(new ManageAccountWindow(UI.getCurrent().getContent()));
                }
            }
        };
        MenuBar menu = new MenuBar();
        menu.setHtmlContentAllowed(true);
        if(VaadinSession.getCurrent() == null) {
            menu.addItem("", new ThemeResource(
                    "../../resources/icons/gap_small.png"), null);
        }   else {
            menu.addItem("Użytkownik:<br/><b>"
                    + VaadinSession.getCurrent().getAttribute(HttpClientHelper.USER_LOGIN) + "</b>"
            , null, null).setCheckable(false);
        }
        menu.addItem("", new ThemeResource(
                "../../resources/icons/logo_skip_small.png"), null);
        if(!(VaadinSession.getCurrent().getAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG) != null
                && VaadinSession.getCurrent().getAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG)
                .equals(StringConsts.ROLE_MASTER))) {
            menu.addItem("", new ThemeResource(
                    "../../resources/icons/gap.png"), null);
            menu.addItem("", new ThemeResource(
                    "../../resources/icons/gap_small.png"), null);
        }
        menu.addItem(StringConsts.MAIN_VIEW_NAME, new ThemeResource(
                "../../resources/icons/home.png"), cmd);
        menu.addItem(StringConsts.DRIVERS_VIEW_NAME, new ThemeResource(
                "../../resources/icons/drivers.png"), cmd);
        menu.addItem(StringConsts.VEHICLE_VIEW_NAME, new ThemeResource(
                "../../resources/icons/car.png"), cmd);
        menu.addItem(StringConsts.COMMUNIQUE_VIEW_NAME, new ThemeResource(
                "../../resources/icons/messages.png"), cmd);
        if(VaadinSession.getCurrent().getAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG) != null
                && VaadinSession.getCurrent().getAttribute(HttpClientHelper.USER_ROLE_SESSION_TAG)
                .equals(StringConsts.ROLE_MASTER)) {
            menu.addItem(StringConsts.MANAGE_ACCOUNTS, new ThemeResource(
                    "../../resources/icons/manage_users.png"), cmd);
        }
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
