package com.application.android.neararsanvar.common.fragments;

import android.app.Activity;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.application.android.neararsanvar.utils.CustomProgressDialog;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class BaseFragment extends Fragment {

    //private AlertDialog progressDialog;

   /* protected void showLoading() {
        progressDialog = Configs.buildProgressLoadingDialog(getActivity());
        progressDialog.show();
    }

    protected void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }*/


    static CustomProgressDialog dialog;
    public static void showLoading(Activity activity) {
        dialog = new CustomProgressDialog(activity);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }



    public static void hideLoading() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        else
            Log.i("Dialog", "already dismissed");
    }
}
