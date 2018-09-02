package de.codemakers.iot.iplogger;

import de.codemakers.iot.IFTTJ;

public class IPLogger {
    
    public static final void main(String[] args) throws Exception {
        System.out.println("Test started");
        IFTTJ.KEY = "";
        final String ipaddress = IFTTJ.getOutInetAddress();
        IFTTJ.trigger("log_ip_address", ipaddress);
        System.out.println("Sent ip address: " + ipaddress);
        System.out.println("Test finished");
    }
    
}
