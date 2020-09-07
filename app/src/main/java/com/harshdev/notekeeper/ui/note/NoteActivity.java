package com.harshdev.notekeeper.ui.note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.harshdev.notekeeper.R;
import com.harshdev.notekeeper.data.CourseInfo;
import com.harshdev.notekeeper.data.DataManager;
import com.harshdev.notekeeper.data.NoteInfo;
import com.harshdev.notekeeper.db.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.harshdev.notekeeper.db.NoteKeeperOpenHelper;

import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_ID = NoteActivity.class.getPackage().getName() + "NOTE_POSITION";
    private static final int NO_ID_SET = -1;
    private final DataManager dataManager;
    private NoteActivityViewModel noteActivityViewModel;
    private Spinner courseSpinner;
    private EditText titleTextField;
    private EditText textNoteTextField;
    private NoteInfo currentNote;
    private boolean isCancelled;
    private NoteKeeperOpenHelper openHelper;
    private SimpleCursorAdapter courseAdapter;

    public NoteActivity() {
        dataManager = DataManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openHelper = new NoteKeeperOpenHelper(this);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));

        noteActivityViewModel = viewModelProvider.get(NoteActivityViewModel.class);



        if(Objects.nonNull(savedInstanceState)) {
            noteActivityViewModel.restoreState(savedInstanceState);
        }

        courseSpinner = findViewById(R.id.course);


        courseAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                null,
                new String[]{CourseInfoEntry.COLUMN_COURSE_TITLE},
                new int[]{android.R.id.text1},0);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        courseSpinner.setAdapter(courseAdapter);

        loadCourseData();


        titleTextField = findViewById(R.id.title);
        textNoteTextField = findViewById(R.id.textNote);

        currentNote = readDisplayStateValue();
        if(Objects.nonNull(currentNote)) {
            saveOriginalNotes();
            displayNote(currentNote);
        }
    }

    private void loadCourseData() {
        final SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        String [] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_ID,
                CourseInfoEntry._ID,
        };

        Cursor cursor = readableDatabase.query(CourseInfoEntry.TABLE_NAME, courseColumns, null, null, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE);
        courseAdapter.changeCursor(cursor);
    }

    private void saveOriginalNotes() {
        noteActivityViewModel.setOriginalValueOfText(currentNote.getText());
        noteActivityViewModel.setOriginalValueOfTitle(currentNote.getTitle());
        noteActivityViewModel.setOriginalValueOfCourseId(currentNote.getCourse().getCourseId());
    }

    private void displayNote(NoteInfo noteInfo) {
        courseSpinner.setSelection(getCursorIndexOfCourse(noteInfo));
        titleTextField.setText(noteInfo.getTitle());
        textNoteTextField.setText(noteInfo.getText());
    }

    private int getCursorIndexOfCourse(NoteInfo noteInfo) {

        final Cursor cursor = courseAdapter.getCursor();
        final int columnIndex = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);

        boolean more = cursor.moveToFirst();
        int index = 0;
        while(more) {
            String courseId = cursor.getString(columnIndex);
            if (courseId.equals(noteInfo.getCourse().getCourseId())) {
                break;
            }
            index++;
            more = cursor.moveToNext();
        }

        return index;


    }

    private NoteInfo readDisplayStateValue() {
        int id = getNoteId();
        return id != NO_ID_SET ? loadNoteInfo(id) : null;
    }

    private NoteInfo loadNoteInfo(int id) {
        return dataManager.getNoteInfo(id, openHelper);
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
            nextItem();
        }

        return super.onOptionsItemSelected(item);
    }

    private void nextItem() {
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

        int cursorIndex =  courseSpinner.getSelectedItemPosition();
        final Cursor cursor = courseAdapter.getCursor();
        final int idColumnIndex = cursor.getColumnIndex(BaseColumns._ID);
        CourseInfo course = null;
        if(cursor.move(cursorIndex)) {
            final int id = cursor.getInt(idColumnIndex);
            course = DataManager.getInstance().getCourseById(id, openHelper);
        }

        String title = titleTextField.getText().toString();
        String text = textNoteTextField.getText().toString();
        if(Objects.isNull(currentNote)) {
            if(Objects.nonNull(course)
                    && Objects.nonNull(title) && !title.isEmpty()) {
                NoteInfo newNote = new NoteInfo(0, course, title, text);
                dataManager.getNotes().add(newNote);
            }

        } else {
            if(Objects.nonNull(course)) {
                currentNote.setCourse(course);
            }
            currentNote.setTitle(title);
            currentNote.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        openHelper.close();
        super.onDestroy();
    }
}
