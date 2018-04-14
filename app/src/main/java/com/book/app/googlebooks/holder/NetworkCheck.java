package com.book.app.googlebooks.holder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class NetworkCheck extends AppCompatActivity {

    private static final String TAG = NetworkCheck.class.getSimpleName();

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            Log.e(TAG, "No Network connection");
            return false;
        } else {
            if (info.isConnected()) {
                Log.e(TAG, " Network connection available...");
                return true;
            } else {
                Log.e(TAG, "No Network connection");
                return false;
            }
        }
    }
}