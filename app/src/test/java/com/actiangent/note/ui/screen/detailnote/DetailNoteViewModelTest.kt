package com.actiangent.note.ui.screen.detailnote

import com.actiangent.note.MainTestDispatcherRule
import com.actiangent.note.data.fake.FakeRepository
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailNoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainTestDispatcherRule()

    private val repository = FakeRepository()
    private lateinit var detailNoteViewModel: DetailNoteViewModel

    @Before
    fun setUp() {
        detailNoteViewModel = DetailNoteViewModel(repository)
    }

    @Test
    fun `load correct note with given id`() = runTest {
        // given - add note to repository
        val note = Note("Note title", "Note content", "1990-01-01 00:00", 1)
        repository.insertNoteBlocking(note)

        // when - get note by id
        detailNoteViewModel.getNote(note.id)

        // then - verify the loaded note data
        val result = detailNoteViewModel.uiState.first()
        assertEquals(note.id, result.noteId)
        assertEquals(note.title, result.title)
        assertEquals(note.contentText, result.contentText)
        assertEquals(note.dateTime, result.dateTime)
    }

    @Test
    fun `create new note test`() = runTest {
        // given - create a new note
        val newNote = Note("Note title", "Note content", "1990-01-01 00:00", 1)

        // when - insert new note
        detailNoteViewModel.createNewNote(newNote)

        // then - verify the loaded note is the inserted note
        detailNoteViewModel.getNote(newNote.id)

        val result = detailNoteViewModel.uiState.first()
        assertEquals(newNote.id, result.noteId)
        assertEquals(newNote.title, result.title)
        assertEquals(newNote.contentText, result.contentText)
        assertEquals(newNote.dateTime, result.dateTime)
    }

    @Test
    fun `update note test`() = runTest {
        // given - add note to repository
        val note = Note("Note title", "Note content", "1990-01-01 00:00", 1)
        repository.insertNoteBlocking(note)

        // when - update the note
        val updatedNote =
            note.copy("Updated note title", "Updated note content", "1990-01-01 11:00")
        detailNoteViewModel.updateNote(updatedNote)

        // then - verify the loaded note is the updated note
        detailNoteViewModel.getNote(note.id)

        val result = detailNoteViewModel.uiState.first()
        assertEquals(updatedNote.id, result.noteId)
        assertEquals(updatedNote.title, result.title)
        assertEquals(updatedNote.contentText, result.contentText)
        assertEquals(updatedNote.dateTime, result.dateTime)
    }

    @Test
    fun `delete note test`() = runTest {
        // given - add note to repository
        val note = Note("Note title", "Note content", "1990-01-01 00:00", 1)
        repository.insertNoteBlocking(note)

        // when - delete the note
        detailNoteViewModel.deleteNote(note)

        // then - result note is emptyNote
        detailNoteViewModel.getNote(note.id)

        val result = detailNoteViewModel.uiState.first()
        assertEquals(emptyNote.id, result.noteId)
        assertEquals(emptyNote.title, result.title)
        assertEquals(emptyNote.contentText, result.contentText)
        assertEquals(emptyNote.dateTime, result.dateTime)
    }

}