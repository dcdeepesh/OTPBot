package dec.daemon.otpbot.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import dec.daemon.otpbot.R;
import dec.daemon.otpbot.TrayManager;
import dec.daemon.otpbot.entity.Message;
import dec.daemon.otpbot.net.GMail;

public class Github {
    public static class UpdateThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                // keep on checking for new code indefinitely
                checkForNewCode();
                if (hasNewCode()) {
                    System.out.println("New Github code: " + getNewCode());
                    TrayManager.notify(getNewCode(), "Github");
                    markNewCodeAsRead();
                }
    
                try {
                    Thread.sleep(R.UPDATE_INTERVAL_MS);
                } catch (InterruptedException e) {}
            }
        }
    }

    private static final String FILE_NAME = "optDB_github";
    private static String newCode = null;
    private static String newMsgId = null;

    private static void checkForNewCode() {
        List<String> msgIds =
            GMail.filterSearch("from:(noreply@github.com) verification code");

        for (String id : msgIds) {
            if (!isRead(id)) {
                newCode = extractCode(id);
                newMsgId = id;
                return;
            }
        }

        // no new code was found
        newCode = null;
    }

    private static boolean hasNewCode() {
        return newCode != null;
    }
    private static String getNewCode() {
        return newCode;
    }

    private static void markNewCodeAsRead() {
        FileWriter writer = null;
        try {
            File file = new File(FILE_NAME);
            if (!file.exists())
                file.createNewFile();

            writer = new FileWriter(file, true);
            writer.write(newMsgId + "\n");
        } catch (IOException e) {
            System.err.println("Error marking as read: "  + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error closing file writer");
                }
            }    
        }
    }

    private static boolean isRead(String msgId) {
        String fileContents = null;

        try {
            File fileObj = new File(FILE_NAME);
            if (!fileObj.exists())
                fileObj.createNewFile();

            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(fileObj);

            int c;
            while ((c = fis.read()) != -1)
                sb.append((char)c);
            fileContents = sb.toString();

            fis.close();
        } catch (IOException e) {
            System.err.println("Error reading cache:\n" + e);
        }

        return fileContents.contains(msgId);
    }

    private static String extractCode(String msgId) {
        Message msg = GMail.getMessage(msgId);

        // extract and decode mail body from objects
        String base64data = msg.payload.body.data;
        String decodedData = new String(Base64.getDecoder().decode(base64data));
        
        // find and extract the code from the mail body
        int codeBeg = decodedData.indexOf("Verification code: ") + 19;
        int codeEnd = decodedData.indexOf("\n", codeBeg);
        String code = decodedData.substring(codeBeg, codeEnd);

        return code;
    }
}