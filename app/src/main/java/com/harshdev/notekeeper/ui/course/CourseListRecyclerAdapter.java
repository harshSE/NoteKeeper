package com.harshdev.notekeeper.ui.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.persistance.CourseInfo;

import java.util.List;
import java.util.Objects;

public class CourseListRecyclerAdapter extends RecyclerView.Adapter<CourseListRecyclerAdapter.CourseItemViewHolder> {

    private @Nullable List<CourseInfo> courses;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public CourseListRecyclerAdapter(Context context) {
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
        if(Objects.isNull(courses)) {
            return 0;
        } else {
            return courses.size();
        }
    }

    public void setCourses(List<CourseInfo> courses) {
        this.courses = courses;
        notifyDataSetChanged();
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
