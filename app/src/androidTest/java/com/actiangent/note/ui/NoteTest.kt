package com.actiangent.note.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.HiltTestActivity
import com.actiangent.note.R
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.ui.navigation.NoteNavigation
import com.actiangent.note.ui.theme.NotesAppTheme
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

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

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
        setContent()

        // click note on the list
        composeTestRule.onNodeWithText(note.title).apply {
            assertIsDisplayed()
            performClick()
        }

        // change note title
        val updatedNoteTitle = "Updated note title"
        findTextField(note.title).performTextReplacement(updatedNoteTitle)
        // change note content
        val updatedNoteContent = "Updated note content"
        findTextField(note.contentText).performTextReplacement(updatedNoteContent)
        // verify note datetime to now
        composeTestRule.onNodeWithTag("updatedAt").apply {
            assertIsDisplayed()
            hasTextExactly(todayDateTimeFormatted())
        }

        // press back button
        activity.onBackPressedDispatcher.onBackPressed()

        // updated note title is displayed on the list
        composeTestRule.onNodeWithText(updatedNoteTitle).assertIsDisplayed()
        // updated note title is displayed on the list
        composeTestRule.onNodeWithText(updatedNoteContent).assertIsDisplayed()
    }

    @Test
    fun createOneNote_deleteNote() {
        // open app
        setContent()

        // create new note
        composeTestRule.onNodeWithTag("addNoteButton").performClick()
        composeTestRule.onNodeWithTag("titleTextField").performTextInput(note.title)
        composeTestRule.onNodeWithTag("contentTextField").performTextInput(note.contentText)

        // save note
        activity.onBackPressedDispatcher.onBackPressed()

        // open detail note screen
        composeTestRule.onNodeWithText(note.title).performClick()
        // delete note
        composeTestRule.onNodeWithTag("deleteNoteButton").performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.delete)).performClick()

        // verify note was deleted
        composeTestRule.onNodeWithText(note.title).assertDoesNotExist()
    }

    @Test
    fun insertTwoNotes_deleteOneNote() {
        // insert two notes into repository
        repository.insertNoteBlocking(note)
        repository.insertNoteBlocking(
            note.copy(title = "Note title 2", contentText = "Note content 2", id = 2)
        )

        // open app
        setContent()

        // open second note in detail screen
        composeTestRule.onNodeWithText("Note title 2").performClick()
        // delete the second note
        composeTestRule.onNodeWithTag("deleteNoteButton").performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.delete)).performClick()

        // verify only one note left
        composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
        composeTestRule.onNodeWithText("Note title 2").assertDoesNotExist()
    }

    @Test
    fun createNote() {
        // open app
        setContent()

        // click new note button
        composeTestRule.onNodeWithTag("addNoteButton").performClick()

        // fill out note title
        composeTestRule.onNodeWithTag("titleTextField").performTextInput(note.title)
        // fill out note content
        composeTestRule.onNodeWithTag("contentTextField").performTextInput(note.contentText)

        // verify note datetime to now
        composeTestRule.onNodeWithTag("updatedAt").apply {
            assertIsDisplayed()
            hasTextExactly(todayDateTimeFormatted())
        }

        // press back button
        activity.onBackPressedDispatcher.onBackPressed()

        // created note title is displayed on the list
        composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
        // created note title is displayed on the list
        composeTestRule.onNodeWithText(note.contentText).assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            NotesAppTheme {
                NoteNavigation()
            }
        }
    }

    private fun findTextField(text: String): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(text)
        )
    }
}