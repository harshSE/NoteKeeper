package com.harshdev.notekeeper.persistance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Objects;

@Database(entities = {CourseInfo.class, NoteInfo.class}, version = 1)
public abstract class NoteKeeperDatabase extends RoomDatabase {


    public abstract CourseInfoDao getCourseInfoDao();

    public abstract NoteInfoDao getNoteInfoDao();

    public static volatile NoteKeeperDatabase noteKeeperDatabase;

    static NoteKeeperDatabase getInstance(final Context context) {

        if (Objects.isNull(noteKeeperDatabase)) {
            synchronized (NoteKeeperDatabase.class) {
                if (Objects.isNull(noteKeeperDatabase)) {
                    noteKeeperDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            NoteKeeperDatabase.class, "note-keeper.bd").addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            DatabaseDataWorker worker = new DatabaseDataWorker(db);
                            worker.insertCourses();
                            worker.insertSampleNotes();
                        }
                    }).build();

                }
            }
        }

        return noteKeeperDatabase;
    }


}
