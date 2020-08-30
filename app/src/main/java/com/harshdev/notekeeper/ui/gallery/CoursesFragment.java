package com.harshdev.notekeeper.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.data.CourseInfo;
import com.harshdev.notekeeper.data.DataManager;
import com.harshdev.notekeeper.R;

import java.util.List;

public class CoursesFragment extends Fragment {

    private CourseListRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        initializeDisplayContent(root);
        return root;
    }

    private void initializeDisplayContent(View root) {

        RecyclerView notesView = root.findViewById(R.id.list_courses);

        List<CourseInfo> notes = DataManager.getInstance().getCourses();

        notesView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new CourseListRecyclerAdapter(notes,getContext());
        notesView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}