package com.application.android.neararsanvar.ads.adapters;

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

import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.ImageLoadingUtils;

public class BrowseSubcategoriesAdapter extends RecyclerView.Adapter<BrowseSubcategoriesAdapter.BrowseSubcategoryVH> {

    private List<ParseObject> subcategories;
    private OnSubcategorySelectedListener onSubcategorySelectedListener;

    public BrowseSubcategoriesAdapter(List<ParseObject> subcategories, OnSubcategorySelectedListener onSubcategorySelectedListener) {
        this.subcategories = subcategories;
        this.onSubcategorySelectedListener = onSubcategorySelectedListener;
    }

    @NonNull
    @Override
    public BrowseSubcategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View teksatir = convertview;
        //BrowseCategoryVH teksatirholder = null;
        //if (teksatir == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View teksatir = inflater.inflate(R.layout.item_browse_category, parent, false);
            //teksatirholder = new BrowseCategoryVH(teksatir);
            //teksatir.setTag(teksatirholder);
        //}
        //else{
        //    teksatirholder = (BrowseCategoryVH) teksatir.getTag();
        //}
        return new BrowseSubcategoryVH(teksatir);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseSubcategoryVH holder, int position) {
        ParseObject cObj = subcategories.get(position);

        holder.titleTV.setTypeface(Configs.mainTitle);
        holder.titleTV.setText(cObj.getString(Configs.SUBCATEGORIES_SUBCATEGORY));

        if (cObj.getObjectId().equals("916b7GUBrG")){
            holder.titleTV.setText("EV");

        }
        else if (cObj.getObjectId().equals( "iIHcbTG5YF")){
            holder.titleTV.setText("Ofis");

        }
        else if (cObj.getObjectId().equals( "PuOatii394")){
            holder.titleTV.setText("Telefon");

        }
        else if (cObj.getObjectId().equals( "ykJnEP2yBA")){
            holder.titleTV.setText("Laptop");

        }
        else if (cObj.getObjectId().equals( "0H5Z4sG1rK")){
            holder.titleTV.setText("Otomobil");

        }
        else if (cObj.getObjectId().equals( "JxRZ77zB6u")){
            holder.titleTV.setText("Motorsiklet");

        }
        else if (cObj.getObjectId().equals( "4qbLAm4D43")){
            holder.titleTV.setText("Kadın");

        }
        else if (cObj.getObjectId().equals( "NeZDJaIkvn")){
            holder.titleTV.setText("Erkek");

        }


        else if (cObj.getObjectId().equals("OX0yGsTrEl")){
            holder.titleTV.setText("Çocuk");

        }
        else if (cObj.getObjectId().equals( "A2gENdYPSf")){
            holder.titleTV.setText("Kolye");

        }
        else if (cObj.getObjectId().equals( "xDGnRY5HXE")){
            holder.titleTV.setText("Kupe");

        }
        else if (cObj.getObjectId().equals( "azkdLUiOVL")){
            holder.titleTV.setText("Evlilik");

        }
        else if (cObj.getObjectId().equals( "SXGv5hOacE")){
            holder.titleTV.setText("Konser");

        }
        else if (cObj.getObjectId().equals( "4wBERE8S5o")){
            holder.titleTV.setText("Oturma Odası");

        }
        else if (cObj.getObjectId().equals( "n5EpRbdFJf")){
            holder.titleTV.setText("Mutfak");

        }
        else if (cObj.getObjectId().equals( "nyWy7WF3zx")){
            holder.titleTV.setText("Makyaj");

        }

        else if (cObj.getObjectId().equals( "ALYIbqCGCS")){
            holder.titleTV.setText("Bakım");

        }

        holder.loadImage();
    }

    @Override
    public int getItemCount() {
        return subcategories != null ? subcategories.size() : 0;
    }

    public class BrowseSubcategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV;
        private ImageView imageIV;


        public BrowseSubcategoryVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            titleTV = itemView.findViewById(R.id.ibc_title_tv);
            imageIV = itemView.findViewById(R.id.ibc_image_iv);
            //loadingPB = itemView.findViewById(R.id.ibc_loading_pb);
        }

        private void loadImage() {
            //loadingPB.setVisibility(View.VISIBLE);


            ParseObject subcategoryObj = subcategories.get(getAdapterPosition());
            ImageLoadingUtils.loadImage(subcategoryObj, Configs.SUBCATEGORIES_IMAGE, new ImageLoadingUtils
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
            if (onSubcategorySelectedListener != null) {
                ParseObject subcategoryObj = subcategories.get(getAdapterPosition());
                onSubcategorySelectedListener.onSubcategorySelected(subcategoryObj);
            }
        }
    }

    public interface OnSubcategorySelectedListener {

        void onSubcategorySelected(ParseObject subcategoryObj);
    }
}
