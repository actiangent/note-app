package com.actiangent.note.ui.screen.detailnote

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.HiltTestActivity
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.ui.navigation.ROUTE_DETAIL_ARG
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
class DetailNoteScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: NoteRepository
    private val note = Note("Note title", "Note content", "1990-01-01 00:00", 1)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun noteDetails_displayCorrectData() {
        // given - add note to repository
        repository.insertNoteBlocking(note)

        // when - detail note screen opened with given note id
        setContent(note.id)

        // then - note detail displayed on the screen
        // make sure title are both shown and correct
        composeTestRule.onNodeWithTag("titleTextField").apply {
            assertIsDisplayed()
            assert(isFocusable())
            assert(hasSetTextAction())

            hasTextExactly(note.title)
        }

        // make sure datetime are both shown and correct
        composeTestRule.onNodeWithTag("updatedAt").apply {
            assertIsDisplayed()

            hasTextExactly(note.dateTime)
        }

        // make sure content text are both shown and correct
        composeTestRule.onNodeWithTag("contentTextField").apply {
            assertIsDisplayed()
            assert(isFocusable())
            assert(hasSetTextAction())

            hasTextExactly(note.contentText)
        }
    }

    @Test
    fun newNoteDetails_displayBlankData() {
        // when - detail note screen opened with emptyNote id
        setContent(emptyNote.id)
        val noteDateTime = todayDateTimeFormatted()

        // then - note detail displayed on the screen
        // make sure title are both shown and correct
        composeTestRule.onNodeWithTag("titleTextField").apply {
            assertIsDisplayed()
            assert(isFocusable())
            assert(hasSetTextAction())

            hasTextExactly(emptyNote.title)
        }

        // make sure datetime are both shown and correct
        composeTestRule.onNodeWithTag("updatedAt").apply {
            assertIsDisplayed()

            hasTextExactly(noteDateTime)
        }

        // make sure content text are both shown and correct
        composeTestRule.onNodeWithTag("contentTextField").apply {
            assertIsDisplayed()
            assert(isFocusable())
            assert(hasSetTextAction())

            hasTextExactly(emptyNote.contentText)
        }
    }

    @Test
    fun noteDetails_verifyDeleteDialog() {
        // given - add note to repository
        repository.insertNoteBlocking(note)

        // when - detail note screen opened with given note id
        setContent(note.id)

        // verify and perform click on delete note button
        composeTestRule.onNodeWithTag("deleteNoteButton").apply {
            assertIsDisplayed()
            assertHasClickAction()

            performClick()
        }

        // verify delete note dialog is shown
        composeTestRule.onNodeWithTag("deleteConfirmDialog").apply {
            assertIsDisplayed()
            // verify any parent of delete dialog content is Dialog node
            hasAnyAncestor(isDialog())
            hasTextExactly("Confirm deletion")
            hasTextExactly("Are you sure want to delete this note ?")
            hasTextExactly("Delete")
            hasTextExactly("Cancel")
        }
    }

    private fun setContent(noteIdArg: Int) {
        composeTestRule.setContent {
            NotesAppTheme {
                DetailNoteScreen(
                    navigateBack = {},
                    viewModel = DetailNoteViewModel(
                        repository,
                        SavedStateHandle(mapOf(ROUTE_DETAIL_ARG to noteIdArg))
                    )
                )
            }
        }
    }

}