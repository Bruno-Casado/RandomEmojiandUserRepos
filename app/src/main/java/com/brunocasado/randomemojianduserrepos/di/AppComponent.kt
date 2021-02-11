package com.brunocasado.randomemojianduserrepos.di

import android.app.Application
import com.brunocasado.randomemojianduserrepos.CustomApplication
import com.brunocasado.randomemojianduserrepos.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(customApplication: CustomApplication)
}