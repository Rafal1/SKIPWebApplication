package SKIPWebApplication;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
//    private TextField searchField = new TextField();

    public CommuniqueView() {
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
        buildTable();
        commTable.setWidth("100%");
        commTable.setFilterBarVisible(true);
        commTable.setRefreshingEnabled(true); //check this
        //TODO napisy: data poczatkwa i koncowa - decorator
        leftLayout.addComponent(commTable);

        //TODO w razie rezygnacji z gotowca
//        searchField.setInputPrompt("Wyszukaj");
//        searchField.setWidth("100%");
//        searchField.setImmediate(true);
//        leftLayout.addComponent(searchField);
//
//        leftLayout.setExpandRatio(searchField, 1);

    }

    private void buildingRightLayout() {

    }

    private void buildTable() {
        //TODO data source from WebService
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");
        try {
            String[] drivers = new String[]{"Adam Dolny", "Zygmunt Kowalski"};
            Integer[] numer_komunikatu = new Integer[]{1, 2};
            Date[] dataTime = new Date[]{sdf.parse("13.01.2014 11:32:00"), sdf.parse("14.01.2014 21:55:00")};
            String[] state = new String[]{"Jazda", "Pauza"};

            commTable.addContainerProperty("Kierowca", String.class, null);
            commTable.addContainerProperty("ID", Integer.class, null);
            commTable.addContainerProperty("Komunikat", String.class, null); //TODO improve style (visibility)
            commTable.addContainerProperty("Data nadesłania", Date.class, null);
            commTable.addContainerProperty("Status", CheckBox.class, null);

            commTable.setColumnAlignment("Komunikat", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("ID", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("Komunikat", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("Data nadesłania", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("Status", CustomTable.ALIGN_CENTER);

            for (int j = 0; j < 50; j++) {
                for (int i = 0; i < 2; i++) {
                    CheckBox isRead = new CheckBox("Przeczytana");
                    commTable.addItem(new Object[]{drivers[i], numer_komunikatu[i], state[i], dataTime[i], isRead}, 2 * j + i + 1);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
