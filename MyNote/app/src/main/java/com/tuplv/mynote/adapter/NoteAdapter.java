package com.tuplv.mynote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tuplv.mynote.R;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    int layout;
    List<Note> list;
    OnNoteClickListener listener;

    int backgroundItemColor;

    public NoteAdapter(Context context, int layout, List<Note> list, OnNoteClickListener listener) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.listener = listener;
    }

    public NoteAdapter(Context context, int layout, List<Note> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = list.get(position);

        backgroundItemColor = Integer.parseInt(note.getColor());
        Drawable drawable = context.getResources().getDrawable(R.drawable.custom_background_item_note);
        drawable.setColorFilter(backgroundItemColor, PorterDuff.Mode.SRC_ATOP);
        holder.llItemNote.setBackground(drawable);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvDateUpdate.setText(note.getUpdateAt());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(holder, note);
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(ViewHolder holder, Note note) {
        MenuBuilder menuBuilder = new MenuBuilder(context);
        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.popup_menu_item_note, menuBuilder);

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(context, menuBuilder, holder.itemView);
        menuPopupHelper.setForceShowIcon(true);

        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnuChangeTag:
                        break;
                    case R.id.mnuDeleteNote:
                        listener.onDeleteClick(note);
                        break;
                    case R.id.mnuShare:
                        listener.onShareNote(note);
                        break;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });

        menuPopupHelper.show();
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }

        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItemNote;
        TextView tvTitle, tvContent, tvDateUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llItemNote = itemView.findViewById(R.id.llItemNote);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDateUpdate = itemView.findViewById(R.id.tvDateUpdate);
        }
    }
}
