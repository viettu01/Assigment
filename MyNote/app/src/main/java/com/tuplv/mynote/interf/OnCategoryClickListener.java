package com.tuplv.mynote.interf;

import com.tuplv.mynote.model.Category;

public interface OnCategoryClickListener {
    void onUpdateClick(Category category);
    void onDeleteClick(Category category);
}
