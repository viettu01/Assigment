package com.tuplv.mynote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tuplv.mynote.R;
import com.tuplv.mynote.adapter.NoteAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class SearchDateActivity extends AppCompatActivity implements OnNoteClickListener {

    ImageView imgBack;
    EditText edtStartDate, edtEndDate;
    Button btnSearchDate;
    RecyclerView rvNote;

    List<Note> listNote;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date);
        mapping();
    }

    private void mapping() {
        imgBack = findViewById(R.id.imgBack);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnSearchDate = findViewById(R.id.btnSearchDate);
        rvNote = findViewById(R.id.rvNote);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        btnSearchDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String startDate = edtStartDate.getText().toString();
//                String endDate = edtEndDate.getText().toString();
//                listNote = MyDatabase.getInstance(SearchDateActivity.this).findNoteByDate(startDate, endDate);
//                noteAdapter = new NoteAdapter(SearchDateActivity.this, R.layout.item_note, listNote, SearchDateActivity.this);
//                rvNote.setAdapter(noteAdapter);
//                StaggeredGridLayoutManager staggeredGridLayoutManager
//                        = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//                rvNote.setLayoutManager(staggeredGridLayoutManager);
//            }
//        });
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