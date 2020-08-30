package com.harshdev.notekeeper.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.ui.NoteActivity;
import com.harshdev.notekeeper.data.NoteInfo;
import com.harshdev.notekeeper.R;

import java.util.List;

public class NoteItemListRecyclerAdapter extends RecyclerView.Adapter<NoteItemListRecyclerAdapter.NoteItemViewHolder> {

    private final List<NoteInfo> notes;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public NoteItemListRecyclerAdapter(List<NoteInfo> notes, Context context) {
        this.notes = notes;
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
        holder.setNoteInfo(notes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteItemViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private TextView courseView;
        private Context context;
        private int position;

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
                    noteIntent.putExtra(NoteActivity.NOTE_POSITION, position);
                    context.startActivity(noteIntent);
                }
            });
        }

        public void setNoteInfo(NoteInfo noteInfo, int position) {
            this.position = position;
            titleView.setText(noteInfo.getTitle());
            courseView.setText(noteInfo.getCourse().getTitle());
        }
    }

}
