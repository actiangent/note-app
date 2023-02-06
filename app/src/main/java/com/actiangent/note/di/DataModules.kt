package com.actiangent.note.di

import android.content.Context
import com.actiangent.note.data.local.NotesDatabase
import com.actiangent.note.data.repository.DefaultNoteRepository
import com.actiangent.note.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNotesRepository(
        notesDatabase: NotesDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): NoteRepository {
        return DefaultNoteRepository(notesDatabase.notesDao(), ioDispatcher)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideNotesDatabase(@ApplicationContext context: Context): NotesDatabase {
        return NotesDatabase.getInstance(context)
    }

}