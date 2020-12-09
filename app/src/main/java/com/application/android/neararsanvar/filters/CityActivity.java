package com.application.android.neararsanvar.filters;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.ToastUtils;

public class CityActivity extends BaseActivity {

    public static final String SELECTED_CITIES_EXTRA_KEY = "SELECTED_CITIES_EXTRA_KEY";
    public static final String HAS_FILTER_ROLE_EXTRA_KEY = "HAS_FILTER_ROLE_EXTRA_KEY";

    private ImageView backIV;
    private ImageView doneIV;
    private RecyclerView citiesRv;

    private List<String> cities;
    private String selectedCities = "";

    private boolean hasFilterRole = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        selectedCities = getIntent().getStringExtra(SELECTED_CITIES_EXTRA_KEY);

        hasFilterRole = getIntent().getBooleanExtra(HAS_FILTER_ROLE_EXTRA_KEY, true);
        if (selectedCities == null) {
            selectedCities = "";
        }

        initViews();
        cities = new ArrayList<>();

        if (hasFilterRole) {
            cities.add(0, getString(R.string.cities_all_title));
        }

        // Call query
        queryCities();
        setUpViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setSelectionResult();


    }

    private void initViews() {
        backIV = findViewById(R.id.ac_back_iv);
        doneIV = findViewById(R.id.ac_done_iv);
        citiesRv = findViewById(R.id.ac_cities_rv);
    }

    private void setUpViews() {
        // MARK: - DONE BUTTON ------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(selectedCities)) {
                    setSelectionResult();
                } else {
                    ToastUtils.showMessage(getString(R.string.city_done_error));
                }
            }
        });

        // MARK: - CANCEL BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cities.clear();
                finish();
            }
        });
    }

    private void setSelectionResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SELECTED_CITIES_EXTRA_KEY, selectedCities);
        //resultIntent.putExtra(SELECTED_SUBCITIES_EXTRA_KEY, selectedSubcategory);
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }

    // MARK: - QUERY CATEGORIES ---------------------------------------------------------------
    void queryCities() {
        showLoading(CityActivity.this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.CITY_CLASS_NAME);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, ParseException error) {
                if (error == null) {
                    hideLoading();

                    for (int i = 0; i < objects.size(); i++) {
                        // Get Parse object
                        ParseObject cObj = objects.get(i);
                        cities.add(cObj.getString(Configs.CITY_CITIES));
                    }

                    citiesRv.setLayoutManager(new LinearLayoutManager(CityActivity.this));
                    citiesRv.setAdapter(new FilterAdapter(cities, selectedCities,
                            new FilterAdapter.OnFilterSelectedListener() {
                                @Override
                                public void onFilterSelected(String filter) {
                                    selectedCities = filter;
                                    Log.i("log-", "SELECTED CATEGORY: " + selectedCities);
                                    setSelectionResult();
                                }
                            }));
                    // Error in query
                } else {
                    hideLoading();
                    ToastUtils.showMessage(error.getMessage());
                }
            }
        });
    }
}
