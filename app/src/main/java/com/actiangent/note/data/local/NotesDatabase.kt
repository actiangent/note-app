package com.actiangent.note.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.actiangent.note.data.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        private fun buildDatabase(context: Context): NotesDatabase =
            Room.databaseBuilder(context, NotesDatabase::class.java, "notes-db")
                .createFromAsset("database/notes.db")
                .build()

        fun getInstance(context: Context): NotesDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

    }

}

