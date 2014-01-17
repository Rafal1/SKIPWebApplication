package SKIPWebApplication;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * @author Rafal Zawadzki
 */
public class CommuniqueView extends VerticalLayout implements View {
    private static String PAGE_WIDTH = "1024px";
    private final Integer SPLIT_POSITION = 52;
    private Table commTable = new Table();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private TextField searchField = new TextField();

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
        mainPanel.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
        mainPanel.addComponent(bodyContent);
        mainPanel.setExpandRatio(bodyContent, 1.0f);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(mainPanel);
        layout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
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
        leftLayout.addComponent(commTable);

        searchField.setInputPrompt("Wyszukaj");
        searchField.setWidth("100%");
        searchField.setImmediate(true);
        leftLayout.addComponent(searchField); //TODO podpiąć dodatek z filtrowaniem (przkompilować WidgetSet)

        leftLayout.setExpandRatio(searchField, 1);

    }

    private void buildingRightLayout(){

    }

    private void buildTable() {
        //TODO data source from WebService
        String[] drivers = new String[]{"Adam Dolny", "Zygmunt Kowalski"};
        Integer[] numer_komunikatu = new Integer[]{1, 2};
        String[] dataTime = new String[]{"13.01.2014 11:32", "14.01.2014 21:55"};
        String[] state = new String[]{"Jazda", "Pauza"};

        commTable.addContainerProperty("Kierowca", String.class, null);
        commTable.addContainerProperty("ID", Integer.class, null);
        commTable.addContainerProperty("Komunikat", String.class, null); //TODO improve style (visibility)
        commTable.addContainerProperty("Data nadesłania", String.class, null);
        commTable.addContainerProperty("Status", CheckBox.class, null);

        for (int j = 0; j < 50; j++) {
            for (int i = 0; i < 2; i++) {
                CheckBox isRead = new CheckBox("Przeczytana");
                commTable.addItem(new Object[]{drivers[i], numer_komunikatu[i], state[i], dataTime[i], isRead}, 2 * j + i + 1);
            }
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
