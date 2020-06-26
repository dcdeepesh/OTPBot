package dec.daemon.otpbot.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dec.daemon.otpbot.TokenManager;

public class API {
    public static String request(String encodedUrl) throws IOException {
        URL url = new URL(encodedUrl);
        URLConnection conn = url.openConnection();
        
        conn.setRequestProperty("Authorization",
            "Bearer " + TokenManager.getAccessToken());

        InputStream is = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = is.read()) != -1)
            sb.append((char)i);

        return sb.toString();
    }

    public static String requestUnauthorized(String url) throws IOException {
        URL encodedUrl = new URL(URLEncoder.encode(url, "UTF-8"));
        URLConnection conn = encodedUrl.openConnection();

        InputStream is = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = is.read()) != -1)
            sb.append((char)i);

        return sb.toString();
    }
}