package com.actiangent.note.ui.screen.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.HiltTestActivity
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.ui.theme.NotesAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: NoteRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun noteList_displayCorrectData() {
        // given - add notes to repository
        addNotesToRepository()

        // when - show HomeScreen
        setContent()

        // then - verify note list displaying correct data
        composeTestRule.onNodeWithTag("noteList").apply {
            assertIsDisplayed()
            hasTextExactly("First note title")
            hasTextExactly("Second note title")
            hasTextExactly("Third note title")
        }
    }

    @Test
    fun noteList_searchNote() {
        // given - add notes to repository
        addNotesToRepository()

        // when - show HomeScreen and input search query
        setContent()

        composeTestRule.onNodeWithTag("noteSearchInputField")
            .performTextInput("Second note")

        // then - verify note list displaying correct searched data
        composeTestRule.onNodeWithTag("noteList").apply {
            assertIsDisplayed()
            hasTextExactly("Second note title")
        }
    }

    @Test
    fun noteList_searchNoteNoMatch() {
        // given - add notes to repository
        addNotesToRepository()

        // when - show HomeScreen and input search query
        setContent()

        composeTestRule.onNodeWithTag("noteSearchInputField")
            .performTextInput("Fourth note")

        // then - no match content shown
        composeTestRule.onNodeWithText("No matching note").assertIsDisplayed()
    }

    @Test
    fun noteList_empty() {
        // when - show HomeScreen
        setContent()

        // then - verify empty content shown
        composeTestRule.onNodeWithText("Start writing some notes").assertIsDisplayed()
    }

    @Test
    fun addNoteButton_isDisplayedAndClickable() {
        // when - show HomeScreen
        setContent()

        // then - verify new note floating action button
        composeTestRule.onNodeWithTag("addNoteButton").apply {
            assertIsDisplayed()
            assertHasClickAction()
        }
    }

    @Test
    fun searchBar_worksProperly() {
        // when - show HomeScreen
        setContent()

        // then
        // verify and perform input on SearchBar input field
        composeTestRule.onNodeWithTag("noteSearchInputField").apply {
            assertIsDisplayed()
            assert(isFocusable())

            performClick()
            performTextInput("This is note title")
        }

        // the expected data is displayed
        composeTestRule.onNodeWithTag("noteList").apply {
            hasTextExactly("This is note title")
        }

        // verify and perform click on SearchBar clear button
        composeTestRule.onNodeWithTag("noteSearchClearQueryButton").apply {
            assertIsDisplayed()
            assertHasClickAction()

            performClick()
        }

        // verify SearchBar input field has empty string
        composeTestRule.onNodeWithTag("noteSearchInputField").apply {
            hasTextExactly("")
        }
    }

    private fun addNotesToRepository() {
        val note1 = Note("First note title", "First note content", "1990-01-01 00:00", 1)
        val note2 = Note("Second note title", "Second note content", "1990-01-02 00:10", 2)
        val note3 = Note("Third note title", "Third note content", "1990-01-03 00:20", 3)
        repository.insertNoteBlocking(note1)
        repository.insertNoteBlocking(note2)
        repository.insertNoteBlocking(note3)
    }

    private fun setContent() {
        composeTestRule.setContent {
            NotesAppTheme {
                HomeScreen(
                    navigateToDetailNote = {},
                    viewModel = HomeViewModel(repository)
                )
            }
        }
    }
}