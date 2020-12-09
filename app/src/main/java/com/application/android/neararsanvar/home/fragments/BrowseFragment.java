package com.application.android.neararsanvar.home.fragments;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import com.application.android.neararsanvar.Chats;
import com.application.android.neararsanvar.ads.activities.BrowseSubcategoryActivity;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.common.fragments.BaseFragment;
import com.application.android.neararsanvar.home.adapters.BrowseCategoriesAdapter;
import com.application.android.neararsanvar.utils.ToastUtils;

public class BrowseFragment extends BaseFragment {

    //private EditText searchTxt;
    private RecyclerView categoriesRV;
    private TextView appNameTitle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setUpViews();
        queryCategories();
    }

    private void initViews() {
        //searchTxt = getActivity().findViewById(classifier.carboncodetechnology.com.classifier.R.id.hSearchTxt);
        categoriesRV = getActivity().findViewById(R.id.fb_categories_rv);
        appNameTitle = getActivity().findViewById(R.id.appNameTitle);
        appNameTitle.setTypeface(Configs.mainTitle);
    }

    private void setUpViews() {
        //searchTxt.setTypeface(Configs.titRegular);
        // MARK: - SEARCH ADS BY KEYWORDS --------------------------------------------------------
       /* searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!searchTxt.getText().toString().matches("")) {

                        UIUtils.hideKeyboard(searchTxt);

                        // Pass strings to AdsListActivity.java
                        Intent adsListIntent = new Intent(getActivity(), AdsListActivity.class);
                        adsListIntent.putExtra(AdsListActivity.SEARCH_QUERY_KEY, searchTxt.getText().toString());
                        adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, Constants.BrowseCategories.DEFAULT_CATEGORY_NAME);
                        startActivity(adsListIntent);

                        return true;
                    }

                    // No text -> No search
                } else {
                    ToastUtils.showMessage(getString(R.string.browse_search_warning));
                }

                return false;
            }
        });*/

        // EditText TextWatcher delegate
        /*searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int closeDrawable;
                if (s.length() == 0) {
                    closeDrawable = 0;
                } else {
                    closeDrawable = R.drawable.close_white_ic;
                }
                searchTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, closeDrawable, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });*/
        //final Drawable imgClearButton = ContextCompat.getDrawable(getActivity(), R.drawable.close_white_ic);
       /* searchTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchTxt.getText().length() > 0) {
                    if (event.getX() > searchTxt.getWidth() - searchTxt.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                        searchTxt.setText("");
                        return true;
                    }
                }
                return false;
            }
        });*/

        // MARK: - CHATS BUTTON ------------------------------------
        ImageView chatButt = getActivity().findViewById(R.id.hChatButt);
        chatButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ParseUser.getCurrentUser().getUsername() != null) {
                    startActivity(new Intent(getActivity(), Chats.class));
                } else {
                    Configs.loginAlert(getString(R.string.browse_chat_error), getActivity());
                }
            }
        });

        // Init AdMob banner
        //AdView mAdView = getActivity().findViewById(classifier.carboncodetechnology.com.classifier.R.id.admobBanner);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
    }


    private void queryCategories() {
        showLoading(getActivity());


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.CATEGORIES_CLASS_NAME);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                hideLoading();
                if (error == null) {
                    categoriesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    categoriesRV.setAdapter(new BrowseCategoriesAdapter(objects, new BrowseCategoriesAdapter.OnCategorySelectedListener() {
                        @Override
                        public void onCategorySelected(ParseObject categoryObj) {
                            String selectedCategoryName = categoryObj.getString(Configs.CATEGORIES_CATEGORY);
                            Intent BrowseSubcategoryActivityIntent = new Intent(getActivity(), BrowseSubcategoryActivity.class);
                            BrowseSubcategoryActivityIntent.putExtra(BrowseSubcategoryActivity.CATEGORY_NAME_KEY, selectedCategoryName);
                            startActivity(BrowseSubcategoryActivityIntent);
                        }
                    }));
                } else {
                    hideLoading();
                    ToastUtils.showMessage(error.getMessage());
                }
            }
        });
    }
}
