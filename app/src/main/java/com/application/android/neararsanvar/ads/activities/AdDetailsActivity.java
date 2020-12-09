package com.application.android.neararsanvar.ads.activities;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.application.android.neararsanvar.FullScreenPreview;
import com.application.android.neararsanvar.InboxActivity;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.ReportAdOrUserActivity;
import com.application.android.neararsanvar.SendFeedback;
import com.application.android.neararsanvar.UserProfile;
import com.application.android.neararsanvar.ads.adapters.AdImagesPagerAdapter;
import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.filters.models.ReportType;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.FileUtils;
import com.application.android.neararsanvar.utils.ToastUtils;

public class AdDetailsActivity extends BaseActivity {

    public static final String AD_OBJ_ID_KEY = "AD_OBJ_ID_KEY";
    private static final int[] dotsDrawableRes = {R.drawable.dots1, R.drawable.dots2, R.drawable.dots3};

    private ImageView likeIV;
    private ViewPager imagesVP;
    private ImageView dotsIV;
    private TextView likesTV;
    private TextView dateTV;
    private TextView titleTV;
    private TextView priceTV;
    private TextView descriptionTV;
    private TextView conditionTV;
    private TextView categoryTV;
    private TextView subcategoryTV;
    //private RelativeLayout subcategoryRL;
    //private View subcategorySeparatorView;
    private TextView cityTV;
    private ImageView avatarIV;
    //private TextView usernameTV;
    //private TextView publishedTV;
    //private TextView verifiedTV;
    private TextView sendmessageTV;
    private TextView feedbackTV;
    //private ImageView chatIV;
    private ImageView backIV;
    private Button optionsBtn;
    //private TextView playVideoTV;
    private TextView toolbarTitle;
    private ImageView giftImage;

    private AdImagesPagerAdapter imagesAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();

    /* Variables */
    private ParseObject adObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        Bundle extras = getIntent().getExtras();
        assert extras != null;

        initViews();

        String objectID = extras.getString(AD_OBJ_ID_KEY);
        adObj = ParseObject.createWithoutData(Configs.ADS_CLASS_NAME, objectID);

            try {
                adObj.fetchIfNeeded().getParseObject(Configs.ADS_CLASS_NAME);

            adObj.getParseObject(Configs.ADS_SELLER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject sellerPointer, ParseException e) {
                    if (ParseUser.getCurrentUser().getObjectId() != null){
                        if (sellerPointer.getObjectId().matches(ParseUser.getCurrentUser().getObjectId())){
                            feedbackTV.setVisibility(View.GONE);
                            sendmessageTV.setVisibility(View.GONE);
                        }else {
                            feedbackTV.setVisibility(View.VISIBLE);
                            sendmessageTV.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            // Call queries
            showAdDetails();
            queryIfYouLikedThisAd();

            // MARK: - OPTIONS BUTTON ------------------------------------
            optionsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdDetailsActivity.this);
                    alert.setMessage(R.string.ad_details_options_alert_title)
                            .setTitle(R.string.app_name)
                            .setPositiveButton(R.string.ad_details_report_ad_option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Pass objectID to the other Activity
                                    Intent in = new Intent(AdDetailsActivity.this, ReportAdOrUserActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString(ReportAdOrUserActivity.AD_OBJECT_ID_EXTRA_KEY, adObj.getObjectId());
                                    extras.putString(ReportAdOrUserActivity.REPORT_TYPE_EXTRA_KEY, ReportType.AD.getValue());
                                    in.putExtras(extras);
                                    startActivity(in);
                                }
                            })
                            .setNegativeButton(R.string.ad_details_share_option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Bitmap firstImageBmp = imagesAdapter.getFirstImageBmp();

                                    File imageFile = FileUtils.createCachedFileToShare(firstImageBmp);
                                    Uri fileUri = FileUtils.getUri(imageFile);

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ad_details_share_description, adObj.getString(Configs.ADS_TITLE)));
                                    startActivity(Intent.createChooser(intent, getString(R.string.ad_details_share_chooser_title)));
                                }
                            })
                            .setNeutralButton(getString(R.string.alert_cancel_button), null)
                            .setIcon(R.drawable.neararsanvar);
                    alert.create().show();
                }
            });

            // MARK: - CHAT WITH SELLER BUTTON ------------------------------------
            //chatIV.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View view) {
            //        if (ParseUser.getCurrentUser().getUsername() != null) {
//
            //            // Get sellerPointer
            //            adObj.getParseObject(Configs.ADS_SELLER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            //                public void done(ParseObject sellerPointer, ParseException e) {
            //                    List<String> hasBlocked = sellerPointer.getList(Configs.USER_HAS_BLOCKED);
//
            //                    // Seller has blocked you
            //                    if (hasBlocked.contains(ParseUser.getCurrentUser().getObjectId())) {
            //                        Configs.simpleAlert(getString(R.string.ad_details_user_block_message,
            //                                sellerPointer.getString(Configs.USER_USERNAME)), AdDetailsActivity.this);
//
            //                    } else {
            //                        // Pass objectID to the other Activity
            //                        Intent i = new Intent(AdDetailsActivity.this, InboxActivity.class);
            //                        Bundle extras = new Bundle();
            //                        extras.putString("adObjectID", adObj.getObjectId());
            //                        extras.putString("sellerObjectID", sellerPointer.getObjectId());
            //                        i.putExtras(extras);
            //                        startActivity(i);
            //                    }
            //                }
            //            });
//
            //        } else {
            //            Configs.loginAlert(getString(R.string.ad_details_chat_error), AdDetailsActivity.this);
            //        }
            //    }
            //});


            // MARK: - LIKE AD BUTTON ------------------------------------
            likeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ParseUser.getCurrentUser().getUsername() != null) {

                        showLoading(AdDetailsActivity.this);

                        final ParseUser currUser = ParseUser.getCurrentUser();


                        // 1. CHECK IF YOU'VE ALREADY LIKED THIS AD
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.LIKES_CLASS_NAME);
                        query.whereEqualTo(Configs.LIKES_CURR_USER, currUser);
                        query.whereEqualTo(Configs.LIKES_AD_LIKED, adObj);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException error) {
                                if (error == null) {


                                    // 2. LIKE THIS AD!
                                    if (objects.size() == 0) {
                                        ParseObject likeObj = new ParseObject(Configs.LIKES_CLASS_NAME);

                                        // Save data
                                        likeObj.put(Configs.LIKES_CURR_USER, currUser);
                                        likeObj.put(Configs.LIKES_AD_LIKED, adObj);
                                        likeObj.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    likeIV.setImageResource(R.drawable.ad_liked);
                                                    hideLoading();

                                                    // Increment likes for the adObj
                                                    adObj.increment(Configs.ADS_LIKES, 1);

                                                    // Add the user's objectID
                                                    if (adObj.getList(Configs.ADS_LIKED_BY) != null) {
                                                        List<String> likedByArr = adObj.getList(Configs.ADS_LIKED_BY);
                                                        likedByArr.add(currUser.getObjectId());
                                                        adObj.put(Configs.ADS_LIKED_BY, likedByArr);
                                                    } else {
                                                        List<String> likedByArr = new ArrayList<String>();
                                                        likedByArr.add(currUser.getObjectId());
                                                        adObj.put(Configs.ADS_LIKED_BY, likedByArr);
                                                    }
                                                    adObj.saveInBackground();

                                                    // Set likes number in the cell
                                                    int likesNr = (int) adObj.getNumber(Configs.ADS_LIKES);
                                                    likesTV.setText(String.valueOf(likesNr) + " like");


                                                    // Send push notification
                                                    final ParseUser sellerPointer = (ParseUser) adObj.get(Configs.ADS_SELLER_POINTER);

                                                    final String pushMessage = getString(R.string.ad_details_user_liked_your_ad, currUser.getUsername(), adObj.getString(Configs.ADS_TITLE));

                                                    HashMap<String, Object> params = new HashMap<String, Object>();
                                                    params.put("someKey", sellerPointer.getObjectId());
                                                    params.put("data", pushMessage);

                                                    ParseCloud.callFunctionInBackground("pushAndroid", params, new FunctionCallback<String>() {
                                                        @Override
                                                        public void done(String object, ParseException e) {
                                                            if (e == null) {
                                                                Log.i("log-", "PUSH SENT TO: " + sellerPointer.getUsername()
                                                                        + "\nMESSAGE: "
                                                                        + pushMessage);

                                                                // Error in Cloud Code
                                                            } else {
                                                                hideLoading();
                                                                Configs.simpleAlert(e.getMessage(), AdDetailsActivity.this);
                                                            }
                                                        }
                                                    });


                                                    // Save Activity
                                                    ParseObject actObj = new ParseObject(Configs.ACTIVITY_CLASS_NAME);
                                                    actObj.put(Configs.ACTIVITY_CURRENT_USER, sellerPointer);
                                                    actObj.put(Configs.ACTIVITY_OTHER_USER, currUser);
                                                    actObj.put(Configs.ACTIVITY_TEXT, pushMessage);
                                                    actObj.saveInBackground();


                                                    // error on saving like
                                                } else {
                                                    hideLoading();
                                                    Configs.simpleAlert(e.getMessage(), AdDetailsActivity.this);
                                                }
                                            }
                                        });


                                        // 3. UNLIKE THIS AD :(
                                    } else {
                                        ParseObject likeObj = new ParseObject(Configs.LIKES_CLASS_NAME);
                                        likeObj = objects.get(0);
                                        likeObj.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    likeIV.setImageResource(R.drawable.no_like);
                                                    hideLoading();
                                                    // Decrement likes for the adObj
                                                    adObj.increment(Configs.ADS_LIKES, -1);

                                                    // Remove the user's objectID
                                                    List<String> likedByArr = adObj.getList(Configs.ADS_LIKED_BY);
                                                    likedByArr.remove(currUser.getObjectId());
                                                    adObj.put(Configs.ADS_LIKED_BY, likedByArr);
                                                    adObj.saveInBackground();
                                                }
                                            }
                                        });
                                    }


                                    // error in query
                                } else {
                                    hideLoading();
                                    Configs.simpleAlert(error.getMessage(), AdDetailsActivity.this);
                                }

                            }
                        });// end query for Likes


                        // YOU'RE NOT LOGGED IN!
                    } else {
                        Configs.loginAlert(getString(R.string.ad_details_like_ad_error), AdDetailsActivity.this);
                    }

                }
            });


        } catch (ParseException e) {
            e.printStackTrace();
        }

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        likeIV = findViewById(R.id.aad_like_iv);
       // imagesVP = findViewById(R.id.aad_images_vp);
        dotsIV = findViewById(R.id.aad_dots_iv);
        likesTV = findViewById(R.id.aad_likes_tv);
        //dateTV = findViewById(R.id.aad_date_tv);
        //titleTV = findViewById(R.id.aad_title_tv);
        priceTV = findViewById(R.id.aad_price_tv);
        toolbarTitle = findViewById(R.id.aad_toolbar_title_tv);
        titleTV = findViewById(R.id.aad_title_tv);
        descriptionTV = findViewById(R.id.aad_description_tv);
        categoryTV = findViewById(R.id.aad_category_tv);
        subcategoryTV = findViewById(R.id.aad_subcategory_tv);
        //subcategoryRL = findViewById(R.id.aad_subcategory_rl);
        conditionTV = findViewById(R.id.aad_condition_tv);
        //subcategorySeparatorView = findViewById(R.id.aad_subcategory_separator_view);
        cityTV = findViewById(R.id.aad_city_tv);
        avatarIV = findViewById(R.id.aad_avatar_iv);
        //usernameTV = findViewById(R.id.aad_username_tv);
        dateTV = findViewById(R.id.aad_date_tv);
        //verifiedTV = findViewById(R.id.aad_verified_tv);
        sendmessageTV = findViewById(R.id.aad_sendmessage_tv);
        feedbackTV = findViewById(R.id.aad_feedback_tv);
        //chatIV = findViewById(R.id.aad_chat_iv);
        backIV = findViewById(R.id.aad_back_iv);
        optionsBtn = findViewById(R.id.aad_options_btn);
        //playVideoTV = findViewById(R.id.aad_play_video_tv);

        giftImage = findViewById(R.id.gift);
    }

    // MARK: - QUERY IF YOU'VE LIKED THIS AD ------------------------------------------------------
    void queryIfYouLikedThisAd() {
        if (ParseUser.getCurrentUser().getUsername() != null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.LIKES_CLASS_NAME);
            query.whereEqualTo(Configs.LIKES_CURR_USER, ParseUser.getCurrentUser());
            query.whereEqualTo(Configs.LIKES_AD_LIKED, adObj);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException error) {
                    if (error == null) {
                        if (objects.size() != 0) {
                            likeIV.setImageResource(R.drawable.ad_liked);
                            likesTV.setText(String.valueOf(adObj.getNumber(Configs.ADS_LIKES)) + " like");
                        } else {
                            likeIV.setImageResource(R.drawable.no_like);
                        }

                        // error
                    } else {
                        ToastUtils.showMessage(error.getMessage());
                    }
                }
            });
        }
    }

    // MARK: - SHOW AD DETAILS ---------------------------------------------------------------------
    void showAdDetails() {

        if (adObj.getParseFile(Configs.ADS_IMAGE1) !=null){
            arrayList.add("image1");
        }

        if (adObj.getParseFile(Configs.ADS_IMAGE2) !=null){
            arrayList.add("image2");
        }

        if (adObj.getParseFile(Configs.ADS_IMAGE3) !=null){
            arrayList.add("image3");
        }

        imagesAdapter = new AdImagesPagerAdapter(adObj, new AdImagesPagerAdapter.OnImageClickListener() {
            @Override
            public void onImageClicked(int pos,String imageFieldKey) {
                openImageFullScreen(pos,imageFieldKey);
            }
        },arrayList);

        //sadsad
        imagesVP.setAdapter(imagesAdapter);
        imagesVP.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position < dotsDrawableRes.length) {
                    dotsIV.setImageResource(dotsDrawableRes[position]);
                }
            }
        });


        // Set likes number
        int likesNr = (int) adObj.getNumber(Configs.ADS_LIKES);
        likesTV.setText(String.valueOf(likesNr) + " like");

        // Get title
        //titleTV.setTypeface(Configs.titRegular);
        //titleTV.setText(adObj.getString(Configs.ADS_TITLE));

        // Get date
        //dateTV.setTypeface(Configs.titRegular);
        //dateTV.setText(Configs.timeAgoSinceDate(adObj.getCreatedAt()));

        // Get price
        priceTV.setTypeface(Configs.titSemibold);


        if (adObj.getString(Configs.ADS_CONDITION).equalsIgnoreCase("Gift")) {
            giftImage.setVisibility(View.VISIBLE);
            giftImage.setImageResource(R.drawable.gift);
        } else if(adObj.getString(Configs.ADS_CONDITION).equalsIgnoreCase("Exchange")) {
            giftImage.setVisibility(View.VISIBLE);
            giftImage.setImageResource(R.drawable.exchange);
        }else {
            giftImage.setVisibility(View.GONE);
            priceTV.setText(adObj.getString(Configs.ADS_CURRENCY) + " " + String.valueOf(adObj.getNumber(Configs.ADS_PRICE)));
        }


        // Get condition
        descriptionTV.setTypeface(Configs.titRegular);
        descriptionTV.setText(adObj.getString(Configs.ADS_DESCRIPTION));

        // Set Toolbar Title
        toolbarTitle.setTypeface(Configs.titSemibold);
        toolbarTitle.setText(adObj.getString(Configs.ADS_TITLE));

        // Get category
        categoryTV.setTypeface(Configs.titRegular);
        categoryTV.setText(adObj.getString(Configs.ADS_CATEGORY));

        // Get subcategory
        subcategoryTV.setTypeface(Configs.titRegular);
        if (adObj.getString(Configs.ADS_SUBCATEGORY) == "" || adObj.getString(Configs.ADS_SUBCATEGORY) == null ){
            subcategoryTV.setText("N/A");
        }
        else {
            subcategoryTV.setText(adObj.getString(Configs.ADS_SUBCATEGORY));
        }
        // Get type
        conditionTV.setTypeface(Configs.titRegular);
        conditionTV.setText(adObj.getString(Configs.ADS_CONDITION));

        // Get city
        cityTV.setTypeface(Configs.titRegular);
        cityTV.setText(adObj.getString(Configs.ADS_CITIES));


        //TODO : CHANGES COMMENT
        // Get subcategory
        /*String subcategory = adObj.getString(Configs.ADS_SUBCATEGORY);
        if (TextUtils.isEmpty(subcategory)) {
            //subcategoryRL.setVisibility(View.GONE);
            //subcategorySeparatorView.setVisibility(View.GONE);
        } else {
            //subcategoryRL.setVisibility(View.VISIBLE);
            subcategorySeparatorView.setVisibility(View.VISIBLE);
            subcategoryTV.setText(subcategory);
        }*/


        //TODO : CHANGES COMMENT
        // Get Location (City, Country)
        // ParseGeoPoint gp = new ParseGeoPoint(adObj.getParseGeoPoint(Configs.ADS_LOCATION));
        // Location adLocation = new Location("dummyprovider");
        //adLocation.setLatitude(gp.getLatitude());
        //adLocation.setLongitude(gp.getLongitude());

        /*try {
            Geocoder geocoder = new Geocoder(AdDetailsActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(adLocation.getLatitude(), adLocation.getLongitude(), 1);
            if (Geocoder.isPresent()) {
                Address returnAddress = addresses.get(0);
                String city = returnAddress.getLocality();
                String country = returnAddress.getCountryName();

                if (city == null) {
                    city = "";
                }

                // Show City/Country
                locationTV.setTypeface(Configs.titRegular);
                locationTV.setText(city + ", " + country);

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.geocoder_not_present_error), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Configs.simpleAlert(e.getMessage(), AdDetailsActivity.this);
        }*/


        // Get video
        //playVideoTV.setTypeface(Configs.titSemibold);
        //if (adObj.get(Configs.ADS_VIDEO) != null) {
        //    playVideoTV.setText(getString(R.string.ad_details_video_watch_button));
        //    playVideoTV.setEnabled(true);
        //} else {
        //    playVideoTV.setText(R.string.ad_details_video_not_available);
        //    playVideoTV.setEnabled(false);
        //}
//
        //// Show it in the WatchVideo screen
        //playVideoTV.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        // Pass objectID to the other Activity
        //        Intent i = new Intent(AdDetailsActivity.this, WatchVideo.class);
        //        Bundle extras = new Bundle();
        //        extras.putString("objectID", adObj.getObjectId());
        //        i.putExtras(extras);
        //        startActivity(i);
        //    }
        //});


        // Get description
        titleTV.setTypeface(Configs.titSemibold);
        titleTV.setText(adObj.getString(Configs.ADS_TITLE));


        // SELLERS DETAILS -------------------------------------

        // Get userPointer

        adObj.getParseObject(Configs.ADS_SELLER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject sellerPointer, ParseException e) {

                // Get Avatar
                Configs.getParseImage(avatarIV, sellerPointer, Configs.USER_AVATAR);

                // TAP ON AVATAR -> SHOW USER PROFILE
                avatarIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Pass objectID to the other Activity
                        Intent i = new Intent(AdDetailsActivity.this, UserProfile.class);
                        Bundle extras = new Bundle();
                        extras.putString("objectID", sellerPointer.getObjectId());
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });


                // Get username
                try {
                    //usernameTV.setTypeface(Configs.titRegular);
                    //usernameTV.setText(sellerPointer.getString(Configs.USER_USERNAME));

                    // Get joined
                    dateTV.setTypeface(Configs.titRegular);
                    dateTV.setText(Configs.timeAgoSinceDate(adObj.getCreatedAt()));

                    // Get verified
                    //verifiedTV.setTypeface(Configs.titRegular);
                    //if (sellerPointer.get(Configs.USER_EMAIL_VERIFIED) != null) {
                    //    if (sellerPointer.getBoolean(Configs.USER_EMAIL_VERIFIED)) {
                    //        verifiedTV.setText(getString(R.string.account_verified));
                    //    } else {
                    //        verifiedTV.setText(getString(R.string.account_not_verified));
                    //    }
                    //} else {
                    //    verifiedTV.setText(getString(R.string.account_not_verified));
                    //}
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                // MARK: - COMMENTS BUTTON ------------------------------------
                sendmessageTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ParseUser.getCurrentUser().getUsername() != null) {

                            // Get sellerPointer
                            adObj.getParseObject(Configs.ADS_SELLER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject sellerPointer, ParseException e) {
                                    List<String> hasBlocked = sellerPointer.getList(Configs.USER_HAS_BLOCKED);

                                    // Seller has blocked you
                                    if (hasBlocked.contains(ParseUser.getCurrentUser().getObjectId())) {
                                        Configs.simpleAlert(getString(R.string.ad_details_user_block_message,
                                                sellerPointer.getString(Configs.USER_USERNAME)), AdDetailsActivity.this);

                                    } else {
                                        // Pass objectID to the other Activity
                                        Intent i = new Intent(AdDetailsActivity.this, InboxActivity.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("adObjectID", adObj.getObjectId());
                                        extras.putString("sellerObjectID", sellerPointer.getObjectId());
                                        i.putExtras(extras);
                                        startActivity(i);
                                    }
                                }
                            });

                        } else {
                            Configs.loginAlert(getString(R.string.ad_details_chat_error), AdDetailsActivity.this);
                        }
                    }
                });


                // MARK: - SEND FEEDBACK BUTTON ------------------------------------
                feedbackTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ParseUser.getCurrentUser().getUsername() != null) {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.FEEDBACKS_CLASS_NAME);
                            query.whereEqualTo(Configs.FEEDBACKS_REVIEWER_POINTER, ParseUser.getCurrentUser());
                            query.whereEqualTo(Configs.FEEDBACKS_SELLER_POINTER, sellerPointer);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        // Enter Send Feedback screen
                                        if (objects.size() == 0) {
                                            Intent i = new Intent(AdDetailsActivity.this, SendFeedback.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("objectID", adObj.getObjectId());
                                            extras.putString("sellerID", sellerPointer.getObjectId());
                                            i.putExtras(extras);
                                            startActivity(i);

                                            // You already sent a Feedback!
                                        } else {
                                            Configs.simpleAlert(getString(R.string.ad_details_feedback_already_sent_error), AdDetailsActivity.this);
                                        }

                                        // error in query
                                    } else {
                                        Configs.simpleAlert(e.getMessage(), AdDetailsActivity.this);
                                    }
                                }
                            });


                        } else {
                            Configs.loginAlert(getString(R.string.ad_details_comment_login_error), AdDetailsActivity.this);
                        }
                    }
                });


            }
        });// end sellerPointer
    }

    // OPEN TAPPED IMAGE INTO FULL SCREEN ACTIVITY
    void openImageFullScreen(int pos,String imageName) {

        Intent i = new Intent(AdDetailsActivity.this, FullScreenPreview.class);
        Bundle extras = new Bundle();
        extras.putString("imageName", imageName);
        extras.putString("objectID", adObj.getObjectId());
        extras.putInt("position", pos);
        //extras.putString("object", String.valueOf(adObj));
        i.putExtras(extras);
        startActivity(i);
    }
}
