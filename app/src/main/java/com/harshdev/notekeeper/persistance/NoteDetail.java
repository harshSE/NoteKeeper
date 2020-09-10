package com.harshdev.notekeeper.persistance;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

public class NoteDetail {

    @Embedded
    public @NonNull
    final NoteInfo note;
    @Relation(
            parentColumn = "course_id",
            entityColumn = "_id"
    )
    public @NonNull
    final CourseInfo course;

    public NoteDetail(@NonNull NoteInfo note, @NonNull CourseInfo course) {
        this.note = note;
        this.course = course;
    }

    public int getId() {
        return note.getId();
    }

    @NonNull
    public String getTitle() {
        return note.getTitle();
    }

    public String getText() {
        return note.getText();
    }

    public String getCourseTitle() {
        return course.getTitle();
    }

}
