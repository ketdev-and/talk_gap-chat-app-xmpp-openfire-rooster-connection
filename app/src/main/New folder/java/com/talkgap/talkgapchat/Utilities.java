package com.blikoon.roosterplus;

import android.app.ActivityManager;
import android.content.Context;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

public class Utilities {

    //check if the service is running

    public static boolean isServiceRunning (Class<?> serviceClass , Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }

        }
        return false;

    }

    public static boolean isNetworkAvailable (Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }


}
