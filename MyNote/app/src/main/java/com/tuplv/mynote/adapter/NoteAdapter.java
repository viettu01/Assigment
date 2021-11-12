package com.tuplv.mynote.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tuplv.mynote.Format;
import com.tuplv.mynote.InvertingColor;
import com.tuplv.mynote.R;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Category;
import com.tuplv.mynote.model.Note;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

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

        holder.tvTitle.setTextColor(InvertingColor.invertingColor(backgroundItemColor));
        holder.tvContent.setTextColor(InvertingColor.invertingColor(backgroundItemColor));
        holder.tvDateUpdate.setTextColor(InvertingColor.invertingColor(backgroundItemColor));

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(Format.sortText(note.getContent(), 120));
        holder.tvDateUpdate.setText(note.getUpdateAt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToActivityUpdate(note);
            }
        });

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
                        updateCategoryOfNote(note);
                        break;
                    case R.id.mnuChangeBackground:
                        openColorPicker(holder, note);
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

    private void updateCategoryOfNote(Note note) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_category_of_note);

        ListView lvCategory = dialog.findViewById(R.id.lvCategory);
        List<Category> listTitleCategory = MyDatabase.getInstance(context).getAllCategory();

        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listTitleCategory);
        lvCategory.setAdapter(arrayAdapter);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                note.setCategoryId(listTitleCategory.get(position).getId());
                if (MyDatabase.getInstance(context).updateNote(note)) {
                    Toast.makeText(context, "Successful!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void openColorPicker(ViewHolder holder, Note note) {
        backgroundItemColor = Integer.parseInt(note.getColor());
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context, backgroundItemColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                note.setColor(String.valueOf(color));
                holder.tvTitle.setTextColor(InvertingColor.invertingColor(color));
                holder.tvContent.setTextColor(InvertingColor.invertingColor(color));
                holder.tvDateUpdate.setTextColor(InvertingColor.invertingColor(color));
                if (MyDatabase.getInstance(context).updateNote(note)){
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });
        colorPicker.show();
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
