package com.harshdev.notekeeper.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.harshdev.notekeeper.db.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.harshdev.notekeeper.db.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.harshdev.notekeeper.db.NoteKeeperOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager ourInstance = null;

    private List<CourseInfo> mCourses = new ArrayList<>();
    private List<NoteInfo> mNotes = new ArrayList<>();

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
            /*ourInstance.initializeCourses();
            ourInstance.initializeExampleNotes();*/
        }
        return ourInstance;
    }

    public static void loadFromDatabase(NoteKeeperOpenHelper dbHelper) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        loadCourse(db);
        loadSampleNoteInfo(db);
    }

    private static void loadSampleNoteInfo(SQLiteDatabase db) {
        final String[] noteInfoColumns = {BaseColumns._ID,NoteInfoEntry.COLUMN_COURSE_ID, NoteInfoEntry.COLUMN_NOTE_TEXT, NoteInfoEntry.COLUMN_NOTE_TITLE};
        try (final Cursor noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteInfoColumns, null, null, null, null, NoteInfoEntry.COLUMN_NOTE_TITLE);) {
            final int noteTextIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
            final int noteTitleIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
            final int courseIdIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
            final int idIndex = noteCursor.getColumnIndex(BaseColumns._ID);

            final DataManager dataManager = DataManager.getInstance();
            dataManager.mNotes.clear();
            while (noteCursor.moveToNext()) {
                String noteText = noteCursor.getString(noteTextIndex);
                String noteTitle = noteCursor.getString(noteTitleIndex);
                String courseId = noteCursor.getString(courseIdIndex);
                int id = noteCursor.getInt(idIndex);

                NoteInfo noteInfo = new NoteInfo(id, dataManager.getCourse(courseId), noteTitle, noteText);
                dataManager.mNotes.add(noteInfo);
            }
        }


    }

    private static void loadCourse(SQLiteDatabase db) {
        final String[] courseInfoColumns = {CourseInfoEntry.COLUMN_COURSE_ID, CourseInfoEntry.COLUMN_COURSE_TITLE};

        try (final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseInfoColumns, null, null, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE)) {

            final int courseIdIndex = courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
            final int courseTitleIndex = courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);

            final DataManager dataManager = DataManager.getInstance();

            dataManager.getCourses().clear();

            while (courseCursor.moveToNext()) {
                String id = courseCursor.getString(courseIdIndex);
                String title = courseCursor.getString(courseTitleIndex);

                CourseInfo courseInfo = new CourseInfo(id, title, null);
                dataManager.getCourses().add(courseInfo);
            }
        }
    }

    public String getCurrentUserName() {
        return "Jim Wilson";
    }

    public String getCurrentUserEmail() {
        return "jimw@jwhh.com";
    }

    public List<NoteInfo> getNotes() {
        return mNotes;
    }

    public int createNewNote() {
        NoteInfo note = new NoteInfo(0, null, null, null);
        mNotes.add(note);
        return mNotes.size() - 1;
    }

    public int findNote(NoteInfo note) {
        for (int index = 0; index < mNotes.size(); index++) {
            if (note.equals(mNotes.get(index)))
                return index;
        }

        return -1;
    }

    public void removeNote(int index) {
        mNotes.remove(index);
    }

    public List<CourseInfo> getCourses() {
        return mCourses;
    }

    public CourseInfo getCourse(String id) {
        for (CourseInfo course : mCourses) {
            if (id.equals(course.getCourseId()))
                return course;
        }
        return null;
    }

    public List<NoteInfo> getNotes(CourseInfo course) {
        ArrayList<NoteInfo> notes = new ArrayList<>();
        for (NoteInfo note : mNotes) {
            if (course.equals(note.getCourse()))
                notes.add(note);
        }
        return notes;
    }

    public int getNoteCount(CourseInfo course) {
        int count = 0;
        for (NoteInfo note : mNotes) {
            if (course.equals(note.getCourse()))
                count++;
        }
        return count;
    }

    private DataManager() {
    }

    //region Initialization code

    private void initializeCourses() {
        mCourses.add(initializeCourse1());
        mCourses.add(initializeCourse2());
        mCourses.add(initializeCourse3());
        mCourses.add(initializeCourse4());
    }

    public void initializeExampleNotes() {
        final DataManager dm = getInstance();

        CourseInfo course = dm.getCourse("android_intents");
        course.getModule("android_intents_m01").setComplete(true);
        course.getModule("android_intents_m02").setComplete(true);
        course.getModule("android_intents_m03").setComplete(true);
        mNotes.add(new NoteInfo(0, course, "Dynamic intent resolution",
                "Wow, intents allow components to be resolved at runtime"));
        mNotes.add(new NoteInfo(0, course, "Delegating intents",
                "PendingIntents are powerful; they delegate much more than just a component invocation"));

        course = dm.getCourse("android_async");
        course.getModule("android_async_m01").setComplete(true);
        course.getModule("android_async_m02").setComplete(true);
        mNotes.add(new NoteInfo(0, course, "Service default threads",
                "Did you know that by default an Android Service will tie up the UI thread?"));
        mNotes.add(new NoteInfo(0, course, "Long running operations",
                "Foreground Services can be tied to a notification icon"));

        course = dm.getCourse("java_lang");
        course.getModule("java_lang_m01").setComplete(true);
        course.getModule("java_lang_m02").setComplete(true);
        course.getModule("java_lang_m03").setComplete(true);
        course.getModule("java_lang_m04").setComplete(true);
        course.getModule("java_lang_m05").setComplete(true);
        course.getModule("java_lang_m06").setComplete(true);
        course.getModule("java_lang_m07").setComplete(true);
        mNotes.add(new NoteInfo(0, course, "Parameters",
                "Leverage variable-length parameter lists"));
        mNotes.add(new NoteInfo(0, course, "Anonymous classes",
                "Anonymous classes simplify implementing one-use types"));

        course = dm.getCourse("java_core");
        course.getModule("java_core_m01").setComplete(true);
        course.getModule("java_core_m02").setComplete(true);
        course.getModule("java_core_m03").setComplete(true);
        mNotes.add(new NoteInfo(0, course, "Compiler options",
                "The -jar option isn't compatible with with the -cp option"));
        mNotes.add(new NoteInfo(0, course, "Serialization",
                "Remember to include SerialVersionUID to assure version compatibility"));
    }

    private CourseInfo initializeCourse1() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("android_intents_m01", "Android Late Binding and Intents"));
        modules.add(new ModuleInfo("android_intents_m02", "Component activation with intents"));
        modules.add(new ModuleInfo("android_intents_m03", "Delegation and Callbacks through PendingIntents"));
        modules.add(new ModuleInfo("android_intents_m04", "IntentFilter data tests"));
        modules.add(new ModuleInfo("android_intents_m05", "Working with Platform Features Through Intents"));

        return new CourseInfo("android_intents", "Android Programming with Intents", modules);
    }

    private CourseInfo initializeCourse2() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("android_async_m01", "Challenges to a responsive user experience"));
        modules.add(new ModuleInfo("android_async_m02", "Implementing long-running operations as a service"));
        modules.add(new ModuleInfo("android_async_m03", "Service lifecycle management"));
        modules.add(new ModuleInfo("android_async_m04", "Interacting with services"));

        return new CourseInfo("android_async", "Android Async Programming and Services", modules);
    }

    private CourseInfo initializeCourse3() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("java_lang_m01", "Introduction and Setting up Your Environment"));
        modules.add(new ModuleInfo("java_lang_m02", "Creating a Simple App"));
        modules.add(new ModuleInfo("java_lang_m03", "Variables, Data Types, and Math Operators"));
        modules.add(new ModuleInfo("java_lang_m04", "Conditional Logic, Looping, and Arrays"));
        modules.add(new ModuleInfo("java_lang_m05", "Representing Complex Types with Classes"));
        modules.add(new ModuleInfo("java_lang_m06", "Class Initializers and Constructors"));
        modules.add(new ModuleInfo("java_lang_m07", "A Closer Look at Parameters"));
        modules.add(new ModuleInfo("java_lang_m08", "Class Inheritance"));
        modules.add(new ModuleInfo("java_lang_m09", "More About Data Types"));
        modules.add(new ModuleInfo("java_lang_m10", "Exceptions and Error Handling"));
        modules.add(new ModuleInfo("java_lang_m11", "Working with Packages"));
        modules.add(new ModuleInfo("java_lang_m12", "Creating Abstract Relationships with Interfaces"));
        modules.add(new ModuleInfo("java_lang_m13", "Static Members, Nested Types, and Anonymous Classes"));

        return new CourseInfo("java_lang", "Java Fundamentals: The Java Language", modules);
    }

    private CourseInfo initializeCourse4() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("java_core_m01", "Introduction"));
        modules.add(new ModuleInfo("java_core_m02", "Input and Output with Streams and Files"));
        modules.add(new ModuleInfo("java_core_m03", "String Formatting and Regular Expressions"));
        modules.add(new ModuleInfo("java_core_m04", "Working with Collections"));
        modules.add(new ModuleInfo("java_core_m05", "Controlling App Execution and Environment"));
        modules.add(new ModuleInfo("java_core_m06", "Capturing Application Activity with the Java Log System"));
        modules.add(new ModuleInfo("java_core_m07", "Multithreading and Concurrency"));
        modules.add(new ModuleInfo("java_core_m08", "Runtime Type Information and Reflection"));
        modules.add(new ModuleInfo("java_core_m09", "Adding Type Metadata with Annotations"));
        modules.add(new ModuleInfo("java_core_m10", "Persisting Objects with Serialization"));

        return new CourseInfo("java_core", "Java Fundamentals: The Core Platform", modules);
    }

    public NoteInfo getNoteInfo(int id, NoteKeeperOpenHelper openHelper) {



        final String[] noteInfoColumns = {NoteInfoEntry.COLUMN_COURSE_ID, NoteInfoEntry.COLUMN_NOTE_TEXT, NoteInfoEntry.COLUMN_NOTE_TITLE};
        try (final SQLiteDatabase db = openHelper.getReadableDatabase();
                final Cursor noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteInfoColumns, BaseColumns._ID + "= ?", new String[]{String.valueOf(id)}, null, null, null);) {
            final int noteTextIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
            final int noteTitleIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
            final int courseIdIndex = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);


            final DataManager dataManager = DataManager.getInstance();
            if (noteCursor.moveToNext()) {
                String noteText = noteCursor.getString(noteTextIndex);
                String noteTitle = noteCursor.getString(noteTitleIndex);
                String courseId = noteCursor.getString(courseIdIndex);

               return new NoteInfo(id, dataManager.getCourse(courseId), noteTitle, noteText);
            }
        }
        return null;
    }

    public CourseInfo getCourseById(int id, NoteKeeperOpenHelper openHelper) {
        final String[] courseInfoColumns = {CourseInfoEntry.COLUMN_COURSE_ID, CourseInfoEntry.COLUMN_COURSE_TITLE};

        try (final SQLiteDatabase db = openHelper.getReadableDatabase();
                final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseInfoColumns, BaseColumns._ID + "= ?", new String[]{String.valueOf(id)}, null, null, null)) {

            final int courseIdIndex = courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
            final int courseTitleIndex = courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);

            if (courseCursor.moveToNext()) {
                String courseId = courseCursor.getString(courseIdIndex);
                String title = courseCursor.getString(courseTitleIndex);

                return new CourseInfo(courseId, title, null);
            }
        }

        return null;
    }
    //endregion

}
