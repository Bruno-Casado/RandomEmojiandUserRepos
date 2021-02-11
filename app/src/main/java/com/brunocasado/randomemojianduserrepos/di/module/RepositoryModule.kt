package com.brunocasado.randomemojianduserrepos.di.module

import android.app.Application
import com.brunocasado.randomemojianduserrepos.emojilist.LocalEmojiPersistenceSource
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfoImpl
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.*
import com.brunocasado.randomemojianduserrepos.googlerepo.LocalRepoPersistenceSource
import com.brunocasado.randomemojianduserrepos.useravatar.LocalUserPersistenceSource
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
class RepositoryModule {
    @Provides
    internal fun provideEmojiRepository(
        apiService: ApiService,
        persistenceSource: LocalEmojiPersistenceSource,
        application: Application
    ): EmojiRepository {
        return EmojiRepositoryImpl(
            apiService,
            persistenceSource,
            NetworkInfoImpl(application.applicationContext)
        )
    }

    @Provides
    internal fun provideUserRepository(
        apiService: ApiService,
        persistenceSource: LocalUserPersistenceSource,
        application: Application
    ): UserRepository {
        return UserRepositoryImpl(
            apiService,
            persistenceSource,
            NetworkInfoImpl(application.applicationContext)
        )
    }

    @Provides
    internal fun provideRepoRepository(
        apiService: ApiService,
        persistenceSource: LocalRepoPersistenceSource,
        application: Application
    ): RepoRepository {
        return RepoRepositoryImpl(
            apiService,
            persistenceSource,
            NetworkInfoImpl(application.applicationContext)
        )
    }
}