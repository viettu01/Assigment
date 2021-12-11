package com.tuplv.mynote.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tuplv.mynote.R;
import com.tuplv.mynote.adapter.CategoryAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnCategoryClickListener;
import com.tuplv.mynote.model.Category;

import java.util.List;

public class ListCategoryActivity extends AppCompatActivity implements OnCategoryClickListener {

    Toolbar tbListCategory;
    RecyclerView rvCategory;
    CategoryAdapter categoryAdapter;

    List<Category> listCategory;
    MyDatabase myDatabase = MyDatabase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        mapping();
        setSupportActionBar(tbListCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showDataCategory();
    }

    private void showDataCategory() {
        listCategory = myDatabase.getAllCategory();
        categoryAdapter = new CategoryAdapter(ListCategoryActivity.this, R.layout.item_list_category, listCategory, this);
        rvCategory.setAdapter(categoryAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCategory.addItemDecoration(itemDecoration);
    }

    private void mapping() {
        rvCategory = findViewById(R.id.lvCategory);
        tbListCategory = findViewById(R.id.tbListCategory);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showActivityAddCategory(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_update_category);
        dialog.setCanceledOnTouchOutside(false);

        EditText edtTitleCategory;
        Button btnCancelCategory, btnAddUpdateCategory;

        edtTitleCategory = dialog.findViewById(R.id.edtTitleCategory);
        btnCancelCategory = dialog.findViewById(R.id.btnCancelCategory);
        btnAddUpdateCategory = dialog.findViewById(R.id.btnAddUpdateCategory);

        btnCancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAddUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String titleCategory = edtTitleCategory.getText().toString();
                if (titleCategory.equalsIgnoreCase("")) {
                    dialog.dismiss();
                } else {
//                    for (int i = 0; i < listCategory.size(); i++) {
//                        if (titleCategory.equalsIgnoreCase(listCategory.get(i).getTitle())) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(ListCategoryActivity.this);
//                            builder.setTitle("Warning!");
//                            builder.setIcon(android.R.drawable.ic_delete);
//                            builder.setMessage("Tag already exist, you want add?");
//                            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Category category = new Category(0, titleCategory.trim(), "");
//                                    if (myDatabase.insertCategory(category)) {
//                                        Toast.makeText(ListCategoryActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
//                                        listCategory.add(category);
//                                        categoryAdapter.notifyDataSetChanged();
//                                        dialog.dismiss();
//                                    } else {
//                                        Toast.makeText(ListCategoryActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//                            builder.show();
//                            Toast.makeText(ListCategoryActivity.this, "Tag already exist!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
                    Category category = new Category(0, titleCategory.trim(), "");
                    if (myDatabase.insertCategory(category)) {
                        Toast.makeText(ListCategoryActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                        listCategory.add(category);
                        categoryAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ListCategoryActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateClick(Category category) {
        String title = category.getTitle();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_update_category);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitleActivityAddUpdate;
        EditText edtTitleCategory;
        Button btnCancelCategory, btnAddUpdateCategory;

        tvTitleActivityAddUpdate = dialog.findViewById(R.id.tvTitleActivityAddUpdate);
        edtTitleCategory = dialog.findViewById(R.id.edtTitleCategory);
        btnCancelCategory = dialog.findViewById(R.id.btnCancelCategory);
        btnAddUpdateCategory = dialog.findViewById(R.id.btnAddUpdateCategory);

        tvTitleActivityAddUpdate.setText("Rename tag");
        btnAddUpdateCategory.setText("Rename");
        edtTitleCategory.setText(title);

        btnCancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAddUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String titleNew = edtTitleCategory.getText().toString().trim();
                if (titleNew.equalsIgnoreCase("")) {
                    dialog.dismiss();
                } else {
//                    for (int i = 0; i < listCategory.size(); i++) {
//                        if (titleNew.equalsIgnoreCase(listCategory.get(i).getTitle())) {
//                            Toast.makeText(ListCategoryActivity.this, "Tag already exist!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
                    category.setTitle(titleNew);
                    if (myDatabase.updateCategory(category)) {
                        Toast.makeText(ListCategoryActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                        categoryAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ListCategoryActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onDeleteClick(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Delete this tag?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myDatabase.deleteCategory(category)) {
                    Toast.makeText(ListCategoryActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                    listCategory.remove(category);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListCategoryActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}