package SKIPWebApplication.receiveinformation;

/**
 * @author Kamil Malka
 */
public interface ServerInfo {

    public static String HOST = "skip-server-backup.herokuapp.com";
    public static int PORT = 8443;
    public static String SSL_ACCESS = "https://" + HOST;//+ ":" + PORT;

    public static String LOGIN_SUFFIX_URL = "/login";
    public static String LOGOUT_SUFFIX_URL = "/logout";
}
