package dec.daemon.otpbot;

import dec.daemon.otpbot.services.Github;
import dec.daemon.otpbot.services.Twitch;

public class OTPBot {
    public static void main(String[] args) {
        TrayManager.init();
        TokenManager.updateAccessToken();
        startServices();

        System.out.println("BOT READY");
    }
    
    private static void startServices() {
        Runnable[] services = new Runnable[] {
            new Github.UpdateThread(),
            new Twitch.UpdateThread()
        };

        for (Runnable service : services) {
            Thread thread = new Thread(service);
            thread.setDaemon(true);
            thread.start();
        }
    }
}