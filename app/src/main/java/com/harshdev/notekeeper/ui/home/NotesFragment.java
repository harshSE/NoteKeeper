package com.harshdev.notekeeper.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.data.DataManager;
import com.harshdev.notekeeper.data.NoteInfo;
import com.harshdev.notekeeper.R;

import java.util.List;

public class NotesFragment extends Fragment {


    private NoteItemListRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initializeDisplayContent(root);

        return root;
    }

    private void initializeDisplayContent(View container) {

        RecyclerView notesView = container.findViewById(R.id.list_items);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        notesView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new NoteItemListRecyclerAdapter(notes,getContext());
        notesView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}