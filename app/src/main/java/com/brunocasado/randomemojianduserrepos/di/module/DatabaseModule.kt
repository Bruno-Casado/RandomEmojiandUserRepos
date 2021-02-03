package com.brunocasado.randomemojianduserrepos.di.module

import android.app.Application
import androidx.room.Room
import com.brunocasado.randomemojianduserrepos.db.Database
import com.brunocasado.randomemojianduserrepos.db.EmojiDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): Database {
        return Room
            .databaseBuilder(application, Database::class.java, "emojis.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideEmojiDao(database: Database): EmojiDao = database.emojiDao()
}