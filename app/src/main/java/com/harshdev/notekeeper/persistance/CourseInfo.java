package com.harshdev.notekeeper.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_info",
        indices = {
                @Index(value = "course_id", unique = true),
                @Index(value = "title", unique = true)
        })
public class CourseInfo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "course_id", typeAffinity = 2)
    private final String courseId;

    @NonNull
    @ColumnInfo(name = "title", typeAffinity = 2)
    private final String title;


    public CourseInfo(int id, @NonNull String courseId, @NonNull String title) {
        this(courseId, title);
        this.id = id;
    }

    @Ignore
    public CourseInfo(@NonNull String courseId, @NonNull String title) {
        this.courseId = courseId;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getCourseId() {
        return courseId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }
}
