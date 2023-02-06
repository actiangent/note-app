package com.actiangent.note.di

import com.actiangent.note.data.fake.FakeRepository
import com.actiangent.note.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {

    @Singleton
    @Provides
    fun provideTasksRepository(): NoteRepository {
        return FakeRepository()
    }
}