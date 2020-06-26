package dec.daemon.otpbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class PreferenceManager {
    public static String getClientID() {
        return getCredentialFromFile("clientId");
    }

    public static String getClientSecret() {
        return getCredentialFromFile("clientSecret");
    }

    public static String getRefreshToken() {
        return getCredentialFromFile("refreshToken");
    }

    private static String getCredentialFromFile(String fileName) {
        String credential = null;

        try {
            credential = loadFile(fileName);
            if (credential == null || credential.trim().equals(""))
                throw new IOException("Empty credentials");
        } catch (IOException e) {
            System.err.println("Error retrieving credential '" + fileName + "':\n" + e);
            System.exit(1);
        }

        return credential;
    }

    private static String loadFile(String fileName) throws IOException {
        File file = Paths.get(System.getProperty("user.home"),
            ".dec", "otpbot", fileName).toFile();

        if (!file.exists())
            throw new IOException("File not found");

        // try block used for auto closing
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            return in.readLine();
        }
    }
}