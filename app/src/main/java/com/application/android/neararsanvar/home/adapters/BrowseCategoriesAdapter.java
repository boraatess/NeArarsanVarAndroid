package com.application.android.neararsanvar.home.adapters;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.ImageLoadingUtils;

public class BrowseCategoriesAdapter extends RecyclerView.Adapter<BrowseCategoriesAdapter.BrowseCategoryVH> {

    private List<ParseObject> categories;
    private OnCategorySelectedListener onCategorySelectedListener;

    public BrowseCategoriesAdapter(List<ParseObject> categories, OnCategorySelectedListener onCategorySelectedListener) {
        this.categories = categories;
        this.onCategorySelectedListener = onCategorySelectedListener;
    }

    @NonNull
    @Override
    public BrowseCategoryVH onCreateViewHolder(@NonNull ViewGroup convertview, int viewType) {
        //View teksatir = convertview;
        //BrowseCategoryVH teksatirholder = null;
        //if (teksatir == null) {
            LayoutInflater inflater = LayoutInflater.from(convertview.getContext());
            View teksatir = inflater.inflate(R.layout.item_browse_category, convertview, false);
            //teksatirholder = new BrowseCategoryVH(teksatir);
            //teksatir.setTag(teksatirholder);
        //}
        //else{
        //    teksatirholder = (BrowseCategoryVH) teksatir.getTag();
        //}
        return new BrowseCategoryVH(teksatir);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseCategoryVH holder, int position) {
        ParseObject cObj = categories.get(position);

        holder.titleTV.setTypeface(Configs.mainTitle);
        holder.titleTV.setText(cObj.getString(Configs.CATEGORIES_CATEGORY));

        if (cObj.getObjectId().equals("2LL0JMNPGa")){
            holder.titleTV.setText("Emlak");

        }
        else if (cObj.getObjectId().equals( "V3DLOSezfE")){
            holder.titleTV.setText("Vasıtalar");

        }
        else if (cObj.getObjectId().equals( "LYwxKBWF3B")){
            holder.titleTV.setText("Giyim");

        }
        else if (cObj.getObjectId().equals( "ZywikrhGbs")){
            holder.titleTV.setText("Aksesuarlar");

        }
        else if (cObj.getObjectId().equals( "Tx30jtfFTm")){
            holder.titleTV.setText("Elektronik");

        }
        else if (cObj.getObjectId().equals( "tMOn65yLin")){
            holder.titleTV.setText("Organizasyonlar");

        }
        else if (cObj.getObjectId().equals( "ssCfEBRe4n")){
            holder.titleTV.setText("Ev Dekorasyonu");

        }
        else if (cObj.getObjectId().equals( "Ygd5WlzDPx")){
            holder.titleTV.setText("Kozmetikler");

        }

        holder.loadImage();
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class BrowseCategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV;
        private ImageView imageIV;


        public BrowseCategoryVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            titleTV = itemView.findViewById(R.id.ibc_title_tv);
            imageIV = itemView.findViewById(R.id.ibc_image_iv);
            //loadingPB = itemView.findViewById(R.id.ibc_loading_pb);
        }

        private void loadImage() {
            //loadingPB.setVisibility(View.VISIBLE);


            ParseObject categoryObj = categories.get(getAdapterPosition());
            ImageLoadingUtils.loadImage(categoryObj, Configs.CATEGORIES_IMAGE, new ImageLoadingUtils
                    .OnImageLoadListener() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    //loadingPB.setVisibility(View.GONE);
                    imageIV.setImageBitmap(bitmap);
                }

                @Override
                public void onImageLoadingError() {
                    //loadingPB.setVisibility(View.GONE);
                    imageIV.setImageBitmap(null);
                    //todo show placeholder
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (onCategorySelectedListener != null) {
                ParseObject categoryObj = categories.get(getAdapterPosition());
                onCategorySelectedListener.onCategorySelected(categoryObj);
            }
        }
    }

    public interface OnCategorySelectedListener {

        void onCategorySelected(ParseObject categoryObj);
    }
}
