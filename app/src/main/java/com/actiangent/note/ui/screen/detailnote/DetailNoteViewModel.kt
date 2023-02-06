package com.actiangent.note.ui.screen.detailnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.note.data.Result.Companion.getOrElse
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.util.todayDateTimeFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailNoteUiState(
    val noteId: Int = emptyNote.id,
    val title: String = emptyNote.title,
    val contentText: String = emptyNote.contentText,
    val dateTime: String = todayDateTimeFormatted(),
)

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailNoteUiState())
    val uiState get() = _uiState.asStateFlow()

    fun getNote(id: Int) = viewModelScope.launch {
        val note = repository.getNoteById(id)
            .getOrElse { emptyNote }

        _uiState.update {
            it.copy(
                noteId = note.id,
                title = note.title,
                contentText = note.contentText,
                dateTime = note.dateTime
            )
        }
    }

    fun setNoteTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                dateTime = todayDateTimeFormatted(),
            )
        }
    }

    fun setNoteContentText(contentText: String) {
        _uiState.update {
            it.copy(
                contentText = contentText,
                dateTime = todayDateTimeFormatted(),
            )
        }
    }

    fun createNewNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

}