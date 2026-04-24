package com.example.boocksaitakehomeassignment

import com.example.boocksaitakehomeassignment.domain.model.Book
import com.example.boocksaitakehomeassignment.domain.repository.BookRepository
import com.example.boocksaitakehomeassignment.presentation.home.HomeUiState
import com.example.boocksaitakehomeassignment.presentation.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    //Fake Repo
    class FakeRepository : BookRepository {
        val mockBooks = listOf(
            Book(
                "1",
                "Apple Book",
                "Author A",
                null,
                "Sum",
                "Content",
                isSaved = false,
                readingProgress = 0
            ),
            Book(
                "2",
                "Banana Book",
                "Author B",
                null,
                "Sum",
                "Content",
                isSaved = true,
                readingProgress = 0
            ),
            Book(
                "3",
                "Cherry Book",
                "Author C",
                null,
                "Sum",
                "Content",
                isSaved = false,
                readingProgress = 100
            )

        )

        override fun getAllBook(): Flow<List<Book>> = flowOf(mockBooks)

        override fun getBookById(id: String): Flow<Book?> = flowOf(null)

        override suspend fun toggleSaveStatus(id: String, isSaved: Boolean) {}

        override suspend fun updateReadingProgress(id: String, progress: Int) {}

        override suspend fun initializeDatabaseIfNeeded() {}
    }

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search query filters books correctly`() = runTest {

        val viewModel = HomeViewModel(FakeRepository())

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }
        //Trigger Search
        viewModel.onSearchQueryChanged("Apple")

        val state = viewModel.uiState.value
        assertTrue("Expected Success but was $state", state is HomeUiState.Success)

        val successState = state as HomeUiState.Success
        assertEquals(1, successState.books.size)
        assertEquals("Apple Book", successState.books.first().title)

        collectJob.cancel()
    }

    @Test
    fun `list is sorted with saved and reading books at the top`() = runTest {
        val viewModel = HomeViewModel(FakeRepository())

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        val state = viewModel.uiState.value
        assertTrue("Expected Success but was $state", state is HomeUiState.Success)


        val successState = state as HomeUiState.Success
        // Banana (Saved) and Cherry (Reading > 0) should be before Apple (Neither)
        assertEquals("Banana Book", successState.books[0].title)
        assertEquals("Cherry Book", successState.books[1].title)
        assertEquals("Apple Book", successState.books[2].title)

        collectJob.cancel()

    }
}