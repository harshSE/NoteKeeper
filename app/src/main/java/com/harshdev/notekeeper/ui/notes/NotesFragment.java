package com.harshdev.notekeeper.ui.notes;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.data.DataManager;
import com.harshdev.notekeeper.data.NoteInfo;
import com.harshdev.notekeeper.db.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.harshdev.notekeeper.db.NoteKeeperOpenHelper;

import java.util.List;

public class NotesFragment extends Fragment {


    private NoteItemListRecyclerAdapter adapter;
    private NoteKeeperOpenHelper openHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        openHelper = new NoteKeeperOpenHelper(container.getContext());

        initializeDisplayContent(root);


        return root;
    }

    private void initializeDisplayContent(View container) {

        DataManager.loadFromDatabase(openHelper);

        RecyclerView notesView = container.findViewById(R.id.list_items);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        notesView.setLayoutManager(new LinearLayoutManager(getContext()));


        final Cursor noteCursor = createCursorForNoteInfo();

        adapter = new NoteItemListRecyclerAdapter(noteCursor,getContext());
        notesView.setAdapter(adapter);
    }

    private Cursor createCursorForNoteInfo() {
        final String[] noteInfoColumns = {BaseColumns._ID, NoteInfoEntry.COLUMN_COURSE_ID, NoteInfoEntry.COLUMN_NOTE_TEXT, NoteInfoEntry.COLUMN_NOTE_TITLE};
        return openHelper.getReadableDatabase().query(NoteInfoEntry.TABLE_NAME, noteInfoColumns, null, null, null, null, NoteInfoEntry.COLUMN_NOTE_TITLE);
    }

    @Override
    public void onDestroy() {
        openHelper.close();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        adapter.changeCursor(createCursorForNoteInfo());
        super.onResume();
    }
}