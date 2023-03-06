package com.actiangent.note.ui.fragment.home

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.R
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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

        // when - show home fragment
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)

        // then - verify note list displaying correct data
        onView(withText("First note title")).check(matches(isDisplayed()))
        onView(withText("Second note title")).check(matches(isDisplayed()))
        onView(withText("Third note title")).check(matches(isDisplayed()))
    }

    @Test
    fun noteList_searchNote() {
        // given - add notes to repository
        addNotesToRepository()

        // when - show home fragment and input search query
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)

        onView(withId(R.id.search_query_edit_text)).apply {
            perform(click())
            perform(typeText("Second"))
        }

        // then - verify note list displaying correct searched data
        onView(withText("Second note title")).check(matches(isDisplayed()))
    }

    @Test
    fun noteList_searchNoteNoMatch() {
        // given - add notes to repository
        addNotesToRepository()

        // when - show home fragment and input search query
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)

        onView(withId(R.id.search_query_edit_text)).apply {
            perform(typeText("Fourth note"))
        }

        // then - no match content shown
        onView(withText("No matching note")).check(matches(isDisplayed()))
    }

    @Test
    fun noteList_empty() {
        // when - show home fragment
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)

        onView(withText("Start writing some notes")).check(matches(isDisplayed()))
    }

    @Test
    fun addNoteButton_isDisplayedAndClickable() {
        // when - show home fragment
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)

        // then - verify new note floating action button
        onView(withId(R.id.add_note_fab)).apply {
            check(matches(isDisplayed()))
            check(matches(isClickable()))
        }
    }

    @Test
    fun searchBar_worksProperly() {
        // when - show home fragment
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)

        // then
        // verify and perform input on SearchBar input field
        onView(withId(R.id.search_query_edit_text)).apply {
            check(matches(isDisplayed()))
            check(matches(isFocusable()))

            perform(click())
            perform(typeText("This is note title"))
        }

        // verify and perform click on SearchBar clear button
        onView(withId(R.id.clear_icon_image_view)).apply {
            check(matches(isDisplayed()))
            check(matches(isClickable()))

            perform(click())
        }

        // verify SearchBar input field has empty string
        onView(withId(R.id.search_query_edit_text)).apply {
            check(matches(withText("")))
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

    private fun launchFragment(navController: TestNavHostController) {
        launchFragmentInHiltContainer<HomeFragment>(Bundle(), R.style.Theme_NoteApp) {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.homeFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
    }
}