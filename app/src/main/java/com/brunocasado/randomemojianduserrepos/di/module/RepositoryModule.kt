package com.brunocasado.randomemojianduserrepos.di.module

import android.app.Application
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfoImpl
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepositoryImpl
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
class RepositoryModule {
    @Provides
    internal fun providesEmojiRepository(
        apiService: ApiService,
        application: Application
    ): EmojiRepository {
        return EmojiRepositoryImpl(apiService, NetworkInfoImpl(application.applicationContext))
    }
}