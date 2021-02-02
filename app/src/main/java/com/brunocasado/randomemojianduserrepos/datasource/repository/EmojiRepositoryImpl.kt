package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.EmojiFailure
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmojiRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : EmojiRepository {

    override suspend fun getEmojiList(): Either<Failure, List<Emoji>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEmojiList()
                Either.Right(response.emojiList)
            } catch (ex: Exception) {
                Either.Left(EmojiFailure.ListNotAvailable)
            }
        }
    }
}