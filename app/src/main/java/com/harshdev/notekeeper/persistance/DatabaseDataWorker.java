package com.harshdev.notekeeper.persistance;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.harshdev.notekeeper.persistance.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.harshdev.notekeeper.persistance.NoteKeeperDatabaseContract.NoteInfoEntry;

import java.util.HashMap;

/**
 * Created by Jim.
 */

public class DatabaseDataWorker {
    public static final String ANDROID_INTENTS = "android_intents";
    public static final String ANDROID_ASYNC = "android_async";
    public static final String JAVA_LANG = "java_lang";
    public static final String JAVA_CORE = "java_core";
    private SupportSQLiteDatabase mDb;
    private final HashMap<String, Long> courseIdToId;

    public DatabaseDataWorker(SupportSQLiteDatabase db) {
        mDb = db;
        courseIdToId = new HashMap<>();
    }

    public void insertCourses() {
        courseIdToId.put(ANDROID_INTENTS, insertCourse("android_intents", "Android Programming with Intents"));
        courseIdToId.put(ANDROID_ASYNC, insertCourse(ANDROID_ASYNC, "Android Async Programming and Services"));
        courseIdToId.put(JAVA_LANG, insertCourse(JAVA_LANG, "Java Fundamentals: The Java Language"));
        courseIdToId.put(JAVA_CORE, insertCourse(JAVA_CORE, "Java Fundamentals: The Core Platform"));
    }

    public void insertSampleNotes() {
        insertNote(courseIdToId.get(ANDROID_INTENTS), "Dynamic intent resolution", "Wow, intents allow components to be resolved at runtime");
        insertNote(courseIdToId.get(ANDROID_INTENTS), "Delegating intents", "PendingIntents are powerful; they delegate much more than just a component invocation");

        insertNote(courseIdToId.get(ANDROID_ASYNC), "Service default threads", "Did you know that by default an Android Service will tie up the UI thread?");
        insertNote(courseIdToId.get(ANDROID_ASYNC), "Long running operations", "Foreground Services can be tied to a notification icon");

        insertNote(courseIdToId.get(JAVA_LANG), "Parameters", "Leverage variable-length parameter lists?");
        insertNote(courseIdToId.get(JAVA_LANG), "Anonymous classes", "Anonymous classes simplify implementing one-use types");

        insertNote(courseIdToId.get(JAVA_CORE), "Compiler options", "The -jar option isn't compatible with with the -cp option");
        insertNote(courseIdToId.get(JAVA_CORE), "Serialization", "Remember to include SerialVersionUID to assure version compatibility");
    }

    private long insertCourse(String courseId, String title) {
        ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_ID, courseId);
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, title);

        return mDb.insert(CourseInfoEntry.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);
    }

    private void insertNote(long courseId, String title, String text) {
        ContentValues values = new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID, courseId);
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE, title);
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT, text);

        mDb.insert(NoteInfoEntry.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);
    }

}
