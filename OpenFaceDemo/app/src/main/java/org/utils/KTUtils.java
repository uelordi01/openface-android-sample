package org.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by uelordi on 21/11/17.
 */

public class KTUtils {
    private static final String LOG_TAG = "JKTUtils";
    // VERY IMPORTANT: askPermission has to be called ALLWAYS from the main thread
    public static void askPermissions(Activity act) {
        final String[] permissions_needed = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
                //,Manifest.permission.SYSTEM_ALERT_WINDOW
        };
        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ArrayList array_aux = new ArrayList<String>();

            String[] permissions_requested;
            for (String aPermissions_needed : permissions_needed) {
                int hasPermission = ContextCompat.checkSelfPermission(act, aPermissions_needed);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    array_aux.add(aPermissions_needed);
                    Log.d("Activity", "request permission %s" + aPermissions_needed);
                }
            }
            if (array_aux.size() != 0) {
                //Log.d("Activity", "allow permission");
                permissions_requested = (String[]) array_aux.toArray(new String[array_aux.size()]);


                ActivityCompat.requestPermissions(act, permissions_requested,
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            Log.v(LOG_TAG, "No need to ask permissions programatically");
        }

    }
}
