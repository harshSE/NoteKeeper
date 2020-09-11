package com.harshdev.notekeeper.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.persistance.CourseInfo;
import com.harshdev.notekeeper.persistance.NoteDetail;
import com.harshdev.notekeeper.persistance.NoteInfo;

import java.util.List;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_ID = NoteActivity.class.getPackage().getName() + "NOTE_POSITION";
    private static final int NO_ID_SET = -1;
    private NoteActivityViewModel model;
    private Spinner courseSpinner;
    private EditText titleTextField;
    private EditText textNoteTextField;
    private boolean isCancelled;
    private NoteDetail currentNote;
    private ArrayAdapter<CourseInfo> courseAdapter;

    public NoteActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));

        model = viewModelProvider.get(NoteActivityViewModel.class);


        courseSpinner = findViewById(R.id.course);


        courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new CourseInfo[0]);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        courseSpinner.setAdapter(courseAdapter);


        titleTextField = findViewById(R.id.title);
        textNoteTextField = findViewById(R.id.textNote);

        readDisplayStateValue();
    }







    private void readDisplayStateValue() {
        int id = getNoteId();
        if(id == NO_ID_SET) {
            loadCourseData();
        } else {
            loadNoteInfo(id);
        }
    }

    private void loadNoteInfo(int id) {
        model.getNoteDetail(id).observe(this, new Observer<NoteDetail>() {
            @Override
            public void onChanged(NoteDetail noteDetail) {
                currentNote = noteDetail;
                loadCourseData();
                displayNote(noteDetail);
            }
        });
    }

    private void displayNote(NoteDetail noteInfo) {
        titleTextField.setText(noteInfo.getTitle());
        textNoteTextField.setText(noteInfo.getText());
    }

    private void loadCourseData() {
        model.getCourses().observe(this, new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                onChange(courseInfos);
            }
        });
    }

    private void onChange(List<CourseInfo> courseInfos) {
        CourseInfo selectedCourse = (CourseInfo) courseSpinner.getSelectedItem();
        courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseInfos);
        courseSpinner.setAdapter(courseAdapter);


        if(Objects.isNull(selectedCourse)) {
            if(Objects.nonNull(currentNote)) {
                selectedCourse = currentNote.getCourse();
            }
        }

        if(Objects.nonNull(selectedCourse)) {
            for (int index = 0; index < courseInfos.size(); index++) {
                CourseInfo courseInfo = courseInfos.get(index);
                if (courseInfo.getId() == selectedCourse.getId()) {
                    courseSpinner.setSelection(index);
                    break;
                }
            }
        } else {
            courseSpinner.setSelection(0);
        }

    }

    private int getNoteId() {
        Intent intent = getIntent();
        return intent.getIntExtra(NOTE_ID, NO_ID_SET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
            return true;
        } else if(id == R.id.action_cancel) {
            this.isCancelled = true;
            finish();
        } else if (id == R.id.action_next) {
            // nextItem();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(Objects.nonNull(currentNote)) {
            model.saveState(outState, currentNote);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        model.restoreState(savedInstanceState);
    }

    /*private void nextItem() {
        saveNotes();

        this.currentNote = getNextNote();

        saveOriginalNotes();
        displayNote(currentNote);
    }

    private NoteInfo getNextNote() {
        int position = dataManager.findNote(currentNote);
        if(isEndOfList(position)){
            return dataManager.getNotes().get(0);
        } else {
            return dataManager.getNotes().get(++position);
        }
    }

    private boolean isEndOfList(int position) {
        return position == DataManager.getInstance().getNotes().size() - 1;
    }
*/
    private void sendEmail() {

        CourseInfo courseInfo = (CourseInfo) courseSpinner.getSelectedItem();
        String subject = titleTextField.getText().toString();
        String text = "Checkout what I learned on plursight course \n"
                + courseInfo.getTitle()
                + "\"\n" + textNoteTextField.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!isCancelled) {
            saveNotes();
        } else {
            restoreValue();
        }
    }

    private void restoreValue() {
        if (Objects.isNull(currentNote)) {
            return;
        }

        model.update(new NoteInfo(model.getOriginalValueOfCourseId(), model.getOriginalValueOfTitle(),model.getOriginalValueOfText()));

    }



    private void saveNotes() {
        CourseInfo course = (CourseInfo) courseSpinner.getSelectedItem();
        String title = titleTextField.getText().toString();
        String text = textNoteTextField.getText().toString();
        if(Objects.isNull(currentNote)) {
            if(Objects.nonNull(course) && Objects.nonNull(title)) {
                model.save(new NoteInfo(course.getId(), title, text));
            }
        } else {
            model.update(new NoteInfo(currentNote.getId(),course.getId(), title, text));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
