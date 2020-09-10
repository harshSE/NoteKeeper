package com.harshdev.notekeeper.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.persistance.CourseInfo;

import java.util.List;

public class CoursesFragment extends Fragment {


    private CourseListRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        initializeDisplayContent(root);

        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courses) {
                adapter.setCourses(courses);
            }
        });


        return root;
    }

    private void initializeDisplayContent(View root) {

        RecyclerView notesView = root.findViewById(R.id.list_courses);

        notesView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new CourseListRecyclerAdapter(getContext());
        notesView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}