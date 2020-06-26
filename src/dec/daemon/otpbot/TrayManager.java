package dec.daemon.otpbot;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class TrayManager {
    private static TrayIcon icon;

    public static void init() {
        if (!SystemTray.isSupported()) {
            System.out.println("Unsupported tray");
            System.exit(1);
        }

        SystemTray tray = SystemTray.getSystemTray();
        icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("res\\boticon.png"));

        try {
            tray.add(icon);
        } catch (AWTException e) {
            System.err.println("Could not add tray icon:\n" + e);
            System.exit(1);
        }
    }

    public static void notify(String code, String serviceName) {
        icon.displayMessage(code, serviceName + " verification code",
            TrayIcon.MessageType.INFO);
    }
}