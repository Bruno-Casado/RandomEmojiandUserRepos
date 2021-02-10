package com.brunocasado.randomemojianduserrepos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.datasource.entity.User

@Database(
    entities = [
        Emoji::class,
        User::class,
        Repo::class
    ],
    version = 3,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun emojiDao(): EmojiDao

    abstract fun userDao(): UserDao
}