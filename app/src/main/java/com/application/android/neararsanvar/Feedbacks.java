package com.application.android.neararsanvar;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.content.Context;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import com.application.android.neararsanvar.utils.Configs;

import static com.application.android.neararsanvar.common.activities.BaseActivity.hideLoading;
import static com.application.android.neararsanvar.common.activities.BaseActivity.showLoading;

public class Feedbacks extends AppCompatActivity {

    /* Variables */
    ParseUser userObj;
    List<ParseObject> feedbacksArray;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbacks);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get userObjectID from previous .java
        Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("userObjectID");
        linearLayout = findViewById(R.id.llLayout);
        userObj = (ParseUser) ParseUser.createWithoutData(Configs.USER_CLASS_NAME, objectID);
        try {
            userObj.fetchIfNeeded().getParseObject(Configs.USER_CLASS_NAME);

            // Call query
            queryFeedbacks();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.feedsBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }// end onCreate()


    // MARK: - QUERY FEEDBACKS ------------------------------------------------------------
    void queryFeedbacks() {
        //Configs.showPD(getString(R.string.progress_dialog_loading), Feedbacks.this);
        showLoading(Feedbacks.this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.FEEDBACKS_CLASS_NAME);
        query.whereEqualTo(Configs.FEEDBACKS_SELLER_POINTER, userObj);
        query.addDescendingOrder(Configs.FEEDBACKS_AD_UPDATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException error) {
                if (error == null) {
                    feedbacksArray = objects;
                    //Configs.hidePD();
                    hideLoading();

                    // CUSTOM LIST ADAPTER
                    class ListAdapter extends BaseAdapter {
                        private Context context;

                        public ListAdapter(Context context, List<ParseObject> objects) {
                            super();
                            this.context = context;
                        }

                        // CONFIGURE CELL
                        @Override
                        public View getView(int position, View cell, ViewGroup parent) {
                            if (cell == null) {
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                assert inflater != null;
                                cell = inflater.inflate(R.layout.cell_feedback, null);
                            }
                            final View finalCell = cell;

                            // Get Parse object
                            final ParseObject fObj = feedbacksArray.get(position);

                            // Get reviewerPointer
                            fObj.getParseObject(Configs.FEEDBACKS_REVIEWER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject reviewerPointer, ParseException e) {

                                    // Get Ad title
                                    TextView adTitleTxt = finalCell.findViewById(R.id.feedsAdTitleTxt);
                                    adTitleTxt.setTypeface(Configs.titSemibold);
                                    adTitleTxt.setText(fObj.getString(Configs.FEEDBACKS_AD_TITLE));

                                    // Get Feedback text
                                    TextView feedTxt = finalCell.findViewById(R.id.feedsFeedbackTxt);
                                    feedTxt.setTypeface(Configs.titRegular);
                                    feedTxt.setText(fObj.getString(Configs.FEEDBACKS_TEXT));

                                    // Get Date & Author
                                    TextView byDateTxt = finalCell.findViewById(R.id.feedsByDateTxt);
                                    byDateTxt.setTypeface(Configs.titSemibold);
                                    String dateStr = Configs.timeAgoSinceDate(fObj.getCreatedAt());
                                    byDateTxt.setText(getString(R.string.username_formatted, reviewerPointer.getString(Configs.USER_USERNAME)) + " • " + dateStr);

                                    // Get stars image
                                    ImageView starsImg = finalCell.findViewById(R.id.feedsStarsImg);
                                    int[] starImages = new int[]{R.drawable.star_0, R.drawable.star_1,
                                            R.drawable.star_2, R.drawable.star_3, R.drawable.star_4, R.drawable.star_5
                                    };
                                    if (fObj.getInt(Configs.FEEDBACKS_STARS) == 0) {
                                        starsImg.setImageResource(starImages[0]);
                                    } else {
                                        starsImg.setImageResource(starImages[fObj.getInt(Configs.FEEDBACKS_STARS)]);
                                    }
                                }
                            });// end reviewerPointer

                            return cell;
                        }

                        @Override
                        public int getCount() {
                            return feedbacksArray.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return feedbacksArray.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                    }

                    // Init ListView and set its adapter
                    ListView aList = findViewById(R.id.feedsListView);
                    aList.setAdapter(new ListAdapter(Feedbacks.this, feedbacksArray));

                    if (feedbacksArray.size() > 0){
                        linearLayout.setVisibility(View.GONE);
                    }
                } else {
                    // Error in query
                    //Configs.hidePD();
                    hideLoading();
                    Configs.simpleAlert(error.getMessage(), Feedbacks.this);
                }
            }
        });
    }
}//@end
