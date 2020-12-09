package com.application.android.neararsanvar.ads.activities;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.ads.adapters.BrowseSubcategoriesAdapter;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.ToastUtils;

import static com.application.android.neararsanvar.common.activities.BaseActivity.hideLoading;
import static com.application.android.neararsanvar.common.activities.BaseActivity.showLoading;

public class BrowseSubcategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_NAME_KEY = "CATEGORY_NAME_KEY";
    public static final String AD_OBJ_ID_KEY = "AD_OBJ_ID_KEY";

    //private EditText searchTxt;
    private RecyclerView subcategoriesRV;
    //private TextView appNameTitle;
    Activity activity;
    private ImageView back;
    private AlertDialog progressDialog;
    private TextView categoryTitle;
    private String selectedCategory;

    /* Variables */
    private ParseObject adObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_browse_subcategory);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //searchString = extras.getString(SEARCH_QUERY_KEY);
            selectedCategory = extras.getString(CATEGORY_NAME_KEY);
        }


        initViews();
        showCategoryTitle();
        //setUpViews();
        queryCategories();


        //String objectID = extras.getString(AD_OBJ_ID_KEY);
        //adObj = ParseObject.createWithoutData(Configs.ADS_CLASS_NAME, objectID);
//
        //try {
        //    adObj.fetchIfNeeded().getParseObject(Configs.ADS_CLASS_NAME);
        //
//
        //} catch (ParseException e) {
        //    e.printStackTrace();
        //}

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initViews() {
        //noLikesRL = findViewById(R.id.mlNoLikesLayout);
        //adView = findViewById(R.id.admobBanner);
        subcategoriesRV = findViewById(R.id.abs_subcategories_rv);
        back = findViewById(R.id.abs_back);
        categoryTitle = findViewById(R.id.abs_categoryTitle);
    }


    // MARK: - SHOW AD DETAILS ---------------------------------------------------------------------
    void showCategoryTitle() {
        // Set Category Title
        //categoryTitle.setTypeface(Configs.titSemibold);
        categoryTitle.setTypeface(Configs.mainTitle);
        categoryTitle.setText(R.string.app_name);

    }


        //private void setUpViews() {
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
        //ImageView chatButt = getActivity().findViewById(R.id.hChatButt);
        //chatButt.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        if (ParseUser.getCurrentUser().getUsername() != null) {
        //            startActivity(new Intent(getActivity(), Chats.class));
        //        } else {
        //            Configs.loginAlert(getString(R.string.browse_chat_error), getActivity());
        //        }
        //    }
        //});

        // Init AdMob banner
        //AdView mAdView = getActivity().findViewById(classifier.carboncodetechnology.com.classifier.R.id.admobBanner);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
    //}

    private void queryCategories() {
        //showLoading(BrowseSubcategoryActivity.this);
        showLoading(BrowseSubcategoryActivity.this);




        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.SUBCATEGORIES_CLASS_NAME);
        query.whereEqualTo(Configs.SUBCATEGORIES_CATEGORY, selectedCategory);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                hideLoading();
                if (error == null) {
                    subcategoriesRV.setLayoutManager(new LinearLayoutManager(activity));
                    subcategoriesRV.setAdapter(new BrowseSubcategoriesAdapter(objects, new BrowseSubcategoriesAdapter.OnSubcategorySelectedListener() {
                        @Override
                        public void onSubcategorySelected(ParseObject subcategoryObj) {
                            String selectedSubcategoryName = subcategoryObj.getString(Configs.SUBCATEGORIES_SUBCATEGORY);
                            Intent adsListIntent = new Intent(activity, AdsListActivity.class);
                            adsListIntent.putExtra(AdsListActivity.SUBCATEGORY_NAME_KEY, selectedSubcategoryName);
                            adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, selectedCategory);
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

    //protected void showLoading(BrowseSubcategoryActivity browseSubcategoryActivity) {
    //    progressDialog = Configs.buildProgressLoadingDialog(activity);
    //    progressDialog.show();
    //}
//
//
    //protected void hideLoading() {
    //    if (progressDialog != null) {
    //        progressDialog.dismiss();
    //    }
    //}
}
