package com.actiangent.note.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.actiangent.note.MainTestDispatcherRule
import com.actiangent.note.data.model.Note
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesDaoTest {

    @get:Rule
    val mainCoroutineRule = MainTestDispatcherRule()

    private lateinit var database: NotesDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun insertNote_getById() = runTest {
        // given - insert a note
        val note = Note("title", "body", "1970-01-01 00:00", 1)
        database.notesDao().insert(note)

        // when - get note by id
        val result = database.notesDao().getNoteById(note.id)

        // then - the result from database is same as the inserted note
        assertEquals(note, result)
    }

    @Test
    fun insertNotesReplacesOnConflict() = runTest {
        // given - insert a note
        val note1 = Note("title", "body", "1970-01-01 00:00", 1)
        database.notesDao().insert(note1)

        // when - insert a note with the same id
        val note2 = Note("title2", "body2", "1970-02-02 00:00", note1.id)
        database.notesDao().insert(note2)

        // then - the result from database contains note2 content
        val result = database.notesDao().getNoteById(note1.id)
        assertEquals(note2, result)
    }

    @Test
    fun updateNote_getById() = runTest {
        // given - insert a note
        val originalNote = Note("title", "body", "1970-01-01 00:00", 1)
        database.notesDao().insert(originalNote)

        // when - update originalNote
        val updatedNote = Note("new title", "new body", "1970-01-01 00:00", originalNote.id)
        database.notesDao().update(updatedNote)

        // then - result from database contains updatedNote content
        val result = database.notesDao().getNoteById(originalNote.id)
        assertEquals(updatedNote.id, result?.id)
        assertEquals(updatedNote.title, result?.title)
        assertEquals(updatedNote.contentText, result?.contentText)
        assertEquals(updatedNote.dateTime, result?.dateTime)
    }

    @Test
    fun deleteNote_getById() = runTest {
        // given - insert a note
        val note = Note("title", "body", "1970-01-01 00:00", 1)
        database.notesDao().insert(note)

        // when - delete the inserted note
        database.notesDao().delete(note)

        // then - result from database
        val result = database.notesDao().getNotes()
        assertEquals(true, result.isEmpty())
    }


}