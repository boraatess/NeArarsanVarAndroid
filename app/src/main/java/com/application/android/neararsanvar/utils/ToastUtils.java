package com.application.android.neararsanvar.utils;

import android.widget.Toast;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class ToastUtils {

    public static void showMessage(String message) {
        Toast.makeText(UIUtils.getAppContext(), message, Toast.LENGTH_LONG)
                .show();
    }
}
