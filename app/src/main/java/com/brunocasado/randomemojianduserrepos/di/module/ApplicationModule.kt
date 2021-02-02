package com.brunocasado.randomemojianduserrepos.di.module

import android.content.Context
import com.brunocasado.randomemojianduserrepos.CustomApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {
    @Provides
    fun provideContext(application: CustomApplication): Context = application.applicationContext
}