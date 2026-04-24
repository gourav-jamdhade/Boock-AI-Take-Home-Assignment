package com.example.boocksaitakehomeassignment.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(

    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String? = null,
    val summary: String,
    val content: String,
    val isSaved: Boolean = false,
    val readingProgress: Int = 0
)

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val summary: String,
    val content: String,
    val isSaved: Boolean = false,
    val readingProgress: Int = 0
)

fun BookDto.toDomain(): Book {
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