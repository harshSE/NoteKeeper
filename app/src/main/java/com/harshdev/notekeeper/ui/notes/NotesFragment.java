package com.harshdev.notekeeper.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.persistance.NoteDetail;

import java.util.List;

public class NotesFragment extends Fragment {


    private NoteItemListRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        initializeDisplayContent(root);
        notesViewModel.getAllNoteDetails().observe(getViewLifecycleOwner(), new Observer<List<NoteDetail>>() {
            @Override
            public void onChanged(List<NoteDetail> noteDetails) {
                adapter.setNotes(noteDetails);
            }
        });



        return root;
    }

    private void initializeDisplayContent(View container) {

        RecyclerView notesView = container.findViewById(R.id.list_items);

        notesView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NoteItemListRecyclerAdapter(getContext());
        notesView.setAdapter(adapter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}