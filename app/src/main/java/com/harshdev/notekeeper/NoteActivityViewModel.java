package com.harshdev.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {


    public static final String PACKAGE_NAME = NoteActivityViewModel.class.getPackage().getName();
    private static final String ORIGINAL_NOTE_COURSE_ID = PACKAGE_NAME + ".ORIGINAL_NOTE_COURSE_ID";
    private static final String ORIGINAL_NOTE_TEXT = PACKAGE_NAME + ".ORIGINAL_NOTE_TEXT";
    private static final String ORIGINAL_NOTE_TITLE = PACKAGE_NAME + ".ORIGINAL_NOTE_TITLE";

    private String originalValueOfCourseId;
    private String originalValueOfText;
    private String originalValueOfTitle;

    public NoteActivityViewModel() {

    }

    public String getOriginalValueOfCourseId() {
        return originalValueOfCourseId;
    }

    public void setOriginalValueOfCourseId(String originalValueOfCourseId) {
        this.originalValueOfCourseId = originalValueOfCourseId;
    }

    public String getOriginalValueOfText() {
        return originalValueOfText;
    }

    public void setOriginalValueOfText(String originalValueOfText) {
        this.originalValueOfText = originalValueOfText;
    }

    public String getOriginalValueOfTitle() {
        return originalValueOfTitle;
    }

    public void setOriginalValueOfTitle(String originalValueOfTitle) {
        this.originalValueOfTitle = originalValueOfTitle;
    }

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, getOriginalValueOfCourseId());
        outState.putString(ORIGINAL_NOTE_TEXT, getOriginalValueOfTitle());
        outState.putString(ORIGINAL_NOTE_TITLE,getOriginalValueOfTitle());
    }

    public void restoreState(Bundle inState) {
        originalValueOfCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalValueOfTitle = inState.getString(ORIGINAL_NOTE_TITLE);
        originalValueOfText = inState.getString(ORIGINAL_NOTE_TEXT);
    }
}