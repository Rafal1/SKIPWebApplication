package SKIPWebApplication.view;

import SKIPWebApplication.CommuniqueFilterDecorator;
import SKIPWebApplication.CommuniqueFilterGenerator;
import SKIPWebApplication.WaitRefreshThread;
import SKIPWebApplication.receiveinformation.ReceiveCommunique;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import custommap.CustomMap;
import org.tepi.filtertable.FilterTable;
import returnobjects.Communique;
import returnobjects.Coordinates;

import java.util.ArrayList;
import java.util.Date;

import static com.vaadin.ui.Alignment.TOP_CENTER;

/**
 * @author Rafal Zawadzki
 */
public class CommuniqueView extends VerticalLayout implements View {

    private static final String DRIVER_TAG = "Kierowca";
    private static final String ID_TAG = "ID";
    private static final String COMMUNIQUE_TYPE_TAG = "Komunikat";
    private static final String DATE_TAG = "Data nadesłania";
    private static final String COORDINATES_TAG = "koordynaty";

    private static final String PAGE_WIDTH = "1024px";
    private final static Integer SPLIT_POSITION = 52;
    private FilterTable commTable = new FilterTable();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private CustomMap customMap;

    public CommuniqueView() {
        setSizeFull();
        initLayout();

    }

    private void initLayout() {
        final Refresher refresher = new Refresher();
        refresher.addListener(new TimeListener());
        addExtension(refresher);
        Component navigationBar = DefaultViewBuilderHelper.getDefaultMenuPanel();
        Component bodyContent = getBodyContent();

        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.setSizeFull();
        mainPanel.setWidth(PAGE_WIDTH);
        mainPanel.addComponent(navigationBar);
        mainPanel.setComponentAlignment(navigationBar, TOP_CENTER);
        mainPanel.addComponent(bodyContent);
        mainPanel.setExpandRatio(bodyContent, 1.0f);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(mainPanel);
        layout.setComponentAlignment(mainPanel, TOP_CENTER);
        layout.setSizeFull();
        addComponent(layout);
    }

    private Component getBodyContent() {
        VerticalSplitPanel splitPanel = new VerticalSplitPanel();

        splitPanel.setSplitPosition(SPLIT_POSITION);
        splitPanel.setLocked(true);

        buildingLeftLayout();
        splitPanel.addComponent(leftLayout);

        buildingRightLayout();

        splitPanel.addComponent(rightLayout);


        return splitPanel;
    }

    private void buildingLeftLayout() {
        leftLayout.setSizeFull();

//    private Button resFilters = new Button("Usuń Filtry");
//        resFilters.addClickListener(new Button.ClickListener() {  TODO po dodaniu przycisku pojawia sie duża wolna przestrzeń
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                commTable.resetFilters();
//            }
//        });
//        leftLayout.addComponent(resFilters);

        commTable.setWidth("100%");
        commTable.setRefreshingEnabled(true);
        commTable.setImmediate(true);
        commTable.setSortEnabled(true);
        commTable.setFilterGenerator(new CommuniqueFilterGenerator());
        commTable.setFilterDecorator(new CommuniqueFilterDecorator());
        commTable.setSelectable(true);

        commTable.setFilterBarVisible(true);
        commTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                customMap.clearMarkers();
                String driverName = event.getItem().getItemProperty(DRIVER_TAG).getValue().toString();
                String communiqueTypeString = event.getItem().getItemProperty(COMMUNIQUE_TYPE_TAG).getValue().toString();
                Coordinates coords = (Coordinates) event.getItem().getItemProperty(COORDINATES_TAG).getValue();
                customMap.addOneMarker(driverName + " " + communiqueTypeString,
                        new LatLon(coords.getLatitude(), coords.getLongitude()));
            }
        }

        );

        leftLayout.addComponent(commTable);
    }

    private void buildingRightLayout() {
        rightLayout.setSizeFull();
        initMap();
    }

    private void initMap() {
        customMap = new CustomMap();
        customMap.setDefaultMarkerImage("VAADIN/resources/icons/messages.png");
        VerticalLayout map = customMap.getCustomMap();
        rightLayout.addComponent(map);
        rightLayout.setExpandRatio(map, 1.0f);
    }

    private Container buildTable() {
        IndexedContainer indx = new IndexedContainer();
        ArrayList<Communique> comList = ReceiveCommunique.getCommuniquesList();

        //TODO Pozwoliłem sobie pozmieniać tutaj rodzaje klas tak żeby to jakotako działało, pozdrawiam Mariusz

        indx.addContainerProperty(DRIVER_TAG, Long.class, null);
        indx.addContainerProperty(ID_TAG, Long.class, null);
        indx.addContainerProperty(COMMUNIQUE_TYPE_TAG, Integer.class, null); //TODO improve style (visibility)
        indx.addContainerProperty(DATE_TAG, Date.class, null);
        indx.addContainerProperty(COORDINATES_TAG, Coordinates.class, null);

        commTable.setColumnAlignment(ID_TAG, CustomTable.ALIGN_CENTER);
        commTable.setColumnAlignment(COMMUNIQUE_TYPE_TAG, CustomTable.ALIGN_CENTER);
        commTable.setColumnAlignment(DATE_TAG, CustomTable.ALIGN_CENTER);

        for (final Communique c : comList) {
            Object id = indx.addItem();
            indx.getContainerProperty(id, DRIVER_TAG).setValue(c.getDriverId());
            indx.getContainerProperty(id, ID_TAG).setValue(c.getId());
            indx.getContainerProperty(id, COMMUNIQUE_TYPE_TAG).setValue(c.getState());
            indx.getContainerProperty(id, DATE_TAG).setValue(c.getDate());
            indx.getContainerProperty(id, COORDINATES_TAG).setValue(c.getcoordinates());
        }

        new WaitRefreshThread().start();

        return indx;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        DefaultViewBuilderHelper.checkLogin();
        commTable.setContainerDataSource(buildTable());
        commTable.setVisibleColumns(new Object[]{DRIVER_TAG, ID_TAG, COMMUNIQUE_TYPE_TAG, DATE_TAG});
    }

    public class TimeListener implements Refresher.RefreshListener {
        @Override
        public void refresh(final Refresher source) {
            commTable.setContainerDataSource(buildTable());
            commTable.setVisibleColumns(new Object[]{DRIVER_TAG, ID_TAG, COMMUNIQUE_TYPE_TAG, DATE_TAG});
        }
    }
}
