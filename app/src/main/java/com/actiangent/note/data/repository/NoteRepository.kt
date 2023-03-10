package com.actiangent.note.data.repository

import com.actiangent.note.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

    fun observeNotes(): Flow<List<Note>>

    suspend fun getNoteById(noteId: Int): Note?

}