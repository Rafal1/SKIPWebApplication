package SKIPWebApplication;

public class SkipapplicationService {

	public static Boolean fieldNamesIfConstains(String [] array, String key) {
		for (String names : array) {
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
