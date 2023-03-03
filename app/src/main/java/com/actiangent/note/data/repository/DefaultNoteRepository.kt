package com.actiangent.note.data.repository

import com.actiangent.note.data.local.NotesDao
import com.actiangent.note.data.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DefaultNoteRepository(
    private val notesDao: NotesDao,
    private val ioDispatcher: CoroutineDispatcher
) : NoteRepository {

    override suspend fun insertNote(note: Note) =
        withContext(ioDispatcher) { notesDao.insert(note) }

    override suspend fun deleteNote(note: Note) =
        withContext(ioDispatcher) { notesDao.delete(note) }

    override suspend fun updateNote(note: Note) =
        withContext(ioDispatcher) { notesDao.update(note) }

    override fun observeNotes(): Flow<List<Note>> =
        notesDao.getAllNoteFlow()

    override suspend fun getNoteById(noteId: Int): Note? =
        notesDao.getNoteById(noteId)

}