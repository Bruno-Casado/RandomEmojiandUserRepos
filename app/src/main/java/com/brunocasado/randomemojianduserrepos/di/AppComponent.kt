package com.brunocasado.randomemojianduserrepos.di

import android.app.Application
import com.brunocasado.randomemojianduserrepos.CustomApplication
import com.brunocasado.randomemojianduserrepos.di.module.ApplicationModule
import com.brunocasado.randomemojianduserrepos.di.module.NetworkModule
import com.brunocasado.randomemojianduserrepos.di.module.RepositoryModule
import com.brunocasado.randomemojianduserrepos.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ApplicationModule::class
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