package com.brunocasado.randomemojianduserrepos.emojilist

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.db.Database
import com.brunocasado.randomemojianduserrepos.db.EmojiDao
import javax.inject.Inject

class LocalEmojiPersistenceSource @Inject constructor(
    private val database: Database,
    private val emojiDao: EmojiDao
) : EmojiPersistenceSource {

    override suspend fun getEmojiList(): Either<Failure, List<Emoji>> {
        return try {
            Either.Right(emojiDao.getEmojiList())
        } catch (ex: Exception) {
            Either.Left(EmojiFailure.GetEmojiListPersistenceError)
        }
    }

    override suspend fun saveEmojiList(emojis: List<Emoji>): Either<Failure, Success> {
        return try {
            database.runInTransaction {
                emojiDao.insert(*emojis.toTypedArray())
            }
            Either.Right(EmojiSuccess.SaveEmojiListSuccess)
        } catch (ex: Exception) {
            Either.Left(EmojiFailure.SaveEmojiListPersistenceError)
        }
    }
}