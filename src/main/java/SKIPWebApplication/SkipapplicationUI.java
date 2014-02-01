package SKIPWebApplication;


import SKIPWebApplication.view.CommuniqueView;
import SKIPWebApplication.view.DriversView;
import SKIPWebApplication.view.LoginView;
import SKIPWebApplication.view.VehicleView;
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
    public static final String COMMUNIQUE_VIEW = "wiadomosci";
    public static final String VEHICLE_VIEW = "pojazdy";

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SkipapplicationUI.class, widgetset = "SKIPWebApplication.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Navigation Example");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        //TODO provider may be required :/?
        // Create and register the views
        navigator.addView("", new LoginView()); //default view
        navigator.addView(DRIVERS_VIEW, new DriversView());
        navigator.addView(COMMUNIQUE_VIEW, new CommuniqueView());
        navigator.addView(VEHICLE_VIEW, new VehicleView());
    }
}