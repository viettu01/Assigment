package com.tuplv.mynote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tuplv.mynote.R;
import com.tuplv.mynote.adapter.NoteAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Note;

import java.util.ArrayList;
import java.util.List;

public class SearchTitleActivity extends AppCompatActivity implements OnNoteClickListener {

    ImageView imgBack;
    EditText edtSearch;
    RecyclerView rvNote;

    List<Note> listNote;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_title);
        mapping();
    }

    private void mapping() {
        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvNote = findViewById(R.id.rvNote);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    listNote = MyDatabase.getInstance(SearchTitleActivity.this).findNoteByTitle(s.toString().trim());
                } else {
                    listNote = new ArrayList<>();
                }
                noteAdapter = new NoteAdapter(SearchTitleActivity.this, R.layout.item_note, listNote, SearchTitleActivity.this);
                rvNote.setAdapter(noteAdapter);
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rvNote.setLayoutManager(staggeredGridLayoutManager);
            }
        });
    }

    @Override
    public void goToActivityUpdate(Note note) {
        Intent intent = new Intent(this, AddUpdateNoteActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Note note) {

    }

    @Override
    public void onGetListNoteByCategoryId(int id) {

    }

    @Override
    public void onShareNote(Note note) {

    }
}