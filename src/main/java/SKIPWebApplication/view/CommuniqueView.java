package SKIPWebApplication.view;

import SKIPWebApplication.CommuniqueFilterDecorator;
import SKIPWebApplication.CommuniqueFilterGenerator;
import SKIPWebApplication.ReadEnum;
import SKIPWebApplication.StatusEnum;
import SKIPWebApplication.receiveinformation.ReceiveCommunique;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import custommap.CustomMap;
import org.tepi.filtertable.FilterTable;
import returnobjects.Communique;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.vaadin.ui.Alignment.TOP_CENTER;

/**
 * @author Rafal Zawadzki
 */
public class CommuniqueView extends VerticalLayout implements View {
    private static String PAGE_WIDTH = "1024px";
    private final Integer SPLIT_POSITION = 52;
    private FilterTable commTable = new FilterTable();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private CustomMap customMap;
    private boolean TestRead = false;

    public CommuniqueView() {
        setSizeFull();
        initLayout();
    }

    private void initLayout() {
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
        commTable.setContainerDataSource(buildTable());
        commTable.setFilterBarVisible(true);
        commTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                customMap.addOneMarker("przyklad", new LatLon(52.324234, 25.2452342));
            }
        }

        );

        leftLayout.addComponent(commTable);
    }

    private void buildingRightLayout() {
        initMap();

        // wstawiam w tym miejscu mape - Mariusz
        //TODO usunac gdy na 100% będzie wiadaomo jak ma wyglądać widok

    }

    private void initMap() {
        customMap = new CustomMap();
        Component map = customMap.getCustomMap();
        rightLayout.addComponent(map);
        rightLayout.setExpandRatio(map, 1.0f);
    }

    private Container buildTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");
        IndexedContainer indx = new IndexedContainer();
        ArrayList<Communique> comList = ReceiveCommunique.getCommuniquesList();

        indx.addContainerProperty("Kierowca", String.class, null);
        indx.addContainerProperty("ID", Integer.class, null);
        indx.addContainerProperty("Komunikat", StatusEnum.class, null); //TODO improve style (visibility)
        indx.addContainerProperty("Data nadesłania", Date.class, null);
        commTable.setColumnAlignment("ID", CustomTable.ALIGN_CENTER);
        commTable.setColumnAlignment("Komunikat", CustomTable.ALIGN_CENTER);
        commTable.setColumnAlignment("Data nadesłania", CustomTable.ALIGN_CENTER);

        for (final Communique c : comList) {
            Object id = indx.addItem();
            indx.getContainerProperty(id, "Kierowca").setValue(c.getDriverId());
            indx.getContainerProperty(id, "ID").setValue(c.getId());
            indx.getContainerProperty(id, "Komunikat").setValue(c.getState());
            indx.getContainerProperty(id, "Data nadesłania").setValue(c.getDate());
        }

        return indx;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        DefaultViewBuilderHelper.checkLogin();
    }
}
