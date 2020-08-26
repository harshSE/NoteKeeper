package com.harshdev.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_POSITION = NoteActivity.class.getPackage().getName() + "NOTE_POSITION";
    private static final int NO_POSITION_SET = -1;
    private final DataManager dataManager;
    private NoteActivityViewModel noteActivityViewModel;
    private Spinner courseSpinner;
    private EditText titleTextField;
    private EditText textNoteTextField;
    private NoteInfo currentNote;
    private boolean isCancelled;

    public NoteActivity() {
        dataManager = DataManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));

        noteActivityViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(Objects.nonNull(savedInstanceState)) {
            noteActivityViewModel.restoreState(savedInstanceState);
        }

        courseSpinner = findViewById(R.id.course);


        List<CourseInfo> courses = dataManager.getCourses();

        ArrayAdapter<CourseInfo> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        courseSpinner.setAdapter(courseAdapter);


        titleTextField = findViewById(R.id.title);
        textNoteTextField = findViewById(R.id.textNote);

        currentNote = readDisplayStateValue();
        if(Objects.nonNull(currentNote)) {
            noteActivityViewModel.setOriginalValueOfText(currentNote.getText());
            noteActivityViewModel.setOriginalValueOfTitle(currentNote.getTitle());
            noteActivityViewModel.setOriginalValueOfCourseId(currentNote.getCourse().getCourseId());
            displayNote(currentNote);
        }
    }

    private void displayNote(NoteInfo noteInfo) {
        courseSpinner.setSelection(dataManager.getCourses().indexOf(noteInfo.getCourse()));
        titleTextField.setText(noteInfo.getTitle());
        textNoteTextField.setText(noteInfo.getText());
    }

    private NoteInfo readDisplayStateValue() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, NO_POSITION_SET);
        return position != NO_POSITION_SET ?  dataManager.getNotes().get(position): null;
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
        }

        return super.onOptionsItemSelected(item);
    }

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

        currentNote.setCourse(dataManager.getCourse(noteActivityViewModel.getOriginalValueOfCourseId()));
        currentNote.setTitle(noteActivityViewModel.getOriginalValueOfTitle());
        currentNote.setText(noteActivityViewModel.getOriginalValueOfText());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(Objects.nonNull(outState)) {
            noteActivityViewModel.saveState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(Objects.nonNull(savedInstanceState)) {
            noteActivityViewModel.restoreState(savedInstanceState);
        }
    }

    private void saveNotes() {


        CourseInfo course = (CourseInfo) courseSpinner.getSelectedItem();
        String title = titleTextField.getText().toString();
        String text = textNoteTextField.getText().toString();
        if(Objects.isNull(currentNote)) {
            if(Objects.nonNull(course)
                    && Objects.nonNull(title) && !title.isEmpty()) {
                NoteInfo newNote = new NoteInfo(course, title, text);
                dataManager.getNotes().add(newNote);
            }

        } else {
            currentNote.setCourse(course);
            currentNote.setTitle(title);
            currentNote.setText(text);
        }
    }
}
