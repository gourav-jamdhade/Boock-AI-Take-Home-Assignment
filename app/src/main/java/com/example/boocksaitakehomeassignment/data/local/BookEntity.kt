package com.example.boocksaitakehomeassignment.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.boocksaitakehomeassignment.domain.model.Book

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val summary: String,
    val content: String,
    val isSaved: Boolean = false,
    val readingProgress: Int = 0
)

//Ext function to map back our Domain model
fun BookEntity.toDomain(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        coverUrl = coverUrl,
        summary = summary,
        content = content,
        isSaved = isSaved,
        readingProgress = readingProgress
    )
}