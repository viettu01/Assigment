package com.tuplv.mynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tuplv.mynote.activity.AddUpdateNoteActivity;
import com.tuplv.mynote.adapter.NoteAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnNoteClickListener {

    EditText edtSearch;
    RecyclerView rvNote;

    List<Note> listNote;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mapping();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtSearch.length() != 0) {
                    listNote = MyDatabase.getInstance(SearchActivity.this).findNoteByTitle(edtSearch.getText().toString());
                    noteAdapter = new NoteAdapter(SearchActivity.this, R.layout.item_note, listNote, SearchActivity.this);
                    rvNote.setAdapter(noteAdapter);
                    StaggeredGridLayoutManager staggeredGridLayoutManager
                            = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                    rvNote.setLayoutManager(staggeredGridLayoutManager);
                }
            }
        });
    }

    private void mapping() {
        edtSearch = findViewById(R.id.edtSearch);
        rvNote = findViewById(R.id.rvNote);
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