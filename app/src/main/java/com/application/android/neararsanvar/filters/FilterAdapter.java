package com.application.android.neararsanvar.filters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.application.android.neararsanvar.R;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterVH> {

    private List<FilterItemHolder> categories;
    private OnFilterSelectedListener onFilterSelectedListener;
    private int selectedCategoryPosition = -1;

    public FilterAdapter(List<String> filterTitles, String selectedFilter,
                         OnFilterSelectedListener onFilterSelectedListener) {
        this.categories = new ArrayList<>();
        if (filterTitles != null && !filterTitles.isEmpty()) {
            for (int i = 0; i < filterTitles.size(); i++) {
                String filter = filterTitles.get(i);
                boolean isSelected = filter.equals(selectedFilter);
                if (isSelected) {
                    selectedCategoryPosition = i;
                }
                this.categories.add(new FilterItemHolder(isSelected, filter));
            }
        }
        this.onFilterSelectedListener = onFilterSelectedListener;
    }

    @NonNull
    @Override
    public FilterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_category, parent, false);
        return new FilterVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterVH holder, int position) {
        FilterItemHolder category = categories.get(position);
        holder.titleTV.setText(category.name);
        /*if (category.isSelected) {
            holder.titleTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark_red_ic, 0);
        } else {
            holder.titleTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }*/
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    class FilterVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV;

        public FilterVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTV = itemView.findViewById(R.id.ic_title_tv);
        }

        @Override
        public void onClick(View v) {
            if (selectedCategoryPosition != -1) {
                FilterItemHolder selectedFilterItemHolder = categories.get(selectedCategoryPosition);
                selectedFilterItemHolder.isSelected = false;
                notifyItemChanged(selectedCategoryPosition);
            }

            selectedCategoryPosition = getAdapterPosition();
            FilterItemHolder selectedCategory = categories.get(selectedCategoryPosition);
            selectedCategory.isSelected = true;
            notifyItemChanged(selectedCategoryPosition);

            if (onFilterSelectedListener != null) {
                onFilterSelectedListener.onFilterSelected(selectedCategory.name);
            }
        }
    }

    public class FilterItemHolder {

        private boolean isSelected;
        private String name;

        public FilterItemHolder(boolean isSelected, String name) {
            this.isSelected = isSelected;
            this.name = name;
        }
    }

    public interface OnFilterSelectedListener {

        void onFilterSelected(String filter);
    }
}
