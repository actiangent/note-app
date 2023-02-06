package com.actiangent.note.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    val title: String,
    val contentText: String,
    val dateTime: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

val emptyNote = Note(title = "", contentText = "", dateTime = "")
