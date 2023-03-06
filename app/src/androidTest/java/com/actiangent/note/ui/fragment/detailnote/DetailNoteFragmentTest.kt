package com.actiangent.note.ui.fragment.detailnote

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.R
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.util.launchFragmentInHiltContainer
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
class DetailNoteFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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

        // when - detail note fragment opened with given note id
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController, note.id)

        // then - note detail displayed on the screen
        // make sure title are both shown and correct
        onView(withId(R.id.note_title_edit_text)).apply {
            check(matches(isDisplayed()))
            check(matches(isFocusable()))
            check(matches(supportsInputMethods()))

            check(matches(withText(note.title)))
        }

        // make sure datetime are both shown and correct
        onView(withId(R.id.note_datetime_text_view)).apply {
            check(matches(isDisplayed()))

            check(matches(withText(note.dateTime)))
        }

        // make sure content text are both shown and correct
        onView(withId(R.id.note_content_edit_text)).apply {
            check(matches(isDisplayed()))
            check(matches(isFocusable()))
            check(matches(supportsInputMethods()))

            check(matches(withText(note.contentText)))
        }
    }

    @Test
    fun newNoteDetails_displayBlankData() {
        // when - detail note fragment opened with emptyNote id
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController, emptyNote.id)
        val noteDateTime = todayDateTimeFormatted()

        // then - note detail displayed on the screen
        // make sure title are both shown and correct
        onView(withId(R.id.note_title_edit_text)).apply {
            check(matches(isDisplayed()))
            check(matches(isFocusable()))
            check(matches(supportsInputMethods()))

            check(matches(withText(emptyNote.title)))
        }

        // make sure datetime are both shown and correct
        onView(withId(R.id.note_datetime_text_view)).apply {
            check(matches(isDisplayed()))

            check(matches(withText(noteDateTime)))
        }

        // make sure content text are both shown and correct
        onView(withId(R.id.note_content_edit_text)).apply {
            check(matches(isDisplayed()))
            check(matches(isFocusable()))
            check(matches(supportsInputMethods()))

            check(matches(withText(emptyNote.contentText)))
        }
    }

    @Test
    fun noteDetails_verifyDeleteDialog() {
        // given - add note to repository
        repository.insertNoteBlocking(note)

        // when - detail note fragment opened with given note id
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController, note.id)

        // verify and perform click on delete note button
        onView(withId(R.id.delete_note_menu)).apply {
            check(matches(isDisplayed()))
            check(matches(isClickable()))

            perform(click())
        }

        // verify delete note dialog is shown
        onView(withText(R.string.delete)).apply {
            inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }

    private fun launchFragment(navController: TestNavHostController, noteId: Int) {
        val bundle = DetailNoteFragmentArgs(noteId).toBundle()
        launchFragmentInHiltContainer<DetailNoteFragment>(bundle, R.style.Theme_NoteApp) {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.detailFragment, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }
    }

}