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
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.filters.models.TypeBy;
import com.application.android.neararsanvar.utils.ToastUtils;

public class TypeActivity extends BaseActivity {

    public static final String SELECTED_TYPE_EXTRA_KEY = "SELECTED_CATEGORY_EXTRA_KEY";

    private ImageView backIV;
    private ImageView doneIV;
    private RecyclerView typeRV;
    private List<String> typeArray = new ArrayList<>();
    private String selectedType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        selectedType = getIntent().getStringExtra(SELECTED_TYPE_EXTRA_KEY);
        if (selectedType == null) {
            selectedType = "";
        }

        // Init sortBy Array
        for (int i = 0; i < TypeBy.values().length; i++) {
            typeArray.add(TypeBy.values()[i].getValue());
        }

        initViews();
        setUpViews();
    }

    private void initViews() {
        backIV = findViewById(R.id.ac_back_iv);
        doneIV = findViewById(R.id.ac_done_iv);
        typeRV = findViewById(R.id.ac_type_rv);
    }

    private void setUpViews() {
        typeRV.setLayoutManager(new LinearLayoutManager(this));
        typeRV.setAdapter(new FilterAdapter(typeArray, selectedType, new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(String filter) {
                selectedType = filter;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(SELECTED_TYPE_EXTRA_KEY, selectedType);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }));

        // MARK: - DONE BUTTON ------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedType.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(SELECTED_TYPE_EXTRA_KEY, selectedType);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    ToastUtils.showMessage(getString(R.string.select_option_title));
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
}
