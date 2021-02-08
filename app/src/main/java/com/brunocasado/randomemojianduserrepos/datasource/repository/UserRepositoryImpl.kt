package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfo
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.useravatar.UserPersistenceSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val persistenceSource: UserPersistenceSource,
    private val networkInfo: NetworkInfo
) : UserRepository {

    override suspend fun getUser(): Either<Failure, User> {
        val user = getUserFromDisk()
        return when (user) {
            is Either.Left -> user
            is Either.Right -> {
                if (shouldFetchFromNetwork(user.b)) {
                    if (networkInfo.isNetworkAvailable()) {
                        handleNetworkRequest(loadUserFromNetwork())
                    } else {
                        Either.Left(Failure.NetworkConnection)
                    }
                } else {
                    user
                }
            }
        }
    }

    private fun shouldFetchFromNetwork(user: User): Boolean = user.isEmpty()

    private suspend fun handleNetworkRequest(networkRequest: Either<Failure, User>): Either<Failure, User> {
        val databaseRequest = when (networkRequest) {
            is Either.Left -> {
                return networkRequest
            }
            is Either.Right -> {
                saveUserToDisk(networkRequest.b)
            }
        }
        return when (databaseRequest) {
            is Either.Left -> databaseRequest
            is Either.Right -> getUserFromDisk()
        }
    }

    private suspend fun loadUserFromNetwork(): Either<Failure, User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUser()
                Either.Right(response)
            } catch (ex: Exception) {
                Either.Left(Failure.ServerError)
            }
        }
    }

    private suspend fun saveUserToDisk(user: User): Either<Failure, Success> {
        return withContext(Dispatchers.IO) {
            persistenceSource.saveUser(user)
        }
    }

    private suspend fun getUserFromDisk(): Either<Failure, User> {
        return persistenceSource.getUser()
    }
}