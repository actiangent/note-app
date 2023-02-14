package com.actiangent.note.data.repository

import com.actiangent.note.data.Result
import com.actiangent.note.data.local.NotesDao
import com.actiangent.note.data.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun observeNotes(): Flow<Result<List<Note>>> =
        notesDao.getAllNoteFlow().map { Result.Success(it) }

    override suspend fun getNoteById(noteId: Int): Result<Note> = withContext(ioDispatcher) {
        return@withContext try {
            val note = notesDao.getNoteById(noteId)
            if (note != null) {
                Result.Success(note)
            } else {
                Result.Error(Exception("Note not found!"))
            }
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

}