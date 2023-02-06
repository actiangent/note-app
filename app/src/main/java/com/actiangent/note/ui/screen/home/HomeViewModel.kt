package com.actiangent.note.ui.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actiangent.note.data.Result.Companion.getOrElse
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeNoteUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _query = mutableStateOf("")

    private val _queryFlow = snapshotFlow { _query.value }
    private val _notesFlow = repository.observeNotes()
        .map { resultNotes ->
//            when (resultNotes) {
//                is Result.Success -> {
//                    resultNotes.data
//                }
//                is Result.Error -> {
//
//                }
//            }
            resultNotes.getOrElse { emptyList() }
        }

    val uiState: StateFlow<HomeNoteUiState> =
        combine(_queryFlow, _notesFlow) { query, notes ->
            HomeNoteUiState(
                notes = notes.searchNotes(query),
                searchQuery = query
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeNoteUiState()
        )

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun clearQuery() {
        _query.value = ""
    }

    private fun List<Note>.searchNotes(query: String) = this.filter { note ->
        with(note) {
            title.contains(query, ignoreCase = true) or
                    contentText.contains(query, ignoreCase = true)
        }
    }

}