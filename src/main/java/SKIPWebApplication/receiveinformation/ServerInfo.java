package SKIPWebApplication.receiveinformation;

/**
 * @author Kamil Malka
 */
public interface ServerInfo {

    public static String HOST = "skip-server.herokuapp.com";
    public static int PORT = 8443;
    public static String SSL_ACCESS = "https://" + HOST ;//+ ":" + PORT;

    public static String LOGIN_SUFFIX_URL = "/spring_security_login";
    public static String LOGOUT_SUFFIX_URL = "/j%5Fspring%5Fsecurity%5Flogout";
}
