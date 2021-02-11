package com.brunocasado.randomemojianduserrepos.di

import com.brunocasado.randomemojianduserrepos.MainActivity
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiListActivity
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoListActivity
import com.brunocasado.randomemojianduserrepos.useravatar.UserAvatarListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeEmojiListActivity(): EmojiListActivity

    @ContributesAndroidInjector
    abstract fun contributeUserAvatarListActivity(): UserAvatarListActivity

    @ContributesAndroidInjector
    abstract fun contributeRepoListActivity(): RepoListActivity
}