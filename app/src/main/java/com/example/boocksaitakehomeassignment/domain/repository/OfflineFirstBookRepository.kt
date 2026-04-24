package com.example.boocksaitakehomeassignment.domain.repository

import android.content.Context
import android.util.Log
import com.example.boocksaitakehomeassignment.data.local.BookDao
import com.example.boocksaitakehomeassignment.data.local.BookEntity
import com.example.boocksaitakehomeassignment.data.local.toDomain
import com.example.boocksaitakehomeassignment.domain.model.Book
import com.example.boocksaitakehomeassignment.domain.model.BookDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OfflineFirstBookRepository @Inject constructor(
    private val bookDao: BookDao,
    @ApplicationContext private val context: Context
) : BookRepository {

    //Configure JSON Parser
    private val json = Json { ignoreUnknownKeys = true }
    override fun getAllBook(): Flow<List<Book>> {
        return bookDao.getAllBooks().map { entities ->
            entities.map { it.toDomain() }

        }
    }

    override fun getBookById(id: String): Flow<Book?> {
        return bookDao.getBookById(id).map { it?.toDomain() }
    }

    override suspend fun toggleSaveStatus(id: String, isSaved: Boolean) {
        val entity = bookDao.getBookById(id).first()
        if (entity != null) {
            bookDao.updateBook(entity.copy(isSaved = isSaved))
        }
    }

    override suspend fun updateReadingProgress(id: String, progress: Int) {
        val entity = bookDao.getBookById(id).first()
        if (entity != null) {
            bookDao.updateBook(entity.copy(readingProgress = progress))
        }
    }

    override suspend fun initializeDatabaseIfNeeded() {
        val currentBooks = bookDao.getAllBooks().first()

        // Only parse the JSON if our database is completely empty
        if (currentBooks.isEmpty()) {
            try {
                // Read the file from assets
                val jsonString =
                    context.assets.open("books.json").bufferedReader().use { it.readText() }

                //Pare Json into DTOs
                val dtos = json.decodeFromString<List<BookDto>>(jsonString)

                //Map DTOs to Room Entities
                val entities = dtos.map { dto->
                    BookEntity(
                        id = dto.id,
                        title = dto.title,
                        author = dto.author,
                        coverUrl = dto.coverUrl,
                        summary = dto.summary,
                        content = dto.content,
                        isSaved = false,
                        readingProgress = 0

                    )

                }
                //Save to Room DB
                bookDao.insertAll(entities)
            }catch (e : Exception){
                Log.e("BOOCK_DEBUG", "Failed to parse JSON", e)
                e.printStackTrace()
            }
        }
    }
}