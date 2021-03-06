package com.harshdev.notekeeper.persistance;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteKeeperRepository {

    private final NoteKeeperDatabase db;
    private final CourseInfoDao courseInfoDao;
    private final NoteInfoDao noteInfoDao;

    public NoteKeeperRepository(Application application) {
        db = NoteKeeperDatabase.getInstance(application);
        courseInfoDao = db.getCourseInfoDao();
        noteInfoDao = db.getNoteInfoDao();
    }

    public LiveData<List<CourseInfo>> getAllCourses() {
        return courseInfoDao.getAllCourses();
    }

    public LiveData<List<NoteInfo>> getAllNoteInfo() {
        return noteInfoDao.getAllNoteInfo();
    }

    public LiveData<List<NoteInfo>> getAllNotes() {
        return noteInfoDao.getAllNoteInfo();
    }

    public LiveData<List<NoteDetail>> getAllNoteDetails() {
        return noteInfoDao.getAllNoteDetails();
    }

    public LiveData<NoteDetail> getNoteDetail(int id) {
        return noteInfoDao.getNoteDetail(id);
    }

    public void save(final NoteInfo noteInfo) {
        db.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteInfoDao.insert(noteInfo);
            }
        });
    }

    public void update(final NoteInfo noteInfo) {

        db.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteInfoDao.update(noteInfo);
            }
        });

    }
}
