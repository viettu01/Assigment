package com.tuplv.mynote.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.tuplv.mynote.InvertingColor;
import com.tuplv.mynote.R;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.model.Category;
import com.tuplv.mynote.model.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddUpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_SCAN = 0x01;

    EditText edtTitleNote, edtContentNote;
    TextView tvDateUpdateNote;
    ImageView imgCloseAddNote, imgTodoList, imgBackgroundColorNote, imgAddNote, imgScanQRCode;
    Spinner spnCategory;

    MyDatabase myDatabase = MyDatabase.getInstance(this);

    List<Category> listCategory;
    ArrayAdapter categoryAdapter;

    Note note;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_note);
        mapping();
        setDateTimeUpdateNote();
        getCategory();
        checkUpdate();
    }

    private void mapping() {
        edtTitleNote = findViewById(R.id.edtTitleNote);
        edtContentNote = findViewById(R.id.edtContentNote);
        tvDateUpdateNote = findViewById(R.id.tvDateUpdateNote);
        imgCloseAddNote = findViewById(R.id.imgCloseAddNote);
//        imgTodoList = findViewById(R.id.imgTodoList);
        imgBackgroundColorNote = findViewById(R.id.imgBackgroundColorNote);
        imgScanQRCode = findViewById(R.id.imgScanQRCode);
        imgAddNote = findViewById(R.id.imgAddNote);
        spnCategory = findViewById(R.id.spnCategory);

        tvDateUpdateNote.setOnClickListener(this);
        imgCloseAddNote.setOnClickListener(this);
//        imgTodoList.setOnClickListener(this);
        imgBackgroundColorNote.setOnClickListener(this);
        imgScanQRCode.setOnClickListener(this);
        imgAddNote.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCloseAddNote:
                finish();
                break;
            case R.id.imgBackgroundColorNote:
                openColorPicker();
                break;
            case R.id.imgScanQRCode:
                int result = ScanUtil.startScan(this,
                        REQUEST_CODE_SCAN,
                        new HmsScanAnalyzerOptions.Creator()
                                .setHmsScanTypes(HmsScan.ALL_SCAN_TYPE, HmsScan.CODE128_SCAN_TYPE)
                                .create());
                break;
            case R.id.imgAddNote:
                addNote();
                break;
        }
    }

    private void openColorPicker() {
        defaultColor = ((ColorDrawable) edtContentNote.getBackground()).getColor();
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                edtContentNote.setBackgroundColor(defaultColor);
                edtContentNote.setTextColor(InvertingColor.invertingColor(color));
            }
        });
        colorPicker.show();
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

    private void checkUpdate() {
        note = (Note) getIntent().getSerializableExtra("note");
        if (note != null) {
            edtTitleNote.setText(note.getTitle());
            tvDateUpdateNote.setText(note.getUpdateAt());
            edtContentNote.setText(note.getContent());
            edtContentNote.setTextColor(InvertingColor.invertingColor(Integer.parseInt(note.getColor())));
            edtContentNote.setBackgroundColor(Integer.parseInt(note.getColor()));

            for (int i = 0; i < listCategory.size(); i++) {
                if (note.getCategoryId() == listCategory.get(i).getId()) {
                    spnCategory.setSelection(i);
                }
            }
        }
    }

    private void addNote() {
        Category category = (Category) spnCategory.getSelectedItem();
        int id_category = category.getId();
        String title = edtTitleNote.getText().toString();
        String createdAt = tvDateUpdateNote.getText().toString();
        String content = edtContentNote.getText().toString();
        String color = String.valueOf(((ColorDrawable) edtContentNote.getBackground()).getColor());

        if (note != null) {
            note = new Note(note.getId(), id_category, title, content, color, note.getCreatedAt(), dateFormat.format(new Date()));
            if (myDatabase.updateNote(note)) {
                Toast.makeText(this, "Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Fix failed notes", Toast.LENGTH_SHORT).show();
            }
        } else {
            note = new Note(0, id_category, title, content, color, createdAt, dateFormat.format(new Date()));
            if (myDatabase.insertNote(note)) {
                Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Add fail!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults.length < 2 ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);

            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj.getOriginalValue());
                        edtTitleNote.setText(jsonObject.getString("title"));
                        edtContentNote.setText(jsonObject.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }
}