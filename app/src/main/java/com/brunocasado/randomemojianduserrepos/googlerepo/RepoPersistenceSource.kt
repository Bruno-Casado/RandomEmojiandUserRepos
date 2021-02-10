package com.brunocasado.randomemojianduserrepos.googlerepo

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo

interface RepoPersistenceSource {
    suspend fun insertRepos(repos: List<Repo>): Either<Failure, Success>
    suspend fun getPagedRepos(page: Int): Either<Failure, List<Repo>>
}