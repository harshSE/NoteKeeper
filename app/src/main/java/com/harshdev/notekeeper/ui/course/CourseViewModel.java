package com.harshdev.notekeeper.ui.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.harshdev.notekeeper.persistance.CourseInfo;
import com.harshdev.notekeeper.persistance.NoteKeeperRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final NoteKeeperRepository repository;
    private final LiveData<List<CourseInfo>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteKeeperRepository(application);
        allCourses = repository.getAllCourses();
    }


    public LiveData<List<CourseInfo>> getAllCourses() {
        return allCourses;
    }
}
