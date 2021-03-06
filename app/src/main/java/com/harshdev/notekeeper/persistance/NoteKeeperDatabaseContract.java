package com.harshdev.notekeeper.persistance;

import android.provider.BaseColumns;

public class NoteKeeperDatabaseContract {

    public static final class CourseInfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_TITLE = "title";
        public static final String COLUMN_COURSE_ID = "course_id";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY, "+
                COLUMN_COURSE_ID +" TEXT UNIQUE NOT NULL," +
                COLUMN_COURSE_TITLE +" TEXT NOT NULL" +
                ")";

    }

    public static final class NoteInfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "note_info";
        public static final String COLUMN_NOTE_TITLE = "title";
        public static final String COLUMN_NOTE_TEXT = "text";
        public static final String COLUMN_COURSE_ID = "course_id";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NOTE_TITLE + " TEXT NOT NULL," +
                COLUMN_NOTE_TEXT + " TEXT," +
                COLUMN_COURSE_ID + " INTEGER NOT NULL," +
                "FOREIGN KEY("+ COLUMN_COURSE_ID +") REFERENCES " + CourseInfoEntry.TABLE_NAME + "(" + _ID + ")";
    }


}
