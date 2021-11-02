package com.tuplv.mynote.interf;

import com.tuplv.mynote.model.Note;

public interface OnNoteClickListener {
    void onDeleteClick(Note note);
    void onShareNote(Note note);
}
