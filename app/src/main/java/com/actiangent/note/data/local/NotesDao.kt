package com.actiangent.note.data.local

import androidx.room.*
import com.actiangent.note.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM note_table")
    suspend fun getNotes(): List<Note>

    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAllNoteFlow(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

}