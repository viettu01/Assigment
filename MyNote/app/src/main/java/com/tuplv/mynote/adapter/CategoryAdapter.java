package com.tuplv.mynote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuplv.mynote.R;
import com.tuplv.mynote.interf.OnCategoryClickListener;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<Category> listCategory;

    private OnCategoryClickListener onCategoryClickListener;
    private OnNoteClickListener onNoteClickListener;

    int row_index = -1;

    public CategoryAdapter(Context context, int layout, List<Category> listCategory, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public CategoryAdapter(Context context, int layout, List<Category> listCategory, OnNoteClickListener onNoteClickListener) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
        this.onNoteClickListener = onNoteClickListener;
    }

    public CategoryAdapter(Context context, int layout, List<Category> listCategory) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = listCategory.get(position);
        holder.tvItemCategory.setText(category.getTitle());

        if (layout == R.layout.item_category) {
            holder.tvItemCategory.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                    row_index = position;
                    notifyDataSetChanged();
                }
            });

            if (row_index == -1) {
                row_index = 0;
                holder.tvItemCategory.setBackgroundResource(R.drawable.custom_background_item_category_color);
                holder.tvItemCategory.setTextColor(Color.parseColor("#ffffff"));
            }
            if (row_index == position) {
                holder.tvItemCategory.setBackgroundResource(R.drawable.custom_background_item_category_color);
                holder.tvItemCategory.setTextColor(Color.parseColor("#ffffff"));
            } else {
                holder.tvItemCategory.setBackgroundResource(R.drawable.custom_background_item_category_base);
                holder.tvItemCategory.setTextColor(Color.parseColor("#000000"));
            }
        } else if (layout == R.layout.item_list_category) {
            if (category.getId() == 1) {
                holder.imgvMoreCategory.setEnabled(false);
                holder.imgvLockItem.setEnabled(false);
            }
            holder.imgvMoreCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder, category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listCategory != null) {
            return listCategory.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemCategory;
        ImageView imgvMoveItem, imgvLockItem, imgvMoreCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemCategory = itemView.findViewById(R.id.tvItemCategory);
            if (layout == R.layout.item_list_category) {
                imgvMoveItem = itemView.findViewById(R.id.imgvMoveItem);
                imgvLockItem = itemView.findViewById(R.id.imgvLockItem);
                imgvMoreCategory = itemView.findViewById(R.id.imgvMoreCategory);
            }
        }
    }

    private void showPopupMenu(ViewHolder holder, Category category) {
        PopupMenu popupMenu = new PopupMenu(context, holder.imgvMoreCategory);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_more_in_list_category, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnuDeleteCategory:
                        onCategoryClickListener.onDeleteClick(category);
                        break;
                    case R.id.mnuChangeNameCategory:
                        onCategoryClickListener.onUpdateClick(category);
                        break;
                }
                return false;
            }
        });
    }
}
