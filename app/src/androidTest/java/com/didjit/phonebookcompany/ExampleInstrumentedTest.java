package com.didjit.phonebookcompany;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Rule
    public ActivityTestRule<MainActivity> mNotesActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.didjit.phonebookcompany", appContext.getPackageName());
    }

    @Test
    public void clickFabButton_opensAddNoteUi() throws Exception {
//        fail("Implement step 7");
        // Click on the add note button
        onView(withId(R.id.savefloatingActionButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());

        // Check if the add note screen is displayed
//        onView(withId(R.id.add_note_title)).check(matches(isDisplayed()));
    }






    @Test
    public void addNoteToNotesList() throws Exception {

//        String newNoteTitle = "Espresso";
//        String newNoteDescription = "UI testing for Android";
//
//        // Click on the add note button
//        onView(withId(R.id.fab_add_notes)).perform(click());
//
//        // Add note title and description
//        // Type new note title
//        onView(withId(R.id.add_note_title)).perform(typeText(newNoteTitle), closeSoftKeyboard());
//        onView(withId(R.id.add_note_description)).perform(typeText(newNoteDescription),
//                closeSoftKeyboard()); // Type new note description and close the keyboard
//
//        // Save the note
//        onView(withId(R.id.fab_add_notes)).perform(click());
//
//        // Scroll notes list to added note, by finding its description
//        onView(withId(R.id.notes_list)).perform(
//                scrollTo(hasDescendant(withText(newNoteDescription))));
//
//        // Verify note is displayed on screen
//        onView(withItemText(newNoteDescription)).check(matches(isDisplayed()));
    }


}
