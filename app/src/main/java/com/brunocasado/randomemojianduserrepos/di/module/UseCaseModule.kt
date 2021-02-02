package com.brunocasado.randomemojianduserrepos.di.module

import com.brunocasado.randomemojianduserrepos.EmojiListUseCase
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun provideEmojiListUseCase(repository: EmojiRepository): EmojiListUseCase =
        EmojiListUseCase(repository)
}