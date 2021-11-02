package com.tuplv.mynote.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuplv.mynote.R;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.model.Category;
import com.tuplv.mynote.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddUpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtTitleNote, edtContentNote;
    TextView tvDateUpdateNote;
    ImageView imgCloseAddNote, imgTodoList, imgAddNote;
    Spinner spnCategory;

    MyDatabase myDatabase = MyDatabase.getInstance(this);

    List<Category> listCategory;
    ArrayAdapter categoryAdapter;

    Note note;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_note);
        mapping();
        setDateTimeUpdateNote();
        getCategory();
    }

    private void mapping() {
        edtTitleNote = findViewById(R.id.edtTitleNote);
        edtContentNote = findViewById(R.id.edtContentNote);
        tvDateUpdateNote = findViewById(R.id.tvDateUpdateNote);
        imgCloseAddNote = findViewById(R.id.imgCloseAddNote);
        imgTodoList = findViewById(R.id.imgTodoList);
        imgAddNote = findViewById(R.id.imgAddNote);
        spnCategory = findViewById(R.id.spnCategory);

        tvDateUpdateNote.setOnClickListener(this);
        imgCloseAddNote.setOnClickListener(this);
        imgTodoList.setOnClickListener(this);
        imgAddNote.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCloseAddNote:
                finish();
                break;
            case R.id.imgTodoList:
                break;
            case R.id.imgAddNote:
                addNote();
                break;
        }
    }

    private void getCategory() {
        listCategory = myDatabase.getAllCategory();
        categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(categoryAdapter);
    }

    private void setDateTimeUpdateNote() {
        if (tvDateUpdateNote.getText().toString().equals("")) {
            tvDateUpdateNote.setText(dateFormat.format(new Date()));
        }
    }

    private void addNote() {
        Category category = (Category) spnCategory.getSelectedItem();
        int id_category = category.getId();
        String title = edtTitleNote.getText().toString();
        String createdAt = tvDateUpdateNote.getText().toString();
        String content = edtContentNote.getText().toString();
        String color = String.valueOf(((ColorDrawable) edtContentNote.getBackground()).getColor());

        note = new Note(0, id_category, title, content, color, createdAt, dateFormat.format(new Date()));
        if (myDatabase.insertNote(note)) {
            Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Add fail!", Toast.LENGTH_SHORT).show();
        }
    }
}