package com.example.boocksaitakehomeassignment.presentation.reader

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boocksaitakehomeassignment.domain.model.Book
import com.example.boocksaitakehomeassignment.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit


@HiltViewModel
class ReaderViewmodel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val bookId: String = checkNotNull(savedStateHandle["bookId"])

    private val prefs = context.getSharedPreferences("boock_prefs", Context.MODE_PRIVATE)

    val book: StateFlow<Book?> = repository.getBookById(bookId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val savedSizeOrdinal = prefs.getInt("text_size_ordinal", TextSize.MEDIUM.ordinal)
    private val _textSize = MutableStateFlow(TextSize.entries[savedSizeOrdinal])
    val textSize: StateFlow<TextSize> = _textSize.asStateFlow()

    fun cycleText() {
       val nextSize = when (_textSize.value) {
            TextSize.SMALL -> TextSize.MEDIUM
            TextSize.MEDIUM -> TextSize.LARGE
            TextSize.LARGE -> TextSize.SMALL
        }

        _textSize.value = nextSize

        // Save the new choice to SharedPreferences immediately
        prefs.edit { putInt("text_size_ordinal", nextSize.ordinal) }
    }

    // Called when the user navigates away from the screen
    fun saveProgress(scrollPosition: Int) {
        viewModelScope.launch {
            // if progress is > 0 so the Home screen registers it as "Reading"
            val progressToSave = if (scrollPosition == 0) 1 else scrollPosition
            repository.updateReadingProgress(bookId, progressToSave)
        }
    }
}


enum class TextSize(val label: String, val sizeSp: Int) {
    SMALL("Small", 14),
    MEDIUM("Medium", 18),
    LARGE("Large", 24)

}