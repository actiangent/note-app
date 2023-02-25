package com.actiangent.note.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.note.R
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeNoteUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val snackbarMessage: Int? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _snackbarMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _queryFlow = MutableStateFlow("")
    private val _notesFlow = repository.observeNotes()
        .catch {
            showSnackbarMessage(R.string.load_note_error)
            emit(emptyList()) // emit empty list in order to avoid canceling the flow
        }

    val uiState: StateFlow<HomeNoteUiState> =
        combine(_queryFlow, _notesFlow, _snackbarMessage) { query, notes, message ->
            HomeNoteUiState(
                notes = notes.searchNotes(query),
                searchQuery = query,
                snackbarMessage = message
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeNoteUiState()
        )

    private fun showSnackbarMessage(message: Int) {
        _snackbarMessage.value = message
    }

    fun snackbarMessageShown() {
        _snackbarMessage.value = null
    }

    fun setQuery(newQuery: String) {
        _queryFlow.value = newQuery
    }

    fun clearQuery() {
        _queryFlow.value = ""
    }

    private fun List<Note>.searchNotes(query: String) = this.filter { note ->
        note.title.contains(query, ignoreCase = true) or
                note.contentText.contains(query, ignoreCase = true)
    }

}