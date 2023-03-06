package com.actiangent.note.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.MainActivity
import com.actiangent.note.R
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.util.todayDateTimeFormatted
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NoteTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: NoteRepository
    private val note = Note("Note title", "Note content", "1990-01-01 00:00", 1)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun openNote_editNote() {
        // insert note into repository
        repository.insertNoteBlocking(note)

        // open app
        ActivityScenario.launch(MainActivity::class.java)

        // click note on the list
        onView(withText(note.title)).apply {
            check(matches(isDisplayed()))
            perform(click())
        }

        // change note title
        val updatedNoteTitle = "Updated note title"
        onView(withId(R.id.note_title_edit_text)).perform(replaceText(updatedNoteTitle))

        // change note content
        val updatedNoteContent = "Updated note content"
        onView(withId(R.id.note_content_edit_text)).perform(replaceText(updatedNoteContent))

        // verify note datetime to now
        onView(withId(R.id.note_datetime_text_view)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(todayDateTimeFormatted())))
        }

        // press back button
        pressBack()

        // updated note title is displayed on the list
        onView(withText(updatedNoteTitle)).check(matches(isDisplayed()))

        // updated note title is displayed on the list
        onView(withText(updatedNoteContent)).check(matches(isDisplayed()))
    }

    @Test
    fun createOneNote_deleteNote() {
        // open app
        ActivityScenario.launch(MainActivity::class.java)

        // create new note
        onView(withId(R.id.add_note_fab)).perform(click())
        onView(withId(R.id.note_title_edit_text)).perform(replaceText(note.title))
        onView(withId(R.id.note_content_edit_text)).perform(replaceText(note.contentText))

        // save note
        pressBack()

        // open detail note screen
        onView(withText(note.title)).perform(click())

        // delete note
        onView(withId(R.id.delete_note_menu)).perform(click())
        onView(withText(R.string.delete)).perform(click())

        // verify note was deleted
        onView(withText(note.title)).check(doesNotExist())
    }

    @Test
    fun insertTwoNotes_deleteOneNote() {
        // insert two notes into repository
        repository.insertNoteBlocking(note)
        repository.insertNoteBlocking(
            note.copy(title = "Note title 2", contentText = "Note content 2", id = 2)
        )

        // open app
        ActivityScenario.launch(MainActivity::class.java)

        // open second note in detail screen
        onView(withText("Note title 2")).perform(click())

        // delete the second note
        onView(withId(R.id.delete_note_menu)).perform(click())
        onView(withText(R.string.delete)).perform(click())

        // verify only one note left
        onView(withText(note.title)).check(matches(isDisplayed()))
        onView(withText("Note title 2")).check(doesNotExist())
    }

    @Test
    fun createNote() {
        // open app
        ActivityScenario.launch(MainActivity::class.java)

        // click new note button
        onView(withId(R.id.add_note_fab)).perform(click())

        // fill out note title
        onView(withId(R.id.note_title_edit_text)).perform(replaceText(note.title))
        // fill out note content
        onView(withId(R.id.note_content_edit_text)).perform(replaceText(note.contentText))

        // verify note datetime to now
        onView(withId(R.id.note_datetime_text_view)).apply {
            check(matches(isDisplayed()))
            check(matches(withText(todayDateTimeFormatted())))
        }

        // press back button
        pressBack()

        // created note title is displayed on the list
        onView(withText(note.title)).check(matches(isDisplayed()))
        // created note title is displayed on the list
        onView(withText(note.contentText)).check(matches(isDisplayed()))
    }

}