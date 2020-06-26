package dec.daemon.otpbot;

public class R {
    public static final String CLIENT_ID = PreferenceManager.getClientID();
    public static final String CLIENT_SECRET = PreferenceManager.getClientSecret();
    public static final String REFRESH_TOKEN = PreferenceManager.getRefreshToken();

    public static final long UPDATE_INTERVAL_MS = 10000;
}