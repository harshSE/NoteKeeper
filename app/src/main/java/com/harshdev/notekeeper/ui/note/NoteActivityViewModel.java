package com.harshdev.notekeeper.ui.note;

import android.app.Application;
import android.os.Bundle;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.harshdev.notekeeper.persistance.CourseInfo;
import com.harshdev.notekeeper.persistance.NoteDetail;
import com.harshdev.notekeeper.persistance.NoteInfo;
import com.harshdev.notekeeper.persistance.NoteKeeperRepository;

import java.util.List;
import java.util.Objects;

public class NoteActivityViewModel extends AndroidViewModel {


    public static final String PACKAGE_NAME = NoteActivityViewModel.class.getPackage().getName();
    private static final String ORIGINAL_NOTE_COURSE_ID = PACKAGE_NAME + ".ORIGINAL_NOTE_COURSE_ID";
    private static final String ORIGINAL_NOTE_TEXT = PACKAGE_NAME + ".ORIGINAL_NOTE_TEXT";
    private static final String ORIGINAL_NOTE_TITLE = PACKAGE_NAME + ".ORIGINAL_NOTE_TITLE";

    private int originalValueOfCourseId;
    private String originalValueOfText;
    private String originalValueOfTitle;
    private final LiveData<List<CourseInfo>> courses;
    private final NoteKeeperRepository noteKeeperRepository;

    public NoteActivityViewModel(Application application) {
        super(application);
        noteKeeperRepository = new NoteKeeperRepository(application);
        courses = noteKeeperRepository.getAllCourses();

    }

    public int getOriginalValueOfCourseId() {
        return originalValueOfCourseId;
    }

    public String getOriginalValueOfText() {
        return originalValueOfText;
    }

    public String getOriginalValueOfTitle() {
        return originalValueOfTitle;
    }


    public LiveData<List<CourseInfo>> getCourses() {
        return courses;
    }

    public LiveData<NoteDetail> getNoteDetail(int id) {
        return noteKeeperRepository.getNoteDetail(id);
    }

    public void save(NoteInfo noteInfo) {
        noteKeeperRepository.save(noteInfo);
    }

    public void update(NoteInfo noteInfo) {
        noteKeeperRepository.update(noteInfo);
    }

    public void saveState(Bundle outState, NoteDetail noteDetail) {
        if(originalValueOfCourseId == 0 && Objects.isNull(originalValueOfTitle)) {
            outState.putInt(ORIGINAL_NOTE_COURSE_ID, noteDetail.getCourse().getId());
            outState.putString(ORIGINAL_NOTE_TEXT, noteDetail.getText());
            outState.putString(ORIGINAL_NOTE_TITLE,noteDetail.getTitle());
        } else {
            outState.putInt(ORIGINAL_NOTE_COURSE_ID, getOriginalValueOfCourseId());
            outState.putString(ORIGINAL_NOTE_TEXT, getOriginalValueOfText());
            outState.putString(ORIGINAL_NOTE_TITLE,getOriginalValueOfTitle());
        }
    }

    public void restoreState(Bundle inState) {
        originalValueOfCourseId = inState.getInt(ORIGINAL_NOTE_COURSE_ID);
        originalValueOfTitle = inState.getString(ORIGINAL_NOTE_TITLE);
        originalValueOfText = inState.getString(ORIGINAL_NOTE_TEXT);
    }
}