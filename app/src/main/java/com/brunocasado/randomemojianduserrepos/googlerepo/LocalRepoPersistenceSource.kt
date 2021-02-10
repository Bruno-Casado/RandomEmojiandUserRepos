package com.brunocasado.randomemojianduserrepos.googlerepo

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.db.Database
import com.brunocasado.randomemojianduserrepos.db.RepoDao
import javax.inject.Inject

class LocalRepoPersistenceSource @Inject constructor(
    private val database: Database,
    private val repoDao: RepoDao
) : RepoPersistenceSource {
    override suspend fun insertRepos(repos: List<Repo>): Either<Failure, Success> {
        return try {
            database.runInTransaction {
                repoDao.insert(*repos.toTypedArray())
            }
            Either.Right(RepoSuccess.SaveRepoListSuccess)
        } catch (ex: Exception) {
            Either.Left(RepoFailure.InsertRepoListPersistenceError)
        }
    }

    override suspend fun getPagedRepos(page: Int): Either<Failure, List<Repo>> {
        return try {
            Either.Right(repoDao.getPagedRepos(page))
        } catch (ex: Exception) {
            Either.Left(RepoFailure.GetRepoListPersistenceError)
        }
    }
}