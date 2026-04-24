package com.example.boocksaitakehomeassignment.domain.repository

import com.example.boocksaitakehomeassignment.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getAllBook(): Flow<List<Book>>
    fun getBookById(id : String):Flow<Book?>

    suspend fun toggleSaveStatus(id:String, isSaved:Boolean)
    suspend fun updateReadingProgress(id: String, progress:Int)

    suspend fun initializeDatabaseIfNeeded()
}