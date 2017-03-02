package com.laulee.rx.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by laulee on 17/3/1.
 */

public class Constants {
    //================= PATH ====================

    public static final String PATH_DATA = Environment.getExternalStorageDirectory( )
            .getAbsolutePath( ) + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
    public static final String BASE_URL = "https://api.github.com";
}
