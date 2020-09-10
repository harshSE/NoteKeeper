package com.harshdev.notekeeper.ui.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.persistance.NoteDetail;
import com.harshdev.notekeeper.ui.note.NoteActivity;

import java.util.List;
import java.util.Objects;

public class NoteItemListRecyclerAdapter extends RecyclerView.Adapter<NoteItemListRecyclerAdapter.NoteItemViewHolder> {

    private @Nullable
    List<NoteDetail> notes;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public NoteItemListRecyclerAdapter(Context context) {
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
        holder.setNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        if (Objects.nonNull(notes)) {
            return notes.size();
        } else {
            return 0;
        }
    }


    public void setNotes(List<NoteDetail> notes) {
        this.notes = notes;
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

        public void setNote(NoteDetail note) {
            this.id = note.getId();
            titleView.setText(note.getTitle());
            courseView.setText(note.getCourseTitle());
        }
    }

}
