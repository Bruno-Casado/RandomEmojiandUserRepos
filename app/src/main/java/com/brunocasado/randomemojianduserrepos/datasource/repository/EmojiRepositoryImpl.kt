package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.EmojiFailure
import com.brunocasado.randomemojianduserrepos.EmojiPersistenceSource
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfo
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmojiRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val persistenceSource: EmojiPersistenceSource,
    private val networkInfo: NetworkInfo
) : EmojiRepository {

    override suspend fun getEmojiList(): Either<Failure, List<Emoji>> {
        val emojiList = getEmojiListFromDisk()
        return when (emojiList) {
            is Either.Left -> emojiList
            is Either.Right -> {
                if (shouldFetchFromNetwork(emojiList.b)) {
                    if (networkInfo.isNetworkAvailable()) {
                        handleNetworkRequest(loadEmojiListFromNetwork())
                    } else {
                        Either.Left(Failure.NetworkConnection)
                    }
                } else {
                    emojiList
                }
            }
        }
    }

    private suspend fun handleNetworkRequest(networkRequest: Either<Failure, List<Emoji>>): Either<Failure, List<Emoji>> {
        val databaseRequest = when (networkRequest) {
            is Either.Left -> {
                return networkRequest
            }
            is Either.Right -> {
                saveEmojiListToDisk(networkRequest.b)
            }
        }
        return when (databaseRequest) {
            is Either.Left -> databaseRequest
            is Either.Right -> getEmojiListFromDisk()
        }
    }

    private fun shouldFetchFromNetwork(data: List<Emoji>): Boolean = data.isEmpty()

    private suspend fun loadEmojiListFromNetwork(): Either<Failure, List<Emoji>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEmojiList()
                Either.Right(response.emojiList)
            } catch (ex: Exception) {
                Either.Left(EmojiFailure.ServerError)
            }
        }
    }

    private suspend fun saveEmojiListToDisk(emojiList: List<Emoji>): Either<Failure, Success> {
        return withContext(Dispatchers.IO) {
            persistenceSource.saveEmojiList(emojiList)
        }
    }

    private suspend fun getEmojiListFromDisk(): Either<Failure, List<Emoji>> {
        return persistenceSource.getEmojiList()
    }
}