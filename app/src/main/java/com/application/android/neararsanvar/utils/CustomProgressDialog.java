package com.application.android.neararsanvar.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.application.android.neararsanvar.R;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class CustomProgressDialog extends Dialog {
    Context context;
    private ImageView image;

    public CustomProgressDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_progress_dialog);
        setCancelable(false);
        image = findViewById(R.id.image);

        Glide.with(context)
                .load(R.drawable.load)
                .into(image);
    }
}