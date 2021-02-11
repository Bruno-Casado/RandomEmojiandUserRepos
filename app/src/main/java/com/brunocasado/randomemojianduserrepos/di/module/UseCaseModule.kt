package com.brunocasado.randomemojianduserrepos.di.module

import com.brunocasado.randomemojianduserrepos.emojilist.EmojiListUseCase
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.RepoRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepository
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoListUseCase
import com.brunocasado.randomemojianduserrepos.useravatar.DeleteUserUseCase
import com.brunocasado.randomemojianduserrepos.useravatar.UserListUseCase
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

    @Provides
    fun provideUserListUseCase(repository: UserRepository): UserListUseCase =
        UserListUseCase(repository)

    @Provides
    fun provideDeleteUserUseCase(repository: UserRepository): DeleteUserUseCase =
        DeleteUserUseCase(repository)

    @Provides
    fun provideRepoListUseCase(repository: RepoRepository): RepoListUseCase =
        RepoListUseCase(repository)
}