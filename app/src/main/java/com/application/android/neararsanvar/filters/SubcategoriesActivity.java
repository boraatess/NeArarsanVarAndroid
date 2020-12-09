package com.application.android.neararsanvar.filters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.utils.ToastUtils;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

public class SubcategoriesActivity extends BaseActivity {

    public static final String IN_SUBCATEGORIES_EXTRA_KEY = "IN_SUBCATEGORIES_EXTRA_KEY";
    public static final String IN_OUT_SELECTED_SUBCATEGORY_EXTRA_KEY = "IN_OUT_SELECTED_SUBCATEGORY_EXTRA_KEY";
    public static final String IN_INCLUDE_ALL_SUBCATEGORY_EXTRA_KEY = "IN_INCLUDE_ALL_SUBCATEGORY_EXTRA_KEY";
    //public static final String SELECTED_CATEGORY = "SELECTED_CATEGORY";


    private ImageView backIV;
    private ImageView doneIV;
    private RecyclerView categoriesRV;
    private TextView titleTV;

    private List<String> subcategories;
    private String selectedSubcategory = "";
    private String selectedCategory;

    //Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        //activity = this;
        //Bundle extras = getIntent().getExtras();
        //if (extras != null) {
        //    selectedCategory = extras.getString(SELECTED_CATEGORY);
        //}

        boolean includeAll = getIntent().getBooleanExtra(IN_INCLUDE_ALL_SUBCATEGORY_EXTRA_KEY, true);
        selectedSubcategory = getIntent().getStringExtra(IN_OUT_SELECTED_SUBCATEGORY_EXTRA_KEY);
        subcategories = getIntent().getStringArrayListExtra(IN_SUBCATEGORIES_EXTRA_KEY);

        if (selectedSubcategory == null) {
            selectedSubcategory = "";
        }

        // add "All" to categories array
        if (subcategories == null) {
            subcategories = new ArrayList<>();
        }
        if (includeAll) {
            subcategories.add(0, getString(R.string.categories_all_title));
        }

        initViews();
        setUpViews();
        setUpSubcategoriesList();
    }

    private void initViews() {
        backIV = findViewById(R.id.ac_back_iv);
        doneIV = findViewById(R.id.ac_done_iv);
        categoriesRV = findViewById(R.id.ac_categories_rv);
        titleTV = findViewById(R.id.ac_title_tv);
    }

    private void setUpViews() {
        titleTV.setText(R.string.subcategories_select_title);
        // MARK: - DONE BUTTON ------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(selectedSubcategory)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(IN_OUT_SELECTED_SUBCATEGORY_EXTRA_KEY, selectedSubcategory);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    ToastUtils.showMessage(getString(R.string.subcategories_selection_empty));
                }
            }
        });

        // MARK: - CANCEL BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpSubcategoriesList() {
        categoriesRV.setLayoutManager(new LinearLayoutManager(this));
        categoriesRV.setAdapter(new FilterAdapter(subcategories, selectedSubcategory,
                new FilterAdapter.OnFilterSelectedListener() {
                    @Override
                    public void onFilterSelected(String filter) {
                        selectedSubcategory = filter;
                        Log.i("log-", "SELECTED SUBCATEGORY: " + selectedSubcategory);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(IN_OUT_SELECTED_SUBCATEGORY_EXTRA_KEY, selectedSubcategory);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                        //Intent adslistIntent = new Intent(activity, SubcategoriesActivity.class);
                        //subcategoryIntent.putExtra(SubcategoriesActivity.SELECTED_CATEGORY, selectedCategory);
                        //subcategoryIntent.putExtra(SubcategoriesActivity.SELECTED_CATEGORY, selectedCategory);
                        //startActivity(subcategoryIntent);
                    }
                }));
    }
}