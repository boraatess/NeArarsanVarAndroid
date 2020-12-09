package com.application.android.neararsanvar;


import android.content.pm.ActivityInfo;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;

import com.application.android.neararsanvar.ads.adapters.AdImagesPagerFullScreenAdapter;
import com.application.android.neararsanvar.utils.Configs;

public class FullScreenPreview extends AppCompatActivity {

    /* Views */
    ImageView prevImage;

    /* Variables */
    ParseObject adObj;

    private ViewPager imagesVP;
    private AdImagesPagerFullScreenAdapter imagesAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_preview);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imagesVP = findViewById(R.id.viewPagerImage);
        Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("objectID");

        adObj = ParseObject.createWithoutData(Configs.ADS_CLASS_NAME, objectID);
        try {
            adObj.fetchIfNeeded().getParseObject(Configs.ADS_CLASS_NAME);


            if (adObj.getParseFile(Configs.ADS_IMAGE1) !=null){
                arrayList.add("image1");
            }

            if (adObj.getParseFile(Configs.ADS_IMAGE2) !=null){
                arrayList.add("image2");
            }

            if (adObj.getParseFile(Configs.ADS_IMAGE3) !=null){
                arrayList.add("image3");
            }


            imagesAdapter = new AdImagesPagerFullScreenAdapter(adObj, new AdImagesPagerFullScreenAdapter.OnImageClickListener() {
                @Override
                public void onImageClicked(String imageFieldKey) {
                    //  openImageFullScreen(imageFieldKey);
                }
            },arrayList);

            imagesVP.setAdapter(imagesAdapter);
            imagesVP.setCurrentItem(extras.getInt("position"));
            imagesVP.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }







       /* // Init views
        prevImage = findViewById(R.id.fspImage);
        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("objectID");
        String imageName = extras.getString("imageName");
        adObj = ParseObject.createWithoutData(Configs.ADS_CLASS_NAME, objectID);
        try {
            adObj.fetchIfNeeded().getParseObject(Configs.ADS_CLASS_NAME);

            ParseFile fileObject = null;

            switch (imageName) {
                case "image1":
                    fileObject = adObj.getParseFile(Configs.ADS_IMAGE1);
                    break;
                case "image2":
                    fileObject = adObj.getParseFile(Configs.ADS_IMAGE2);
                    break;
                case "image3":
                    fileObject = adObj.getParseFile(Configs.ADS_IMAGE3);
                    break;
            }

            if (fileObject != null) {
                fileObject.getDataInBackground(new GetDataCallback() {
                    public void done(byte[] data, ParseException error) {
                        if (error == null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            if (bmp != null) {
                                prevImage.setImageBitmap(bmp);

                                // Attach image to PhotoViewAttacher to zoom image with pinch gesture and tap to close
                                PhotoViewAttacher pAttacher;
                                pAttacher = new PhotoViewAttacher(prevImage);
                                pAttacher.update();

                                pAttacher.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                                    @Override
                                    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                                        finish();
                                        return false;
                                    }

                                    @Override
                                    public boolean onDoubleTap(MotionEvent motionEvent) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                                        return false;
                                    }
                                });
                            }
                        }
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }// end onCreate()
}//@end
