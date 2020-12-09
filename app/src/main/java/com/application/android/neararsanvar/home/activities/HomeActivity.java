package com.application.android.neararsanvar.home.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.wizard.WizardActivity;
import com.application.android.neararsanvar.home.adapters.BottomNavigationAdapter;
import com.application.android.neararsanvar.home.adapters.HomeScreenPagerAdapter;
import com.application.android.neararsanvar.selledit.activities.SellEditItemActivity;
import com.application.android.neararsanvar.utils.SessionUtils;


public class HomeActivity extends AppCompatActivity implements BottomNavigationAdapter.BottomNavigationListener {

    private static final int INITIAL_SELECTED_TAB_POSITION = 0;

    private ViewPager contentVP;
    private RecyclerView bottomNavigationRV;

    private HomeScreenPagerAdapter pagerAdapter;
    private BottomNavigationAdapter bottomNavigationAdapter;

    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isUserLoggedIn = SessionUtils.isUserLoggedIn();

        initViews();
        setUpContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser().getUsername() != null) {
            // Register for Push Notifications
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();

            // IMPORTANT: REPLACE "579429182800" WITH YOUR OWN GCM Sender ID
            installation.put("GCMSenderId", "579429182800");
            //--------------------------------------------------------

            installation.put("username", ParseUser.getCurrentUser().getUsername());
            installation.put("userID", ParseUser.getCurrentUser().getObjectId());
            installation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i("log-", "REGISTERED FOR PUSH NOTIFICATIONS");
                }
            });
        }
    }

    private void initViews() {
        contentVP = findViewById(R.id.ah_content_vp);
        bottomNavigationRV = findViewById(R.id.ah_bottom_navigation_rv);
    }

    private void setUpContent() {
        setUpViewPager();
        setUpBottomNavigation();
    }

    private void setUpViewPager() {
        pagerAdapter = new HomeScreenPagerAdapter(getSupportFragmentManager());
        contentVP.setAdapter(pagerAdapter);
        contentVP.setOffscreenPageLimit(1);
    }

    private void setUpBottomNavigation() {
        ViewTreeObserver vto = bottomNavigationRV.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpBottomNavigationRV();
                bottomNavigationRV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setUpBottomNavigationRV() {
        bottomNavigationAdapter = new BottomNavigationAdapter(this, INITIAL_SELECTED_TAB_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        bottomNavigationRV.setAdapter(bottomNavigationAdapter);
        bottomNavigationRV.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onTabSelected(int pos) {
        if (!isUserLoggedIn && pos != INITIAL_SELECTED_TAB_POSITION) {
            startActivity(new Intent(this, WizardActivity.class));
            return false;
        }

        contentVP.setCurrentItem(pos);
        return true;
    }

    @Override
    public void onSpecialTabSelected() {
        if (!isUserLoggedIn) {
            startActivity(new Intent(this, WizardActivity.class));
            return;
        }

        startActivity(new Intent(this, SellEditItemActivity.class));
        return;
    }
}
