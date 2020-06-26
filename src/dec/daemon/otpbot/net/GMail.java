package dec.daemon.otpbot.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dec.daemon.otpbot.entity.AbbrevMessage;
import dec.daemon.otpbot.entity.Message;
import dec.daemon.otpbot.entity.MessageList;

public class GMail {
    public static ArrayList<String> filterSearch(String searchQuery) {
        final String BASE_URL =
            "https://www.googleapis.com/gmail/v1/users/me/messages";
        ArrayList<String> ids = new ArrayList<>();
        
        try {
            String url = BASE_URL + "?q=" + URLEncoder.encode(searchQuery, "UTF-8");
            String response = API.request(url);

            MessageList msgs = new Gson().fromJson(response, MessageList.class);
            for (AbbrevMessage msg : msgs.messages)
                ids.add(msg.id);
        } catch (IOException e) {
            System.err.println("IOException in filterSearch:\n" + e);
            System.exit(1);
        }

        return ids;
    }

    public static Message getMessage(String messageId) {
        final String BASE_URL =
            "https://www.googleapis.com/gmail/v1/users/me/messages/";
        String url = BASE_URL + messageId;
        Message msg = null;

        // deserialize json to objects
        try {
            String response = API.request(url);
            msg = new Gson().fromJson(response, Message.class);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error getting message:\n" + e);
            System.exit(1);
        }

        return msg;
    }
}