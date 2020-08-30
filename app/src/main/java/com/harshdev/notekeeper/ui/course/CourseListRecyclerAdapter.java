package com.harshdev.notekeeper.ui.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.data.CourseInfo;
import com.harshdev.notekeeper.R;

import java.util.List;

public class CourseListRecyclerAdapter extends RecyclerView.Adapter<CourseListRecyclerAdapter.CourseItemViewHolder> {

    private final List<CourseInfo> courses;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public CourseListRecyclerAdapter(List<CourseInfo> courses, Context context) {
        this.courses = courses;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CourseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new CourseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseItemViewHolder holder, int position) {
        holder.setNoteInfo(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseItemViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;

        public CourseItemViewHolder(@NonNull View view) {
            super(view);
            titleView = view.findViewById(R.id.text_title);
            attachListener(view);
        }

        private void attachListener(View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void setNoteInfo(CourseInfo courseInfo) {
            titleView.setText(courseInfo.getTitle());
        }
    }

}
