package com.actiangent.note.ui.fragment.home

import androidx.lifecycle.*
import com.actiangent.note.data.Result
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeNoteUiState(
    val searchQuery: String = "",
    val notes: List<Note> = emptyList(),
    val errorState: HomeNoteUiErrorState = HomeNoteUiErrorState()
)

data class HomeNoteUiErrorState(
    val isError: Boolean = false,
    val message: String = "An error occurred"
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: NoteRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _resultNotes = repository.observeNotes()

    val uiState: StateFlow<HomeNoteUiState> =
        combine(_searchQuery, _resultNotes) { searchQuery, notes ->
            when (notes) {
                is Result.Success -> {
                    HomeNoteUiState(
                        searchQuery = searchQuery,
                        notes = notes.data.filter { note -> note.title.contains(searchQuery) }
                    )
                }
                is Result.Error -> {
                    HomeNoteUiState(
                        searchQuery = searchQuery,
                        errorState = HomeNoteUiErrorState(
                            isError = true
                        )
                    )
                }
            }
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeNoteUiState()
        )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearQuery() {
        _searchQuery.value = ""
    }
}