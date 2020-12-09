package com.application.android.neararsanvar.utils;

import com.parse.ParseUser;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class SessionUtils {

    public static boolean isUserLoggedIn() {
        return ParseUser.getCurrentUser().getUsername() != null;
    }
}
