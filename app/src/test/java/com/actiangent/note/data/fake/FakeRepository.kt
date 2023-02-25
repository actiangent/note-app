package com.actiangent.note.data.fake

import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class FakeRepository : NoteRepository {

    private var shouldReturnError = false

    private val fakeNotes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())

    override suspend fun insertNote(note: Note) {
        fakeNotes.update {
            val m = it.toMutableList().apply {
                add(note)
            }
            m.toList()
        }
    }

    override suspend fun deleteNote(note: Note) {
        fakeNotes.update {
            val m = it.toMutableList().apply {
                remove(note)
            }
            m.toList()
        }
    }

    override suspend fun updateNote(note: Note) {
        fakeNotes.update {
            val m = it.toMutableList().apply {
                set(note.id - 1, note)
            }
            m.toList()
        }
    }

    override fun observeNotes(): Flow<List<Note>> = fakeNotes.map {
        if (shouldReturnError) {
            throw Exception("Test exception")
        } else {
            it
        }
    }

    override suspend fun getNoteById(noteId: Int): Note? =
        fakeNotes.value.firstOrNull { note -> note.id == noteId }

    fun shouldThrowError(value: Boolean) {
        shouldReturnError = value
    }

    fun getNotes() = fakeNotes.value

}

fun NoteRepository.insertNoteBlocking(note: Note) = runBlocking {
    this@insertNoteBlocking.insertNote(note)
}