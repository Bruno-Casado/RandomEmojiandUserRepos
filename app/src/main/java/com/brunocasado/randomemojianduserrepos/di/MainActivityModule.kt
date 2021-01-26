package com.brunocasado.randomemojianduserrepos.di

import com.brunocasado.randomemojianduserrepos.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}