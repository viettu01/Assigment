package com.tuplv.mynote.interf;

import com.tuplv.mynote.model.Note;

public interface OnNoteClickListener {
    void goToActivityUpdate(Note note);
    void onDeleteClick(Note note);
    void onGetListNoteByCategoryId(int id);
    void onShareNote(Note note);
}
