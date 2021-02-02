package com.brunocasado.randomemojianduserrepos.di.module

import android.content.Context
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfoImpl
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    internal fun providesEmojiRepository(apiService: ApiService, context: Context): EmojiRepository {
        return EmojiRepositoryImpl(apiService, NetworkInfoImpl(context))
    }
}