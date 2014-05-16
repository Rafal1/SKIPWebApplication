package SKIPWebApplication;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import java.util.Locale;

/**
 * @author Rafal Zawadzki
 */
public class CommuniqueFilterDecorator implements FilterDecorator {

    @Override
    public String getEnumFilterDisplayName(Object o, Object stat) {

        if ("Komunikat".equals(o)) {
            StatusEnum state = (StatusEnum) stat;
            switch (state) {
                case Pauza:
                    return "Pauza";
                case Start:
                    return "Start";
            }
        }

        if ("Status".equals(o)) {
            ReadEnum state = (ReadEnum) stat;
            switch (state) {
                case Nieprzeczytany:
                    return "Nieprzeczytany";
                case Przeczytany:
                    return "Przeczytany";
            }
        }
        return null;
    }

    @Override
    public Resource getEnumFilterIcon(Object o, Object o2) {
        return null;
    }

    @Override
    public String getBooleanFilterDisplayName(Object o, boolean b) {
        return null;
    }

    @Override
    public Resource getBooleanFilterIcon(Object o, boolean b) {
        return null;
    }

    @Override
    public boolean isTextFilterImmediate(Object o) {
        return true;
    }

    @Override
    public int getTextChangeTimeout(Object o) {
        return 0;
    }

    @Override
    public String getFromCaption() {
        return "Od";
    }

    @Override
    public String getToCaption() {
        return "Do";
    }

    @Override
    public String getSetCaption() {

        return "Ustaw";
    }

    @Override
    public String getClearCaption() {
        return "Wyczyść";
    }

    @Override
    public Resolution getDateFieldResolution(Object o) {
        return null;
    }

    @Override
    public String getDateFormatPattern(Object o) {
        return null;
    }

    @Override
    public Locale getLocale() {
        return new Locale("pl_PL");
    }

    @Override
    public String getAllItemsVisibleString() {
        return null;
    }

    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        NumberFilterPopupConfig cinfInst = new NumberFilterPopupConfig();
        cinfInst.setEqPrompt("Równe");
        cinfInst.setGtPrompt("Wieksze");
        cinfInst.setOkCaption("Filtruj");
        cinfInst.setResetCaption("Resetuj");
        cinfInst.setLtPrompt("Mniejsze");
        return cinfInst;
    }

    @Override
    public boolean usePopupForNumericProperty(Object o) {
        return true;
    }
}
