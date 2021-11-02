package com.tuplv.mynote.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.tuplv.mynote.R;
import com.tuplv.mynote.activity.AddUpdateNoteActivity;
import com.tuplv.mynote.activity.ListCategoryActivity;
import com.tuplv.mynote.adapter.CategoryAdapter;
import com.tuplv.mynote.adapter.NoteAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Category;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener, OnNoteClickListener {

    List<Category> listCategory;
    CategoryAdapter categoryAdapter;

    List<Note> listNote;
    NoteAdapter noteAdapter;

    MyDatabase myDatabase;

    ImageView imgAddCategory;
    FloatingActionButton fabAddNote;
    RecyclerView rvCategory, rvNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mapping(view);

        myDatabase = MyDatabase.getInstance(getActivity());

        imgAddCategory.setOnClickListener(this);
        fabAddNote.setOnClickListener(this);

        return view;
    }

    private void mapping(View view) {
        imgAddCategory = view.findViewById(R.id.imgAddCategory);
        fabAddNote = view.findViewById(R.id.fabAddNote);
        rvCategory = view.findViewById(R.id.lvCategory);
        rvNote = view.findViewById(R.id.rvNote);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAddCategory:
                startActivity(new Intent(getActivity(), ListCategoryActivity.class));
                break;
            case R.id.fabAddNote:
                startActivity(new Intent(getActivity(), AddUpdateNoteActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showDataCategory();
        showDataNote();
    }

    private void showDataNote() {
        listNote = myDatabase.getAllNote();
        setDataNoteToView();
    }

    private void showDataCategory() {
        listCategory = myDatabase.getAllCategory();

        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.item_category, listCategory, this);
        rvCategory.setAdapter(categoryAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);
    }

    public void setDataNoteToView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        noteAdapter = new NoteAdapter(getActivity(), R.layout.item_note, listNote, this);
        rvNote.setAdapter(noteAdapter);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvNote.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public void onDeleteClick(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warning!");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myDatabase.deleteNote(note)) {
                    Toast.makeText(getActivity(), "Successful!", Toast.LENGTH_SHORT).show();
                    listNote.remove(note);
                    noteAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onShareNote(Note note) {
        int type = HmsScan.QRCODE_SCAN_TYPE;
        int width = 400;
        int height = 400;

        HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.BLACK).setBitmapColor(android.R.color.white).setBitmapMargin(2).create();

        Bitmap qrBitmap = null;
        try {
            qrBitmap = ScanUtil.buildBitmap(note.noteMapper(), type, width, height, options);
        } catch (WriterException e) {
            Log.w("buildBitmap", e);
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_qr_code);

        ImageView imgQRCode = dialog.findViewById(R.id.imgQRCode);

        imgQRCode.setImageBitmap(qrBitmap);

        dialog.show();
    }
}
