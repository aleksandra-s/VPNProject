public class Handshake {
    /* Static data -- replace with handshake! */

    /* Where the client forwarder forwards data from  */
    public static final String serverHost = "localhost";
    public static final int serverPort = 4412;

    /* The final destination */
    public static String targetHost = "localhost";
    public static int targetPort = 6789;

    public static SessionKey sessionKey;
    public static SessionIV sessionIV;
}
