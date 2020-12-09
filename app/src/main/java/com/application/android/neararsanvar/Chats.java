package com.application.android.neararsanvar;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.utils.Configs;

public class Chats extends BaseActivity {
    /* Variables */
    List<ParseObject> chatsArray;

    @Override
    protected void onStart() {
        super.onStart();
        // Call query
        queryChats();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.chBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Init AdMob banner
        AdView mAdView = findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }// end onCreate()

    // MARK: - QUERY CHATS ----------------------------------------------------------------------
    void queryChats() {
        showLoading(Chats.this);
        //Configs.showPD(getString(R.string.progress_dialog_loading), Chats.this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.CHATS_CLASS_NAME);
        query.include(Configs.USER_CLASS_NAME);
        query.whereContains(Configs.CHATS_ID, ParseUser.getCurrentUser().getObjectId());
        query.orderByDescending(Configs.CHATS_UPDATEAT);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, final ParseException error) {
                if (error == null) {
                    chatsArray = objects;
                    hideLoading();
                    //Configs.hidePD();

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

                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            //assert inflater != null;
                            cell = inflater.inflate(R.layout.cell_chats, null);

                            final View finalCell = cell;

                            // Get Parse object
                            final ParseObject chatsObj = chatsArray.get(position);

                            // Get adPointer
                            chatsObj.getParseObject(Configs.CHATS_AD_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                public void done(final ParseObject adPointer, ParseException e) {

                                    // Get userPointer
                                    chatsObj.getParseObject(Configs.CHATS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                        public void done(final ParseObject userPointer, ParseException e) {

                                            // Get otherPointer
                                            chatsObj.getParseObject(Configs.CHATS_OTHER_USER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                                public void done(ParseObject otherPointer, ParseException e) {

                                                    // Get Ad Title
                                                   /* TextView adTitleTxt = finalCell.findViewById(R.id.cchatsAdTitleTxt);
                                                    adTitleTxt.setTypeface(Configs.titSemibold);
                                                    adTitleTxt.setText(adPointer.getString(Configs.ADS_TITLE));*/

                                                    // Get Ad Image1
                                                    ImageView adImage = finalCell.findViewById(R.id.cchatsUserImage);


                                                    // Get Sender's username
                                                    TextView senderTxt = finalCell.findViewById(R.id.cchatsSenderTxt);
                                                    senderTxt.setTypeface(Configs.titSemibold);

                                                    if (userPointer.getObjectId().matches(ParseUser.getCurrentUser().getObjectId())) {
                                                        senderTxt.setText(R.string.chats_sent_message_title);
                                                        Configs.getParseImage(adImage, otherPointer, Configs.USER_AVATAR);
                                                    } else {
                                                        Configs.getParseImage(adImage, userPointer, Configs.USER_AVATAR);
                                                        senderTxt.setText(userPointer.getString(Configs.USER_USERNAME));
                                                    }

                                                    // Get date
                                                    TextView dateTxt = finalCell.findViewById(R.id.cchatsDateTxt);
                                                    dateTxt.setTypeface(Configs.titRegular);
                                                    dateTxt.setText(Configs.timeAgoSinceDate(chatsObj.getUpdatedAt()));

                                                    // Get last Message
                                                    TextView lastMessTxt = finalCell.findViewById(R.id.cchatsLastMessTxt);
                                                    lastMessTxt.setTypeface(Configs.titRegular);
                                                    lastMessTxt.setText(chatsObj.getString(Configs.CHATS_LAST_MESSAGE));
                                                }
                                            });// end otherUser
                                        }
                                    });// end userPointer
                                }
                            });// end adPointer
                            return cell;
                        }

                        @Override
                        public int getCount() {
                            return chatsArray.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return chatsArray.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }
                    }

                    // Init ListView and set its adapter
                    ListView aList = findViewById(R.id.chatsListView);
                    aList.setAdapter(new ListAdapter(Chats.this, chatsArray));
                    aList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            final ParseObject chatObj = chatsArray.get(position);

                            // Get adPointer
                            chatObj.getParseObject(Configs.CHATS_AD_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                public void done(final ParseObject adPointer, ParseException e) {
                                    if (e == null) {
                                        // Get userPointer
                                        chatObj.getParseUser(Configs.CHATS_USER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                                            public void done(final ParseUser userPointer, final ParseException e) {

                                                // Get otherPointer
                                                chatObj.getParseUser(Configs.CHATS_OTHER_USER).fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                                                    public void done(final ParseUser otherUser, ParseException e) {

                                                        ParseUser currentUser = ParseUser.getCurrentUser();
                                                        List<String> blockedUsers = otherUser.getList(Configs.USER_HAS_BLOCKED);

                                                        // otherUser user has blocked you
                                                        if (blockedUsers.contains(currentUser.getObjectId())) {
                                                            Configs.simpleAlert(getString(R.string.chats_user_block_error, otherUser.getString(Configs.USER_USERNAME)), Chats.this);
                                                        } else {
                                                            // You can chat with otherUser
                                                            Intent i = new Intent(Chats.this, InboxActivity.class);
                                                            Bundle extras = new Bundle();
                                                            String userID = "";

                                                            if (userPointer.getObjectId().matches(ParseUser.getCurrentUser().getObjectId())) {
                                                                userID = otherUser.getObjectId();
                                                            } else {
                                                                userID = userPointer.getObjectId();
                                                            }

                                                            extras.putString("sellerObjectID", userID);
                                                            extras.putString("adObjectID", adPointer.getObjectId());
                                                            i.putExtras(extras);
                                                            startActivity(i);
                                                        }
                                                    }
                                                });// end otherPointer
                                            }
                                        });// end userPointer
                                    }
                                }
                            });// end adPointer
                        }
                    });
                    // Error in query
                } else {
                    hideLoading();
                    //Configs.hidePD();
                    Configs.simpleAlert(error.getMessage(), Chats.this);
                }
            }
        });
    }
}//@end
