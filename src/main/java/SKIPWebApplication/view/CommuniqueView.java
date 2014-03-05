package SKIPWebApplication.view;

import SKIPWebApplication.CommuniqueFilterDecorator;
import SKIPWebApplication.CommuniqueFilterGenerator;
import SKIPWebApplication.ReadEnum;
import SKIPWebApplication.StatusEnum;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
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
        commTable.setImmediate(true);
        commTable.setFilterGenerator(new CommuniqueFilterGenerator());
        commTable.setFilterDecorator(new CommuniqueFilterDecorator());
        commTable.setContainerDataSource(buildTable());
        commTable.removeGeneratedColumn("Status");
        commTable.addGeneratedColumn("Status", new CustomTable.ColumnGenerator() {
            @Override
            public Object generateCell(CustomTable components, Object o, Object o2) {
                Object TableCheckInstance = components.getContainerProperty(o, "").getValue();
                Boolean valueOfInstance = null;
                if (TableCheckInstance instanceof CheckBox) {
                    valueOfInstance = ((CheckBox) TableCheckInstance).getValue();
                }

                String finalKnow = "";
                if (valueOfInstance) {
                    finalKnow = "Przeczytana";
                } else {
                    finalKnow = "Nieprzeczytana";
                }
                return finalKnow;
            }
        });
        commTable.setFilterBarVisible(true);
        commTable.setFilterFieldVisible("Status", false);

        leftLayout.addComponent(commTable);
    }

    private void buildingRightLayout() {
        //TODO usunac gdy na 100% będzie wiadaomo jak ma wyglądać widok
    }

    private Container buildTable() {
        //TODO data source from WebService
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");
        IndexedContainer indx = new IndexedContainer();
        try {
            String[] drivers = new String[]{"Adam Dolny", "Zygmunt Kowalski"};
            Integer[] numer_komunikatu = new Integer[]{1, 2};
            Date[] dataTime = new Date[]{sdf.parse("13.01.2014 11:32:00"), sdf.parse("14.01.2014 21:55:00")};
            StatusEnum[] state = new StatusEnum[]{StatusEnum.Pauza, StatusEnum.Start};

            indx.addContainerProperty("Kierowca", String.class, null);
            indx.addContainerProperty("ID", Integer.class, null);
            indx.addContainerProperty("Komunikat", StatusEnum.class, null); //TODO improve style (visibility)
            indx.addContainerProperty("Data nadesłania", Date.class, null);
            indx.addContainerProperty("", CheckBox.class, null);
            indx.addContainerProperty("Status", String.class, null); //TODO filtrowanie nie działa nawet po dodaniu generowanej kolumny do kontenera i próbie nadpisania. Niestety, aby kolumna się odświeżała to trzebą ją generować za każdym razem (usuwać i dodawać) i to uniemożliwia filtrowanie

            commTable.setColumnAlignment("", CustomTable.ALIGN_CENTER); //TODO nie centeruje checkboxów
            commTable.setColumnAlignment("ID", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("Komunikat", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("Data nadesłania", CustomTable.ALIGN_CENTER);
            commTable.setColumnAlignment("Status", CustomTable.ALIGN_CENTER);

            for (int j = 0; j < 50; j++) {
                for (int i = 0; i < 2; i++) {
                    CheckBox isReadBool = new CheckBox("Przeczytaj");
                    isReadBool.addListener(new Property.ValueChangeListener() {
                        @Override
                        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                            commTable.removeGeneratedColumn("Status");
                            commTable.addGeneratedColumn("Status", new CustomTable.ColumnGenerator() {
                                @Override
                                public Object generateCell(CustomTable components, Object o, Object o2) {
                                    Object TableCheckInstance = components.getContainerProperty(o, "").getValue();
                                    Boolean valueOfInstance = null;
                                    if (TableCheckInstance instanceof CheckBox) {
                                        valueOfInstance = ((CheckBox) TableCheckInstance).getValue();
                                    }

                                    String finalKnow = "";
                                    if (valueOfInstance) {
                                        finalKnow = "Przeczytana";
                                    } else {
                                        finalKnow = "Nieprzeczytana";
                                    }
                                    return finalKnow;
                                }
                            });
                        }
                    });
                    isReadBool.setImmediate(true);

                    ReadEnum isRead = ReadEnum.Nieprzeczytany;
                    //todo if isRead.checked = true generate column
                    if (drivers[i].equals("Adam Dolny")) { //!!
                        isReadBool.setValue(true);
                        isRead = ReadEnum.Przeczytany;
                    }
                    Object id = indx.addItem();
                    indx.getContainerProperty(id, "Kierowca").setValue(drivers[i]);
                    indx.getContainerProperty(id, "ID").setValue(numer_komunikatu[i]);
                    indx.getContainerProperty(id, "Komunikat").setValue(state[i]);
                    indx.getContainerProperty(id, "Data nadesłania").setValue(dataTime[i]);
                    indx.getContainerProperty(id, "").setValue(isReadBool);
//                    indx.getContainerProperty(id, "Status").setValue("hej"); //todo test czy jest w konetenrze kolumna status
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return indx;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
