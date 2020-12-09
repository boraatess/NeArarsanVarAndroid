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
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.ads.activities.AdsListActivity;
import com.application.android.neararsanvar.common.fragments.BaseFragment;
import com.application.android.neararsanvar.ads.adapters.BrowseSubcategoriesAdapter;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.ToastUtils;

public class BrowseSubcategoryFragment extends BaseFragment {

    //private EditText searchTxt;
    private RecyclerView subcategoriesRV;
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
        querySubcategories();
    }

    private void initViews() {
        //searchTxt = getActivity().findViewById(classifier.carboncodetechnology.com.classifier.R.id.hSearchTxt);
        subcategoriesRV = getActivity().findViewById(R.id.fb_categories_rv);
        appNameTitle = getActivity().findViewById(R.id.appNameTitle);
        appNameTitle.setTypeface(Configs.mainTitle);
        appNameTitle.setVisibility(View.GONE);

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
        chatButt.setVisibility(View.GONE);
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


    private void querySubcategories() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.SUBCATEGORIES_CLASS_NAME);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                hideLoading();
                if (error == null) {
                    subcategoriesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    subcategoriesRV.setAdapter(new BrowseSubcategoriesAdapter(objects, new BrowseSubcategoriesAdapter.OnSubcategorySelectedListener() {
                        @Override
                        public void onSubcategorySelected(ParseObject subcategoryObj) {
                            String selectedSubcategoryName = subcategoryObj.getString(Configs.SUBCATEGORIES_SUBCATEGORY);
                            Intent adsListIntent = new Intent(getActivity(), AdsListActivity.class);
                            adsListIntent.putExtra(AdsListActivity.SUBCATEGORY_NAME_KEY, selectedSubcategoryName);
                            startActivity(adsListIntent);
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
