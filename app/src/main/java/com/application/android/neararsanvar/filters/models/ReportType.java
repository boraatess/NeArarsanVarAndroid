package com.application.android.neararsanvar.filters.models;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.UIUtils;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public enum ReportType {

    AD(UIUtils.getString(R.string.categories_report_type_ad)),
    USER(UIUtils.getString(R.string.categories_report_type_user));

    private String value;

    ReportType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
