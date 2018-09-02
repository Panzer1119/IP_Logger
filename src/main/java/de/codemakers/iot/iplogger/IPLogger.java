/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.iot.iplogger;

import de.codemakers.iot.IFTTJ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class IPLogger {
    
    public static final File FILE_PROPERTIES = new File("IPLogger.cfg");
    
    public static final Properties PROPERTIES = new Properties();
    
    static {
        loadProperties();
    }
    
    public static final void main(String[] args) throws Exception {
        System.out.println("Started " + IPLogger.class.getSimpleName());
        final int period = (args != null && args.length >= 1) ? Integer.parseInt(args[0]) : 0;
        final int duration = (args != null && args.length >= 2) ? Integer.parseInt(args[1]) : 0;
        if (period <= 0 || duration == 0) {
            logIPAddress();
        } else {
            System.out.println("Executing IP Address logging every " + (period / 1000) + " second");
            final Timer timer = new Timer();
            if (duration > 0) {
                System.out.println("IP Address logging ends in " + (duration / 1000) + " seconds");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timer.cancel();
                        System.out.println("Ended IP Address logging Timer");
                    }
                }, duration);
            }
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    logIPAddress();
                }
            }, 0, period);
        }
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
            final Instant instant = Instant.now();
            final String ip_address = IFTTJ.getOutInetAddress();
            IFTTJ.trigger("log_ip_address", ip_address, instant.toEpochMilli(), ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
            System.out.println(String.format("[%s] Logged IP Address: %s", ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME), ip_address));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
