package com.harshdev.notekeeper;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public class NodeCreationTest {

    private static DataManager dataManager;
    @Rule public ActivityScenarioRule<NoteListActivity> noteListActivity = new ActivityScenarioRule<>(NoteListActivity.class);

    @BeforeClass
    public static void setUpClass() {
        dataManager = DataManager.getInstance();
    }


    @Test
    public void createNewNote() {
        CourseInfo course = dataManager.getCourse("java_lang");
        String title = "testNote";
        String text = "testNote text";


        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.course)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click());
        onView(withId(R.id.title)).perform(typeText(title));
        onView(withId(R.id.textNote)).perform(typeText(text),
                 closeSoftKeyboard());

         pressBack();


        NoteInfo noteInfo = dataManager.getNotes().get(lastIndex());
        assertSame(course, noteInfo.getCourse());
        assertEquals(title, noteInfo.getTitle());
        assertEquals(text, noteInfo.getText());



    }

    private int lastIndex() {
        return dataManager.getNotes().size() - 1;
    }

}