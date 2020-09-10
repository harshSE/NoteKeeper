package com.harshdev.notekeeper.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ColumnInfo.INTEGER;
import static androidx.room.ColumnInfo.TEXT;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(
        tableName = "note_info",
        foreignKeys = @ForeignKey(entity = CourseInfo.class,
                parentColumns = "_id",
                childColumns = "course_id",
                onDelete = RESTRICT)
)
public class NoteInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id", typeAffinity = INTEGER)
    private int id;

    @NonNull
    @ColumnInfo(name = "course_id", typeAffinity = INTEGER)
    private int courseId;

    @NonNull
    @ColumnInfo(name = "title", typeAffinity = TEXT)
    private String title;

    @ColumnInfo(name = "text", typeAffinity = TEXT)
    private String text;

    public NoteInfo(int id,int courseId, @NonNull String title, String text) {
        this(courseId, title, text);
        this.id = id;
    }

    @Ignore
    public NoteInfo(int courseId, @NonNull String title, String text) {
        this.courseId = courseId;
        this.title = title;
        this.text = text;
    }


    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
