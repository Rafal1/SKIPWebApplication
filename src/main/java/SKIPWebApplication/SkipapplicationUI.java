package SKIPWebApplication;

import SKIPWebApplication.view.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
public class SkipapplicationUI extends UI {
    public static Navigator navigator;
    public static final String DRIVERS_VIEW = "drivers";
    public static final String LOGIN_VIEW = "";
    public static final String VEHICLE_VIEW = "vehicle";
    public static final String COMMUNIQUE_VIEW = "wiadomosci";
    public static final String MAIN_VIEW = "main";

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SkipapplicationUI.class, widgetset = "SKIPWebApplication.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("SKIP");

        //SSL initialization
        //System.setProperty("javax.net.ssl.trustStore", VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/VAADIN/resources/ssl/cacertsts" );
       // System.setProperty("javax.net.ssl.trustStorePassword", "18SK1P");

        //SSL initialization  -- localhost - w późniejszym etapie do usuniecia
        // odkomentuj 2 linijki pod jesli chcesz łączyć się z lokalnym serwerem
       // System.setProperty("javax.net.ssl.trustStore", VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "//VAADIN//resources//ssl//cacertsts" );
        //System.setProperty("javax.net.ssl.trustStorePassword", "18SK1P");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        //TODO provider may be required :/?
        // Create and register the views
        navigator.addView("", new LoginView()); //default view

    }
}