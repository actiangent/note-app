package com.actiangent.note.ui.fragment.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.note.data.Result.Companion.getOrElse
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.data.repository.NoteRepository
import com.actiangent.note.util.todayDateTimeFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DetailNoteUiState(
    val noteId: Int = emptyNote.id,
    val title: String = emptyNote.title,
    val contentText: String = emptyNote.contentText,
    val dateTime: String = todayDateTimeFormatted()
)

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: StateFlow<Int> = savedStateHandle.getStateFlow("noteId", 1)

    private val _uiState = noteId.map { noteId ->
        val note = repository.getNoteById(noteId)
            .getOrElse { emptyNote } // TODO - handle error

        DetailNoteUiState(
            noteId = note.id,
            title = note.title,
            contentText = note.contentText,
            dateTime = note.dateTime
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DetailNoteUiState()
    )
    val uiState get() = _uiState

}