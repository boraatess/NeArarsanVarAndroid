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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.List;

import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.ads.activities.AdDetailsActivity;
import com.application.android.neararsanvar.common.fragments.BaseFragment;
import com.application.android.neararsanvar.home.adapters.MyLikesAdapter;
import com.application.android.neararsanvar.utils.ToastUtils;

public class MyLikesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    //private LinearLayout noLikesRL;
    private RecyclerView likesRV;
    //private AdView adView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_my_likes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if (ParseUser.getCurrentUser().getUsername() == null) {
        //    return;
        //}
        //likesRV.getLayoutManager().smoothScrollToPosition(likesRV,new RecyclerView.State(), likesRV.getAdapter().getItemCount());

        initViews();

        // Call query
       // queryLikes();


        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                queryLikes();
            }
        });

        // Init AdMob banner
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);
    }

    private void initViews() {
        //noLikesRL = getActivity().findViewById(R.id.mlNoLikesLayout);
        //adView = getActivity().findViewById(R.id.admobBanner);
        likesRV = getActivity().findViewById(R.id.fml_likes_rv);
    }

    // MARK: - QUERY ADS ------------------------------------------------------------------
    void queryLikes() {
        //likesRV.getLayoutManager().smoothScrollToPosition(likesRV,new RecyclerView.State(), likesRV.getAdapter().getItemCount());
        //likesRV.smoothScrollToPosition(getId());
        //noLikesRL.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        // Launch query
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.ADS_CLASS_NAME);
        query.whereEqualTo(Configs.ADS_IS_REPORTED, false);
        query.orderByDescending(Configs.LIKES_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                Collections.shuffle(objects);
                if (error == null) {
                    hideLoading();
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);

                    // Show/hide noLikesView
                    //if (objects.size() == 0) {
                    //    noLikesRL.setVisibility(View.VISIBLE);
                    //} else {
                        //noLikesRL.setVisibility(View.GONE);
                        setUpLikedAds(objects);
                    //}
                } else {
                    showLoading(getActivity());
                    ToastUtils.showMessage(error.getMessage());
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setUpLikedAds(List<ParseObject> adObjects) {
        likesRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        likesRV.setAdapter(new MyLikesAdapter(adObjects, new MyLikesAdapter.MyLikesClickListener() {
            @Override
            public void onAdClicked(String adObjId) {

                Intent adDetailsIntent = new Intent(getActivity(), AdDetailsActivity.class);
                adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObjId);
                startActivity(adDetailsIntent);
            }

            //@Override
            //public void onUnlikeClicked(ParseObject likeObject, final ParseObject adObject) {
            //    showLoading(getActivity());
            //    likeObject.deleteInBackground(new DeleteCallback() {
            //        @Override
            //        public void done(ParseException e) {
            //           // hideLoading();
//
            //         /*   // Decrement likes for the adObj
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
            //            queryLikes();*/
            //        }
            //    });
            //}
        }));
    }

    @Override
    public void onRefresh() {
        queryLikes();
    }

    //RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(position) {
    //    @Override protected int getVerticalSnapPreference() {
    //        return LinearSmoothScroller.SNAP_TO_START;
    //    }
    //};

}
