package com.harshdev.notekeeper.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface NoteInfoDao {

    @Query("SELECT * FROM note_info ORDER BY title")
    public LiveData<List<NoteInfo>> getAllNoteInfo();

    @Transaction
    @Query("SELECT * FROM note_info ORDER BY title")
    LiveData<List<NoteDetail>> getAllNoteDetails();
}
