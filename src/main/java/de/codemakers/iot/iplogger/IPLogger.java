package de.codemakers.iot.iplogger;

import de.codemakers.iot.IFTTJ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class IPLogger {
    
    public static final File FILE_PROPERTIES = new File("IPLogger.cfg");
    
    public static final Properties PROPERTIES = new Properties();
    
    static {
        loadProperties();
    }
    
    public static final void main(String[] args) throws Exception {
        System.out.println("Started " + IPLogger.class.getSimpleName());
        logIPAddress();
        System.out.println("Finished " + IPLogger.class.getSimpleName());
    }
    
    public static final void loadProperties() {
        try {
            if (!FILE_PROPERTIES.exists()) {
                FILE_PROPERTIES.createNewFile();
                //PROPERTIES.clear();
                PROPERTIES.setProperty("ifttt_key", "Insert your IFTTT Webhook Key here");
                PROPERTIES.store(new FileOutputStream(FILE_PROPERTIES, false), "Changed on:");
            }
            PROPERTIES.clear();
            PROPERTIES.load(new FileInputStream(FILE_PROPERTIES));
            IFTTJ.KEY = PROPERTIES.getProperty("ifttt_key", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static final void logIPAddress() {
        try {
            final String ip_address = IFTTJ.getOutInetAddress();
            IFTTJ.trigger("log_ip_address", ip_address);
            System.out.println("Logged IP Address: " + ip_address);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
