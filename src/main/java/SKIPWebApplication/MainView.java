package SKIPWebApplication;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import custommap.CustomMap;

import static com.vaadin.ui.Alignment.TOP_CENTER;

/**
 * User: Mariusz
 * Date: 09.03.14
 * Time: 19:34
 * Desc: Klasa bedąca odwzorowaniem widoku głownego w aplikacji
 */
public class MainView extends VerticalLayout implements View {

    private static String PAGE_WIDTH = "1024px";
    private final Integer SPLIT_POSITION = 30;

    private GoogleMap googleMap;
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();

    public MainView() {
        setSizeFull();
        initLayout();
    }

    private void initLayout() {
        Component navigationBar = DriversView.createMenuPanel();
        Component bodyContent = getBodyContent();

        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.setSizeFull();
        mainPanel.setWidth(PAGE_WIDTH);
        mainPanel.addComponent(navigationBar);
        mainPanel.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
        mainPanel.addComponent(bodyContent);
        mainPanel.setExpandRatio(bodyContent, 1.0f);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(mainPanel);
        layout.setComponentAlignment(mainPanel, TOP_CENTER);
        layout.setSizeFull();
        addComponent(layout);


    }

    private Component getBodyContent() {
        CustomMap customMap = new CustomMap();
        return customMap.getCustomMap();
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
