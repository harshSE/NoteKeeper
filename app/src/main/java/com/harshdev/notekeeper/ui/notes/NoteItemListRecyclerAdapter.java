package com.harshdev.notekeeper.ui.notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.data.DataManager;
import com.harshdev.notekeeper.data.NoteInfo;
import com.harshdev.notekeeper.db.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.harshdev.notekeeper.ui.note.NoteActivity;

import java.util.Objects;

public class NoteItemListRecyclerAdapter extends RecyclerView.Adapter<NoteItemListRecyclerAdapter.NoteItemViewHolder> {

    private Cursor noteCursor;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public NoteItemListRecyclerAdapter(Cursor noteCursor, Context context) {
        this.noteCursor = noteCursor;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new NoteItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder holder, int position) {
        if(!noteCursor.moveToPosition(position)) {
            return;
        }
        final int idIndex = noteCursor.getColumnIndex(BaseColumns._ID);
        final int noteTextIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        final int noteTitleIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        final int courseIdIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);


        final DataManager dataManager = DataManager.getInstance();
        String noteText = noteCursor.getString(noteTextIndex);
        String noteTitle = noteCursor.getString(noteTitleIndex);
        String courseId = noteCursor.getString(courseIdIndex);
        int id = noteCursor.getInt(idIndex);

        holder.setNoteInfo(new NoteInfo(id, dataManager.getCourse(courseId), noteTitle, noteText));

    }

    @Override
    public int getItemCount() {
        return noteCursor.getCount();
    }

    public void changeCursor(Cursor cursor){
        if(Objects.nonNull(noteCursor)) {
            noteCursor.close();
        }

        noteCursor = cursor;
        notifyDataSetChanged();
    }

    public static class NoteItemViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private TextView courseView;
        private Context context;
        private int id;

        public NoteItemViewHolder(@NonNull View view, Context context) {
            super(view);
            titleView = view.findViewById(R.id.text_title);
            courseView = view.findViewById(R.id.text_course);
            this.context = context;
            attachListener(view);
        }

        private void attachListener(View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent noteIntent = new Intent(context, NoteActivity.class);
                    noteIntent.putExtra(NoteActivity.NOTE_ID, id);
                    context.startActivity(noteIntent);
                }
            });
        }

        public void setNoteInfo(NoteInfo noteInfo) {
            this.id = noteInfo.getId();
            titleView.setText(noteInfo.getTitle());
            courseView.setText(noteInfo.getCourse().getTitle());
        }
    }

}
