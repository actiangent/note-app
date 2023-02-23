package com.actiangent.note.ui.screen.detailnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.note.data.Result
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.ui.navigation.ROUTE_DETAIL_ARG
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
    val isNoteSaved: Boolean = false
)

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // use SavedStateHandle to retrieve noteId navigation argument
    private val noteId: Int? = savedStateHandle[ROUTE_DETAIL_ARG]

    private val _uiState = MutableStateFlow(DetailNoteUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            getNote(noteId)
        }
    }

    private fun getNote(id: Int) = viewModelScope.launch {
        repository.getNoteById(id).let { result ->
            when (result) {
                is Result.Success -> {
                    val note = result.data
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
                else -> {}
            }
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

}