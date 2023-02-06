package com.actiangent.note.data.fake

import com.actiangent.note.data.Result
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
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

    override fun observeNotes(): Flow<Result<List<Note>>> = fakeNotes.map {
        if (shouldReturnError) {
            Result.Error(Exception())
        } else {
            Result.Success(it)
        }
    }

    override suspend fun getNoteById(noteId: Int): Result<Note> {
        val note = fakeNotes.map { it.firstOrNull { note -> note.id == noteId } }

        return note.first()?.let {
            Result.Success(it)
        } ?: Result.Error(Exception("Note not found!"))
    }

}

fun NoteRepository.insertNoteBlocking(note: Note) = runBlocking {
    this@insertNoteBlocking.insertNote(note)
}