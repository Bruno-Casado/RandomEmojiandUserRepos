package com.brunocasado.randomemojianduserrepos.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brunocasado.randomemojianduserrepos.MainActivityViewModel
import com.brunocasado.randomemojianduserrepos.di.ViewModelFactory
import com.brunocasado.randomemojianduserrepos.di.ViewModelKey
import com.brunocasado.randomemojianduserrepos.useravatar.UserAvatarListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserAvatarListViewModel::class)
    abstract fun bindUserAvatarListViewModel(userAvatarListViewModel: UserAvatarListViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}