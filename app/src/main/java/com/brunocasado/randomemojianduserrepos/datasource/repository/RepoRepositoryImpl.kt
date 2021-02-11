package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfo
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.db.RepoDao
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoFailure
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoPersistenceSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val persistenceSource: RepoPersistenceSource,
    private val networkInfo: NetworkInfo
) : RepoRepository {

    private var lastPage = -1

    override suspend fun getPagedRepos(page: Int): Either<Failure, List<Repo>> {
        val repos = getPagedReposFromDisk(page)
        return when (repos) {
            is Either.Left -> repos
            is Either.Right -> {
                if (shouldFetchFromNetwork(repos.b)) {
                    if (networkInfo.isNetworkAvailable()) {
                        handleNetworkRequest(loadReposFromNetwork(), page)
                    } else {
                        Either.Left(Failure.NetworkConnection)
                    }
                } else if (isLastPage(page, repos.b)) {
                    Either.Left(RepoFailure.LastPageReached)
                } else {
                    repos
                }
            }
        }
    }

    private suspend fun getPagedReposFromDisk(page: Int): Either<Failure, List<Repo>> {
        return persistenceSource.getPagedRepos(page)
    }

    private fun shouldFetchFromNetwork(repos: List<Repo>): Boolean {
        return lastPage == -1 && repos.isEmpty()
    }

    private suspend fun handleNetworkRequest(
        networkRequest: Either<Failure, List<Repo>>,
        page: Int
    ): Either<Failure, List<Repo>> {
        val databaseRequest = when (networkRequest) {
            is Either.Left -> return networkRequest
            is Either.Right -> {
                insertRepoListToDisk(networkRequest.b)
            }
        }
        return when (databaseRequest) {
            is Either.Left -> databaseRequest
            is Either.Right -> {
                getPagedReposFromDisk(page)
            }
        }
    }

    private fun isLastPage(page: Int, repos: List<Repo>): Boolean {
        return page > lastPage && repos.isEmpty()
    }

    private suspend fun loadReposFromNetwork(): Either<Failure, List<Repo>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getGoogleRepos()
                lastPage = (response.size / RepoDao.PAGE_SIZE)
                Either.Right(response)
            } catch (ex: Exception) {
                Either.Left(Failure.ServerError)
            }
        }
    }

    private suspend fun insertRepoListToDisk(repos: List<Repo>): Either<Failure, Success> {
        return withContext(Dispatchers.IO) {
            persistenceSource.insertRepos(repos)
        }
    }
}