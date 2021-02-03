package com.brunocasado.randomemojianduserrepos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji

@Dao
abstract class EmojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg emojis: Emoji)

    @Query("SELECT * FROM Emoji")
    abstract fun getEmojiList(): List<Emoji>

}