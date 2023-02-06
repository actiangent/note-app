package com.actiangent.note.ui.screen.home

import com.actiangent.note.MainTestDispatcherRule
import com.actiangent.note.data.fake.FakeRepository
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainTestDispatcherRule()

    private val repository = FakeRepository()
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(repository)

        // given - add notes to repository
        val note1 = Note("First note title", "First note content", "1990-01-01 00:00", 1)
        val note2 = Note("Second note title", "Second note content", "1990-01-02 00:10", 2)
        val note3 = Note("Third note title", "Third note content", "1990-01-03 00:20", 3)
        repository.insertNoteBlocking(note1)
        repository.insertNoteBlocking(note2)
        repository.insertNoteBlocking(note3)
    }

    @Test
    fun `load correct notes data`() = runTest {
        // when - get notes
        val result = homeViewModel.uiState.first()

        // then - verify loaded notes as expected
        val expectedNotes = repository.getNotes()
        assertEquals(expectedNotes.size, result.notes.size)
        assertEquals(expectedNotes, result.notes)
    }

    @Test
    fun `load correct notes when searched`() = runTest {
        // when - search query is set
        val searchedQuery = "Second"
        homeViewModel.setQuery(searchedQuery)

        // then - verify loaded notes as expected
        val expectedNote =
            repository.getNotes().filter { note -> note.title.contains(searchedQuery) }

        val result = homeViewModel.uiState.first()
        assertEquals(expectedNote.size, result.notes.size)
        assertEquals(expectedNote, result.notes)
    }

}