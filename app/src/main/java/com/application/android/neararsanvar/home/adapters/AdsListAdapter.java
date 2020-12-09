package com.application.android.neararsanvar.home.adapters;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.ImageLoadingUtils;

public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.AdsListVH> {

    private List<ParseObject> adsList;
    private OnAdClickListener onAdClickListener;

    public AdsListAdapter(List<ParseObject> adsList, OnAdClickListener onAdClickListener) {
        this.adsList = adsList;
        this.onAdClickListener = onAdClickListener;
    }

    public void addMoreAds(List<ParseObject> moreAds) {
        int itemCountBeforeAdding = getItemCount();
        this.adsList.addAll(moreAds);
        notifyItemRangeInserted(itemCountBeforeAdding, this.adsList.size());
    }

    public void clearAds() {
        if (adsList != null) {
            adsList.clear();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ads_list, parent, false);
        return new AdsListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsListVH holder, int position) {
        final ParseObject adObj = adsList.get(position);

        holder.titleTV.setTypeface(Configs.titSemibold);
        holder.conditionTV.setTypeface(Configs.titLight);
        holder.titleTV.setText(adObj.getString(Configs.ADS_TITLE));
        holder.conditionTV.setText(adObj.getString(Configs.ADS_CONDITION));

        if (holder.conditionTV.getText().toString().equalsIgnoreCase("Gift")){
            holder.giftImage.setVisibility(View.VISIBLE);
            holder.giftImage.setImageResource(R.drawable.gift);
            holder.priceTV.setText("");

        }else if(holder.conditionTV.getText().toString().equalsIgnoreCase("Exchange")){
            holder.giftImage.setVisibility(View.VISIBLE);
            holder.giftImage.setImageResource(R.drawable.exchange);
            holder.priceTV.setText("");

        }else {
            holder.giftImage.setVisibility(View.GONE);
            holder.priceTV.setTypeface(Configs.titBlack);
            holder.priceTV.setText(adObj.getString(Configs.ADS_CURRENCY)+" "+adObj.getNumber(Configs.ADS_PRICE));
        }

        holder.loadImage();
    }

    @Override
    public int getItemCount() {
        return adsList != null ? adsList.size() : 0;
    }

    public class AdsListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV;
        private ImageView imageIV;
        private TextView priceTV;
        private TextView conditionTV;
        private ImageView profileImage;
        private ImageView giftImage;

        public AdsListVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTV = itemView.findViewById(R.id.ial_title_tv);
            imageIV = itemView.findViewById(R.id.ial_image_iv);
            priceTV = itemView.findViewById(R.id.ial_price_tv);
            profileImage = itemView.findViewById(R.id.profileImage);
            conditionTV = itemView.findViewById(R.id.ial_condition_tv);
            giftImage = itemView.findViewById(R.id.imgGift);
        }


        private void loadImage() {
            final ParseObject adObj = adsList.get(getAdapterPosition());


            adObj.getParseObject(Configs.ADS_SELLER_POINTER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                public void done(final ParseObject sellerPointer, ParseException e) {
                    // Get Avatar
                    Configs.getParseImage(profileImage, sellerPointer, Configs.USER_AVATAR);
                }
            });// end sellerPointers


            ImageLoadingUtils.loadImage(adObj, Configs.ADS_IMAGE1, new ImageLoadingUtils.OnImageLoadListener() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    imageIV.setImageBitmap(bitmap);
                }

                @Override
                public void onImageLoadingError() {
                    imageIV.setImageResource(R.drawable.neararsanvar);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (onAdClickListener != null) {
                ParseObject clickedAdObj = adsList.get(getAdapterPosition());
                onAdClickListener.onAdClicked(clickedAdObj);
            }
        }
    }

    public interface OnAdClickListener {

        void onAdClicked(ParseObject adObj);
    }
}
