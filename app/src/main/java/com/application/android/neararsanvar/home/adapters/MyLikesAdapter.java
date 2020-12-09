package com.application.android.neararsanvar.home.adapters;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.parse.ParseObject;

import java.util.List;

import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.ImageLoadingUtils;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class MyLikesAdapter extends RecyclerView.Adapter<MyLikesAdapter.MyLikesVH> {

    private List<ParseObject> likesObjects;
    private MyLikesClickListener myLikesClickListener;

    public MyLikesAdapter(List<ParseObject> likesObjects, MyLikesClickListener myLikesClickListener) {
        this.likesObjects = likesObjects;
        this.myLikesClickListener = myLikesClickListener;
    }

    @NonNull
    @Override
    public MyLikesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_my_likes, parent, false);
        return new MyLikesVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLikesVH holder, int position) {
        ParseObject likeObj = likesObjects.get(position);
        holder.bind(likeObj);
    }

    @Override
    public int getItemCount() {
        return likesObjects != null ? likesObjects.size() : 0;
    }

    class MyLikesVH extends RecyclerView.ViewHolder {

        private RelativeLayout adRL;
        private ImageView imageIV;
        //private TextView priceTV;
        //private TextView titleTV;
        //private ImageView unlikeIV;

        public MyLikesVH(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            adRL = itemView.findViewById(R.id.iml_ad_rl);
            imageIV = itemView.findViewById(R.id.iml_image_iv);
            //priceTV = itemView.findViewById(R.id.iml_price_tv);
            //titleTV = itemView.findViewById(R.id.iml_title_tv);
            //unlikeIV = itemView.findViewById(R.id.iml_remove_iv);

            //titleTV.setTypeface(Configs.titRegular);
            //priceTV.setTypeface(Configs.titRegular);
        }

        private void bind(ParseObject likeObj) {
            loadAdObject(likeObj);
        }

        private void loadAdObject(final ParseObject likeObj) {


            ImageLoadingUtils.loadImage(likeObj, Configs.ADS_IMAGE1, new ImageLoadingUtils.OnImageLoadListener() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    imageIV.setImageBitmap(bitmap);
                }

                @Override
                public void onImageLoadingError() {
                    imageIV.setImageResource(R.drawable.image_placeholder);
                }
            });


            if (likeObj.getBoolean(Configs.ADS_IS_REPORTED)) {
                setUpReportedAd();
            } else {

                adRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myLikesClickListener != null) {
                            myLikesClickListener.onAdClicked(likeObj.getObjectId());
                        }
                    }
                });
            }






            //TODO : CHANGES COMMENT

            /*likeObj.getParseObject(Configs.ADS_IS_REPORTED).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        unlikeIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (myLikesClickListener != null) {
                                    ParseObject likeObj = likesObjects.get(getAdapterPosition());
                                    myLikesClickListener.onUnlikeClicked(likeObj, object);
                                }
                            }
                        });
                        if (object.getBoolean(Configs.ADS_IS_REPORTED)) {
                            setUpReportedAd();
                        } else {
                            setUpAd(object);
                        }
                    } else {
                        setUpReportedAd();
                    }
                }
            });*/
        }

        //TODO : CHANGES COMMENT
        //private void setUpAd(final ParseObject adObject) {
        //    adRL.setEnabled(true);
        //    titleTV.setText(adObject.getString(Configs.ADS_TITLE));
        //    priceTV.setText(adObject.getString(Configs.ADS_CURRENCY) + String.valueOf(adObject.getNumber(Configs.ADS_PRICE)));
//
        //    //TODO : CHANGES COMMENT
        //   /* adRL.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            if (myLikesClickListener != null) {
        //                myLikesClickListener.onAdClicked(adObject.getObjectId());
        //            }
        //        }
        //    });*/
//
        //    //TODO : CHANGES COMMENT
        //    /*unlikeIV.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            if (myLikesClickListener != null) {
        //                ParseObject likeObj = likesObjects.get(getAdapterPosition());
        //                myLikesClickListener.onUnlikeClicked(likeObj, adObject);
        //            }
        //        }
        //    });*/
//
        //    ImageLoadingUtils.loadImage(adObject, Configs.ADS_IMAGE1, new ImageLoadingUtils.OnImageLoadListener() {
        //        @Override
        //        public void onImageLoaded(Bitmap bitmap) {
        //            imageIV.setImageBitmap(bitmap);
        //        }
//
        //        @Override
        //        public void onImageLoadingError() {
        //            imageIV.setImageResource(R.drawable.image_placeholder);
        //        }
        //    });
        //}

        //TODO : CHANGES COMMENT
        private void setUpReportedAd() {
            imageIV.setImageResource(R.drawable.report_image);
            //titleTV.setText(R.string.not_available_text_placeholder);
            //priceTV.setText(R.string.not_available_text_placeholder);
            adRL.setEnabled(false);
        }
    }

    public interface MyLikesClickListener {

        void onAdClicked(String adObjId);

        //void onUnlikeClicked(ParseObject likeObject, ParseObject adObject);
    }
}
