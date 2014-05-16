package SKIPWebApplication;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.tepi.filtertable.FilterGenerator;

/**
 * @author Rafal Zawadzki
 */
public class CommuniqueFilterGenerator implements FilterGenerator {
    @Override
    public Container.Filter generateFilter(Object o, Object o2) {
//        if ("ID".equals(o)) {
//                    /* Create an 'equals' filter for the ID field */
//            if (o2 != null && o2 instanceof String) {
//                try {
//
//                    return new Compare.Equal(o,
//                            Integer.parseInt( o2.toString()));
//                } catch (NumberFormatException ignored) {
//                    // If no integer was entered, just generate default filter
//                }
//            }
//        }
        return null;
    }

    @Override
    public Container.Filter generateFilter(Object o, Field<?> field) {
        return null;
    }

    @Override
    public AbstractField<?> getCustomFilterComponent(Object o) {
        if ("".equals(o)) {
            TextField empty = new TextField();
            empty.setVisible(false);
            return empty;
        }
        return null;
    }

    @Override
    public void filterRemoved(Object o) {
    }

    @Override
    public void filterAdded(Object o, Class<? extends Container.Filter> aClass, Object o2) {
    }

    @Override
    public Container.Filter filterGeneratorFailed(Exception e, Object o, Object o2) {
        return null;
    }
}
