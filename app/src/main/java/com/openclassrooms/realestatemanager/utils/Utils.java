package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.openclassrooms.realestatemanager.ConstantsKt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Convert dollar to euro
     * @param dollars
     * @return euros
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * ConstantsKt.DOLLAR_TO_EURO_COEFFICIENT);
    }

    /**
     * Convert euro to dollar
     * @param euros
     * @return dollars
     */

    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros/ConstantsKt.DOLLAR_TO_EURO_COEFFICIENT);
    }

    /**
     * Convert today's date to a well-formatted string
     * @return well-formatted string
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Check if internet is available or not
     * @param context
     * @return a boolean, true if internet is available false otherwise
     */
    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
