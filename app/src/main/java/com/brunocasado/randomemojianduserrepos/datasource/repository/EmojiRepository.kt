package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji

interface EmojiRepository {
    suspend fun getEmojiList(): Either<Failure, List<Emoji>>
}