package com.brunocasado.randomemojianduserrepos.di.module

import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    internal fun providesEmojiRepository(apiService: ApiService): EmojiRepository {
        return EmojiRepositoryImpl(apiService)
    }
}