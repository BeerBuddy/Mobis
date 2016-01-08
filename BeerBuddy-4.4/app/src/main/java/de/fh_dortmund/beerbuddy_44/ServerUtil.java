package de.fh_dortmund.beerbuddy_44;

import android.os.Build;

/**
 * Created by David on 06.01.2016.
 */
public class ServerUtil {

    private static final String HOST_EMULATOR = "10.0.2.2";
    private static final String HOST_PRODUCTION = "example.com";
    private static final String HOST_PORT = "9000";
    private static final String HOST_PROTOCOL = "http://";

    public static String getHost() {
        if (BuildConfig.DEBUG) {
            return (Build.PRODUCT).contains("sdk") ? HOST_PROTOCOL+HOST_EMULATOR+":"+HOST_PORT : HOST_PROTOCOL+BuildConfig.LOCAL_IP+":"+HOST_PORT;
            //return  HOST_PROTOCOL+BuildConfig.LOCAL_IP+":"+HOST_PORT;
        }
        return HOST_PROTOCOL+HOST_PRODUCTION+":"+HOST_PORT;
    }
}
