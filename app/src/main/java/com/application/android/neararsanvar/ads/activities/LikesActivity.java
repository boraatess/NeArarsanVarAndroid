package com.application.android.neararsanvar.ads.activities;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.home.adapters.MyLikesActivityAdapter;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.ToastUtils;

public class LikesActivity extends AppCompatActivity {

    private RelativeLayout noLikesRL;
    private AdView adView;
    private RecyclerView likesRV;
    private Activity activity;
    private AlertDialog progressDialog;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_likes);

        if (ParseUser.getCurrentUser().getUsername() == null) {
            return;
        }

        initViews();

        // Call query
        queryLikes();

        // Init AdMob banner
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void initViews() {
        noLikesRL = findViewById(R.id.mlNoLikesLayout);
        adView = findViewById(R.id.admobBanner);
        likesRV = findViewById(R.id.likes_rv);
        back = findViewById(R.id.back);
    }





    // MARK: - QUERY ADS ------------------------------------------------------------------
    void queryLikes() {
        noLikesRL.setVisibility(View.GONE);

        // Launch query
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.LIKES_CLASS_NAME);
        query.whereEqualTo(Configs.LIKES_CURR_USER, ParseUser.getCurrentUser());
        query.orderByDescending(Configs.LIKES_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                if (error == null) {
                    hideLoading();

                    // Show/hide noLikesView
                    if (objects.size() == 0) {
                        noLikesRL.setVisibility(View.VISIBLE);
                    } else {
                        noLikesRL.setVisibility(View.GONE);
                        setUpLikedAds(objects);
                    }
                } else {
                    showLoading();
                    ToastUtils.showMessage(error.getMessage());
                }
            }
        });
    }




    private void setUpLikedAds(List<ParseObject> adObjects) {
        likesRV.setLayoutManager(new GridLayoutManager(activity, 3));
        likesRV.setAdapter(new MyLikesActivityAdapter(adObjects, new MyLikesActivityAdapter.MyLikesClickListener() {
            @Override
            public void onAdClicked(String adObjId) {
                Intent adDetailsIntent = new Intent(activity, AdDetailsActivity.class);
                adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObjId);
                startActivity(adDetailsIntent);
            }

            //@Override
            //public void onUnlikeClicked(ParseObject likeObject, final ParseObject adObject) {
            //    showLoading();
            //    likeObject.deleteInBackground(new DeleteCallback() {
            //        @Override
            //        public void done(ParseException e) {
            //            hideLoading();
//
            //            // Decrement likes for the adObj
            //            adObject.increment(Configs.ADS_LIKES, -1);
            //            ParseUser currUser = ParseUser.getCurrentUser();
//
            //            // Remove the user's objectID
            //            List<String> likedByArr = adObject.getList(Configs.ADS_LIKED_BY);
            //            likedByArr.remove(currUser.getObjectId());
            //            adObject.put(Configs.ADS_LIKED_BY, likedByArr);
            //            adObject.saveInBackground();
//
            //            // Recall query
            //            queryLikes();
            //        }
            //    });
            //}
        }));
    }




    protected void showLoading() {
        progressDialog = Configs.buildProgressLoadingDialog(activity);
        progressDialog.show();
    }


    protected void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
