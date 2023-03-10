package com.actiangent.note.ui.fragment.detailnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.util.todayDateTimeFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailNoteUiState(
    val noteId: Int = emptyNote.id,
    val title: String = emptyNote.title,
    val contentText: String = emptyNote.contentText,
    val dateTime: String = todayDateTimeFormatted(),
    val isNoteSaved: Boolean = false
)

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Int? = savedStateHandle["noteId"]

    private val _uiState = MutableStateFlow(DetailNoteUiState())
    val uiState get() = _uiState

    init {
        if (noteId != null) {
            getNote(noteId)
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

    fun saveNote() {
        if (_uiState.value.title.isBlank() && _uiState.value.contentText.isBlank()) {
            return
        }

        if (_uiState.value.noteId == emptyNote.id) {
            createNewNote()
        } else {
            updateNote()
        }
    }

    fun deleteNote() {
        if (_uiState.value.isNoteSaved) {
            viewModelScope.launch {
                val deletedNote = Note(
                    title = uiState.value.title,
                    contentText = uiState.value.contentText,
                    dateTime = uiState.value.dateTime,
                    id = uiState.value.noteId
                )
                repository.deleteNote(deletedNote)
            }
        } else {
            return
        }
    }

    private fun createNewNote() = viewModelScope.launch {
        val newNote = Note(
            title = uiState.value.title,
            contentText = uiState.value.contentText,
            dateTime = todayDateTimeFormatted()
        )
        repository.insertNote(newNote)
    }

    private fun updateNote() = viewModelScope.launch {
        val updatedNote = Note(
            title = uiState.value.title,
            contentText = uiState.value.contentText,
            dateTime = uiState.value.dateTime,
            id = uiState.value.noteId
        )
        repository.updateNote(updatedNote)
    }

    private fun getNote(id: Int) = viewModelScope.launch {
        repository.getNoteById(id)?.let { note ->
            _uiState.update {
                it.copy(
                    noteId = note.id,
                    title = note.title,
                    contentText = note.contentText,
                    dateTime = note.dateTime,
                    isNoteSaved = true
                )
            }
        }
    }
}