package com.actiangent.note.ui.fragment.detailnote

import androidx.lifecycle.SavedStateHandle
import com.actiangent.note.MainTestDispatcherRule
import com.actiangent.note.data.fake.FakeRepository
import com.actiangent.note.data.fake.insertNoteBlocking
import com.actiangent.note.data.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailNoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainTestDispatcherRule()

    private val repository = FakeRepository()
    private val note = Note("Note title", "Note content", "1990-01-01 00:00", 1)

    private lateinit var detailNoteViewModel: DetailNoteViewModel

    @Test
    fun `load correct note with given id`() = runTest {
        // given - insert task to repository
        repository.insertNoteBlocking(note)

        // when - get note by id on DetailViewModel init
        setupDetailNoteViewModel(note.id)

        // then - verify the loaded note data
        val result = detailNoteViewModel.uiState.first()
        assertEquals(note.id, result.noteId)
        assertEquals(note.title, result.title)
        assertEquals(note.contentText, result.contentText)
        assertEquals(note.dateTime, result.dateTime)
    }

    @Test
    fun `create new note test`() = runTest {
        setupDetailNoteViewModel(0)

        // given - new note
        val newNoteTitle = "Note title"
        val newNoteContent = "Note content"
        detailNoteViewModel.apply {
            setNoteTitle(newNoteTitle)
            setNoteContentText(newNoteContent)
        }
        val newNoteDateTime = detailNoteViewModel.uiState.value.dateTime

        // when - insert new note
        detailNoteViewModel.saveNote()

        // then - new note is saved
        val savedNewNote = repository.getNotes().first()
        assertEquals(newNoteTitle, savedNewNote.title)
        assertEquals(newNoteContent, savedNewNote.contentText)
        assertEquals(newNoteDateTime, savedNewNote.dateTime)
    }

    @Test
    fun `update note test`() = runTest {
        // given - add note to repository
        repository.insertNoteBlocking(note)
        setupDetailNoteViewModel(note.id)

        // when - update the note
        val updatedNoteTitle = "Updated note title"
        val updatedNoteContent = "Updated note content"
        detailNoteViewModel.apply {
            setNoteTitle(updatedNoteTitle)
            setNoteContentText(updatedNoteContent)
        }
        val updatedNoteDateTime = detailNoteViewModel.uiState.value.dateTime
        detailNoteViewModel.saveNote()

        // then - updated note is saved
        val savedUpdatedNote = repository.getNotes().first()
        assertEquals(updatedNoteTitle, savedUpdatedNote.title)
        assertEquals(updatedNoteContent, savedUpdatedNote.contentText)
        assertEquals(updatedNoteDateTime, savedUpdatedNote.dateTime)
    }

    @Test
    fun `delete note test`() = runTest {
        // given - add note to repository
        repository.insertNoteBlocking(note)
        setupDetailNoteViewModel(note.id)

        // when - delete the note
        detailNoteViewModel.deleteNote()

        // then - inserted note deleted
        assertTrue(repository.getNotes().isEmpty())
    }

    private fun setupDetailNoteViewModel(noteIdArg: Int) {
        detailNoteViewModel = DetailNoteViewModel(
            repository,
            SavedStateHandle(mapOf("noteId" to noteIdArg))
        )
    }

}