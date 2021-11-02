package com.tuplv.mynote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tuplv.mynote.R;
import com.tuplv.mynote.model.Category;
import com.tuplv.mynote.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "notes.sqlite";
    private static final int VERSION = 1;

    public static final String TABLE_CATEGORY = "tbl_category";
    public static final String TABLE_NOTE = "tbl_note";

    private static MyDatabase instance;

    private Context context;

    public static MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabase(context.getApplicationContext());
        }

        return instance;
    }

    private MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    public void execSQL(String sql) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.rawQuery(sql, selectionArgs);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableCategory = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, color TEXT)";
        db.execSQL(sqlCreateTableCategory);
        Category category = new Category(0, "All", "");
        ContentValues values = new ContentValues();
        values.put("title", category.getTitle());
        values.put("color", category.getColor());
        db.insert(TABLE_CATEGORY, null, values);

        String sqlCreateTableNote = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_category INTEGER, title TEXT, content TEXT, color TEXT, created_at TEXT, updated_at TEXT, " +
                "FOREIGN KEY (id_category) REFERENCES " + TABLE_CATEGORY + "(id))";
        db.execSQL(sqlCreateTableNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        Cursor cursor = rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String color = cursor.getString(2);

            Category category = new Category(id, title, color);
            list.add(category);
        }
        return list;
    }

    public List<String> getTitleCategory() {
        List<String> list = new ArrayList<>();
        Cursor cursor = rawQuery("SELECT title FROM " + TABLE_CATEGORY, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            list.add(title);
        }
        return list;
    }

    public boolean insertCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("title", category.getTitle());
        values.put("color", category.getColor());
        long index_insert = insert(TABLE_CATEGORY, null, values);
        return index_insert >= 1;
    }

    public boolean updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("title", category.getTitle());
        values.put("color", category.getColor());
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(category.getId())};
        int index_update = update(TABLE_CATEGORY, values, whereClause, whereArgs);
        return index_update >= 1;
    }

    public boolean deleteCategory(Category category) {
        String[] whereArgs = {String.valueOf(category.getId())};
        int index_delete = delete(TABLE_CATEGORY, "id = ?", whereArgs);
        return index_delete >= 1;
    }

    public List<Note> getAllNote() {
        List<Note> list = new ArrayList<>();
        Cursor cursor = rawQuery("SELECT * FROM " + TABLE_NOTE, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_category = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String created_at = cursor.getString(5);
            String updated_at = cursor.getString(6);

            Note note = new Note(id, id_category, title, content, color, created_at, updated_at);
            list.add(note);
        }
        return list;
    }

    public List<Note> getNoteByCategoryId(int categoryId) {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NOTE + " WHERE id_category = " + categoryId;
        Cursor cursor = rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_category = cursor.getInt(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String color = cursor.getString(4);
            String created_at = cursor.getString(5);
            String updated_at = cursor.getString(6);

            Note note = new Note(id, id_category, title, content, color, created_at, updated_at);
            list.add(note);
        }
        return list;
    }

    public boolean insertNote(Note note) {
        ContentValues values = new ContentValues();
        values.put("id_category", note.getCategoryId());
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("color", note.getColor());
        values.put("created_at", note.getCreatedAt());
        values.put("updated_at", note.getUpdateAt());
        long index_insert = insert(TABLE_NOTE, null, values);
        return index_insert >= 1;
    }

    public boolean updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put("id_category", note.getCategoryId());
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("color", note.getColor());
        values.put("created_at", note.getCreatedAt());
        values.put("updated_at", note.getUpdateAt());
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(note.getId())};
        int index_update = update(TABLE_NOTE, values, whereClause, whereArgs);
        return index_update >= 1;
    }

    public boolean deleteNote(Note note) {
        String[] whereArgs = {String.valueOf(note.getId())};
        int index_delete = delete(TABLE_NOTE, "id = ?", whereArgs);
        return index_delete >= 1;
    }

    public boolean checkIdExist(int id_check) {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT id FROM " + TABLE_NOTE + " WHERE id = " + id_check;
        Cursor cursor = rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);

            if (id == id_check) {
                return true;
            }
        }
        return false;
    }
}
