package com.application.android.neararsanvar.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class PermissionsUtils {

    public static boolean hasPermissions(Context context, String... permissions) {
        boolean arePermissionsGranted = true;

        for (String permission : permissions) {
            if (!(ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED)) {
                arePermissionsGranted = false;
            }
        }

        return arePermissionsGrantedAutomatically() || arePermissionsGranted;
    }

    private static boolean arePermissionsGrantedAutomatically() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }
}
