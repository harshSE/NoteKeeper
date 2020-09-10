package com.harshdev.notekeeper.ui.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.harshdev.notekeeper.persistance.NoteDetail;
import com.harshdev.notekeeper.persistance.NoteKeeperRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private final NoteKeeperRepository repository;
    private final LiveData<List<NoteDetail>> notes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteKeeperRepository(application);
        notes = repository.getAllNoteDetails();

    }

    public LiveData<List<NoteDetail>> getAllNoteDetails() {
        return notes;
    }
}
