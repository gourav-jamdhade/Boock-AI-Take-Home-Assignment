package com.example.boocksaitakehomeassignment.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boocksaitakehomeassignment.domain.model.Book
import com.example.boocksaitakehomeassignment.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: String = checkNotNull(savedStateHandle["bookId"])

    //Observe this specific book from the DB
    val book: StateFlow<Book?> = repository.getBookById(bookId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun toggleSavedStatus() {
        val currentBook = book.value ?: return
        viewModelScope.launch {
            repository.toggleSaveStatus(currentBook.id, !currentBook.isSaved)
        }
    }
}