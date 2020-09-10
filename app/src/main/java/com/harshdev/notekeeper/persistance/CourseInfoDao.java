package com.harshdev.notekeeper.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseInfoDao {

    @Insert
    long insert(CourseInfo courseInfo);


    @Query("SELECT * FROM course_info ORDER BY title")
    LiveData<List<CourseInfo>> getAllCourses();
}
