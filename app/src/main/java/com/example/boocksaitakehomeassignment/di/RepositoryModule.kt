package com.example.boocksaitakehomeassignment.di

import com.example.boocksaitakehomeassignment.domain.repository.BookRepository
import com.example.boocksaitakehomeassignment.domain.repository.OfflineFirstBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        offlineFirstBookRepository: OfflineFirstBookRepository
    ): BookRepository
}