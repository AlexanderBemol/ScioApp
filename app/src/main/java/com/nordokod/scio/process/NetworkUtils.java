package com.nordokod.scio.process;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class NetworkUtils {
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo.isConnected();
    }
}