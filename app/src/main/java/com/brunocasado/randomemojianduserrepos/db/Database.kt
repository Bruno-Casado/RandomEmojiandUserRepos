package com.brunocasado.randomemojianduserrepos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji

@Database(
    entities = [
        Emoji::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun emojiDao(): EmojiDao
}