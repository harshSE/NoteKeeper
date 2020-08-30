package com.harshdev.notekeeper;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.harshdev.notekeeper.data.DataManager;
import com.harshdev.notekeeper.data.NoteInfo;
import com.harshdev.notekeeper.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.contrib.NavigationViewActions.navigateTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NextThroughNoteTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = DataManager.getInstance();
    }

    @Test
    public void nextThroughNotes() {
        selectNotes();
        selectFirstNote();
        List<NoteInfo> notes = dataManager.getNotes();
        for (NoteInfo note : notes) {
            checkNoteDetail(note);
            clickNext();
        }

        checkNoteDetail(notes.get(0));
        pressBack();
    }

    private void clickNext() {
        nextButton().perform(click());
    }

    private ViewInteraction nextButton() {
        return onView(withId(R.id.action_next));
    }

    private void checkNoteDetail(NoteInfo note) {
        title().check(matches(withText(note.getTitle())));
        course().check(matches(withSpinnerText(note.getCourse().getTitle())));
        textNote().check(matches(withText(note.getText())));

    }

    private ViewInteraction textNote() {
        return onView(withId(R.id.textNote));
    }

    private ViewInteraction course() {
        return onView(withId(R.id.course));
    }

    private ViewInteraction title() {
        return onView(withId(R.id.title));
    }

    private void selectFirstNote() {
        onView(withId(R.id.list_items)).perform(actionOnItemAtPosition(0, click()));
    }

    private void selectNotes() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_notes));
    }
}
