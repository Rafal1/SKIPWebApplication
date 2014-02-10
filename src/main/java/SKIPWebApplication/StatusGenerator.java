package SKIPWebApplication;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * @author Rafal Zawadzki
 */
public class StatusGenerator implements Table.ColumnGenerator {

    public Component generateCell(Table source, Object itemId,
                                  Object columnId) {
        // Get the object stored in the cell as a property
//        Property prop =
//                source.getItem(itemId).getItemProperty(columnId);
//        if (prop.getType().equals(StatusEnum.class)) {
//            Label label = new Label(String.format(format,
//                    new Object[]{(Double) prop.getValue()}));
//            return label;
//        }
        return null;
    }
}
