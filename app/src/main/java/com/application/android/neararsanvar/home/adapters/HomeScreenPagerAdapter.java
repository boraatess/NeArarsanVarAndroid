package com.application.android.neararsanvar.home.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.application.android.neararsanvar.home.fragments.AccountFragment;
import com.application.android.neararsanvar.home.fragments.ActivityFragment;
import com.application.android.neararsanvar.home.fragments.BrowseFragment;
import com.application.android.neararsanvar.home.fragments.MyLikesFragment;

public class HomeScreenPagerAdapter extends FragmentStatePagerAdapter {

    public final static int FRAGMENTS_COUNT = 4;

    public final static int HOME_FRAGMENT_POSITION = 0;
    public final static int LIKES_FRAGMENT_POSITION = 1;
    public final static int ACTIVITIES_FRAGMENT_POSITION = 2;
    public final static int ACCOUNT_FRAGMENT_POSITION = 3;

    public HomeScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HOME_FRAGMENT_POSITION:
                return new BrowseFragment();
            case LIKES_FRAGMENT_POSITION:
                return new MyLikesFragment();
            case ACTIVITIES_FRAGMENT_POSITION:
                return new ActivityFragment();
            case ACCOUNT_FRAGMENT_POSITION:
                return new AccountFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
