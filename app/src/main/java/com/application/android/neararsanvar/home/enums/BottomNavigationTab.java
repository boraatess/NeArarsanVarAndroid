package com.application.android.neararsanvar.home.enums;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.application.android.neararsanvar.R;

/**
 /*-------------------------------

 - Classifier -

 Created by CarbonCode Technology @2019
 All Rights reserved.

 -------------------------------*/
public enum BottomNavigationTab {

    HOME(R.drawable.tb_home_unselect, R.drawable.tb_home, R.string.home_tab_browse, false),

    ACTIVITIES(R.drawable.tb_explore, R.drawable.tb_explore_selected, R.string.home_tab_activity, false),
    SELL(R.drawable.tb_add, R.drawable.tb_add, 0, true),
    LIKES(R.drawable.tb_activity, R.drawable.tb_activity_selected, R.string.home_tab_likes, false),
    ACCOUNT(R.drawable.tb_user, R.drawable.tb_user_selected, R.string.home_tab_account, false);

    public static final float SPECIAL_ITEM_WIDTH_RATIO = 1.0f;

    @DrawableRes
    private int selectedResId;
    @DrawableRes
    private int unselectedResId;
    @StringRes
    private int titleResId;
    private boolean isSpecialItem;

    BottomNavigationTab(@DrawableRes int unselectedResId,
                        @DrawableRes int selectedResId,
                        @StringRes int titleResId,
                        boolean isSpecialItem) {
        this.unselectedResId = unselectedResId;
        this.selectedResId = selectedResId;
        this.titleResId = titleResId;
        this.isSpecialItem = isSpecialItem;
    }

    public static int getSpecialItemCount() {
        int counter = 0;
        for (BottomNavigationTab current : BottomNavigationTab.values()) {
            if (current.isSpecialItem()) {
                counter++;
            }
        }
        return counter;
    }

    public boolean isSpecialItem() {
        return isSpecialItem;
    }

    public int getUnselectedResId() {
        return unselectedResId;
    }

    public int getSelectedResId() {
        return selectedResId;
    }

    public int getTitleResId() {
        return titleResId;
    }
}
