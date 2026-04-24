package com.example.boocksaitakehomeassignment.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boocksaitakehomeassignment.domain.model.Book
import com.example.boocksaitakehomeassignment.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllBook(),
        searchQuery
    ) { books, query ->
        if (books.isEmpty()) {
            // Trigger database init if it's completely empty
            repository.initializeDatabaseIfNeeded()
            HomeUiState.Loading
        } else {
            val safeQuery = query.trim()
            val filteredList = if (safeQuery.isBlank()) {
                books
            } else {
                books.filter {
                    it.title.contains(safeQuery, ignoreCase = true) ||
                            it.author.contains(safeQuery, ignoreCase = true)
                }
            }

            val sortedList = filteredList.sortedWith(
                compareByDescending<Book> { it.isSaved }
                    .thenByDescending { it.readingProgress > 0 }
            )

            if (sortedList.isEmpty() && safeQuery.isNotBlank()) {
                HomeUiState.Error("No books found matching \"$query\"")
            } else {
                HomeUiState.Success(sortedList)
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val books: List<Book>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}