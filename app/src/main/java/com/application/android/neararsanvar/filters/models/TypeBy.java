package com.application.android.neararsanvar.filters.models;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.UIUtils;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public enum TypeBy {


    ALL(UIUtils.getString(R.string.type_all)),
    SELL(UIUtils.getString(R.string.type_sell)),
    RENT(UIUtils.getString(R.string.type_rent)),
    EXCHANGE(UIUtils.getString(R.string.type_exchange)),
    GIFT(UIUtils.getString(R.string.type_gift));

    private String value;

    TypeBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TypeBy getForValue(String value) {
        for (int i = 0; i < TypeBy.values().length; i++) {
            if (TypeBy.values()[i].getValue().equals(value)) {
                return TypeBy.values()[i];
            }
        }
        return null;
    }
}
