package dec.daemon.otpbot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class TokenManager {
    private static String accessToken = null;

    public static void updateAccessToken() {
        try {
            URL url = new URL("https://oauth2.googleapis.com/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.getOutputStream().write(postOutput(R.REFRESH_TOKEN));

            InputStream is = conn.getInputStream();
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = is.read()) != -1)
                sb.append((char)i);
            String jsonOutput = sb.toString();

            conn.disconnect();

            JSONResponse response = new Gson().fromJson(jsonOutput, JSONResponse.class);
            accessToken = response.access_token;
        } catch (IOException e) {
            System.err.println("Error updating token:\n" + e);
            System.exit(1);
        }
    }

    private static byte[] postOutput(String refreshToken) {
        String postOutString = 
            "client_id="      + R.CLIENT_ID     +
            "&client_secret=" + R.CLIENT_SECRET +
            "&refresh_token=" + refreshToken  +
            "&grant_type=refresh_token";

        return postOutString.getBytes(StandardCharsets.UTF_8);
    }
    
    public static String getAccessToken() {
        return accessToken;
    }
}

class JSONResponse {
    String access_token;
}