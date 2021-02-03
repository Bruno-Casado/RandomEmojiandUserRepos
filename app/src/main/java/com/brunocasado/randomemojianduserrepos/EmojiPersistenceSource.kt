package com.brunocasado.randomemojianduserrepos

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji

interface EmojiPersistenceSource {
    suspend fun getEmojiList(): Either<Failure, List<Emoji>>
    suspend fun saveEmojiList(emojis: List<Emoji>): Either<Failure, Success>
}