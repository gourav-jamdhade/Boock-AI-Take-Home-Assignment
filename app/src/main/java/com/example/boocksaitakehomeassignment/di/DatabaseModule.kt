package com.example.boocksaitakehomeassignment.di

import android.app.Application
import androidx.room.Room
import com.example.boocksaitakehomeassignment.data.local.BoockDatabase
import com.example.boocksaitakehomeassignment.data.local.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBoockDatabase(app: Application): BoockDatabase {
        return Room.databaseBuilder(
            app,
            BoockDatabase::class.java,
            "boock_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookDao(db: BoockDatabase): BookDao {
        return db.bookDao
    }
}