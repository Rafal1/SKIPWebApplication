package SKIPWebApplication;

public class SkipapplicationService {

	public static Boolean fieldNamesIfConstains(String key) {
		String[] fieldNames = DriversView.getFieldnames();
		for (String names : fieldNames) {
			if (names.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static String createFormatForDetailSizeTab(Integer px) {
		return px.toString() + "px";
	}

}
