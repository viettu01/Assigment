package com.tuplv.mynote.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tuplv.mynote.R;
import com.tuplv.mynote.activity.ListCategoryActivity;
import com.tuplv.mynote.adapter.CategoryAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.model.Category;

import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener {

    List<Category> listCategory;
    CategoryAdapter categoryAdapter;

    MyDatabase myDatabase;

    ImageView imgAddCategory;
    RecyclerView rvCategory;

    SharedPreferences sharedPreferences;
    int ROW = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mapping(view);

        myDatabase = MyDatabase.getInstance(getActivity());

        imgAddCategory.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("option", MODE_PRIVATE);
        ROW = sharedPreferences.getInt("ROW", 1);

        return view;
    }

    private void mapping(View view) {
        imgAddCategory = view.findViewById(R.id.imgAddCategory);
        rvCategory = view.findViewById(R.id.lvCategory);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAddCategory:
                startActivity(new Intent(getActivity(), ListCategoryActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showDataCategory();
    }

    private void showDataCategory() {
        listCategory = myDatabase.getAllCategory();

        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.item_category, listCategory);
        rvCategory.setAdapter(categoryAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);
    }
}
