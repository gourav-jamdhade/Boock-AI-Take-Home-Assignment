package com.example.boocksaitakehomeassignment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.boocksaitakehomeassignment.domain.model.Book
import com.example.boocksaitakehomeassignment.presentation.detail.DetailScreenContent
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ctaShowStartReadingWhenProgressIsZero() {
        val unreadBook = Book("1", "Test Title", "Author", null, "Summary", "Content", false, 0)

        composeTestRule.setContent {
            DetailScreenContent(
                book = unreadBook,
                onNavigateBack = {},
                onReadClick = {},
                onToggleSave = {}
            )
        }

        composeTestRule.onNodeWithText("Start Reading").assertIsDisplayed()
    }

    @Test
    fun ctaShowsContinuedReadingWhenProgressIsGreaterThanZero(){
        val readingBook = Book("2", "Test Title", "Author", null, "Summary", "Content", false, 150)

        composeTestRule.setContent {
            DetailScreenContent(
                book = readingBook,
                onNavigateBack = {},
                onReadClick = {},
                onToggleSave = {},
            )
        }

        composeTestRule.onNodeWithText("Continue Reading").assertIsDisplayed()
    }
}