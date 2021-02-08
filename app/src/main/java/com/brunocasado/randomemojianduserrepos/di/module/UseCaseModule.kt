package com.brunocasado.randomemojianduserrepos.di.module

import com.brunocasado.randomemojianduserrepos.EmojiListUseCase
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepository
import com.brunocasado.randomemojianduserrepos.useravatar.UserUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun provideEmojiListUseCase(repository: EmojiRepository): EmojiListUseCase =
        EmojiListUseCase(repository)

    @Provides
    fun provideUserUseCase(repository: UserRepository): UserUseCase = UserUseCase(repository)
}