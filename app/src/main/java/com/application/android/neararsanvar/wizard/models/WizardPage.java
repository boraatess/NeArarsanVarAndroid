package com.application.android.neararsanvar.wizard.models;

import com.application.android.neararsanvar.R;

public enum WizardPage {

    RED(R.string.wizard_page1, R.drawable.wizard1),
    BLUE(R.string.wizard_page2, R.drawable.wizard2),
    GREEN(R.string.wizard_page3, R.drawable.wizard3);


    private int descriptionResId;
    private int backgroundResId;

    WizardPage(int descriptionResId, int backgroundResId) {
        this.descriptionResId = descriptionResId;
        this.backgroundResId = backgroundResId;
    }

    public int getDescriptionResId() {
        return descriptionResId;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }
}
