package zimmermann.larissa.moveitcollector.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {

    private static boolean isWiFiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) &&
                (networkInfo.isConnectedOrConnecting()) &&
                (networkInfo.getType() == connectivityManager.TYPE_WIFI);
    }

    private static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null
                && networkInfo.isConnectedOrConnecting()
                && networkInfo.getType() == connectivityManager.TYPE_MOBILE;
    }

    public static boolean isConnected(Context context) {
        return isWiFiConnected(context) || isMobileNetworkConnected(context);
    }

}
