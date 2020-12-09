package com.application.android.neararsanvar.ads.adapters;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.ImageLoadingUtils;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class AdImagesPagerFullScreenAdapter extends PagerAdapter {

    private ParseObject adObject;
    private OnImageClickListener onImageClickListener;
    private Bitmap firstImageBmp = null;
    private ArrayList<String> arrayList = new ArrayList<>();

    public AdImagesPagerFullScreenAdapter(ParseObject adObject, OnImageClickListener onImageClickListener, ArrayList<String> arrayList) {
        this.adObject = adObject;
        this.onImageClickListener = onImageClickListener;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_ad_image_fullscreen, container, false);
       // final AdImages adImage = AdImages.values()[position];
        final ImageView imageIV = layout.findViewById(R.id.iai_image_iv);
        final TextView errorTv = layout.findViewById(R.id.error_tv);


        ImageLoadingUtils.loadImage(adObject, arrayList.get(position), new ImageLoadingUtils.OnImageLoadListener() {
            @Override
            public void onImageLoaded(Bitmap bitmap) {
                if (firstImageBmp == null) {
                    firstImageBmp = bitmap;
                }
                imageIV.setVisibility(View.VISIBLE);
                errorTv.setVisibility(View.GONE);
                imageIV.setImageBitmap(bitmap);
                imageIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onImageClickListener != null) {
                            onImageClickListener.onImageClicked(arrayList.get(position));
                        }
                    }
                });
            }

            @Override
            public void onImageLoadingError() {
                imageIV.setImageBitmap(null);
                imageIV.setOnClickListener(null);
                imageIV.setVisibility(View.GONE);
                errorTv.setVisibility(View.VISIBLE);
            }
        });
        container.addView(layout);
        return layout;
    }


    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    public Bitmap getFirstImageBmp() {
        return firstImageBmp;
    }

    /*public enum AdImages {

        IMAGE1(Configs.ADS_IMAGE1),
        IMAGE2(Configs.ADS_IMAGE2),
        IMAGE3(Configs.ADS_IMAGE3);

        private String imageFieldKey;

        AdImages(String imageFieldKey) {
            this.imageFieldKey = imageFieldKey;
        }
    }*/

    public interface OnImageClickListener {

        void onImageClicked(String imageFieldKey);
    }
}
