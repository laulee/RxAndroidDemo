package com.laulee.rx.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.laulee.rx.app.App;

/**
 * Created by laulee on 17/3/1.
 */

public class SystemUtil {

    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance( )
                .getApplicationContext( ).getSystemService( Context.CONNECTIVITY_SERVICE );
        return connectivityManager.getActiveNetworkInfo( ) != null;
    }
}
