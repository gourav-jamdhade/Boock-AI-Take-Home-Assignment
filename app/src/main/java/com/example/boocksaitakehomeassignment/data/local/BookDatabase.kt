package com.example.boocksaitakehomeassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BoockDatabase : RoomDatabase() {

    abstract val bookDao: BookDao
}