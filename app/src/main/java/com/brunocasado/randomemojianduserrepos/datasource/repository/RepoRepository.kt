package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo

interface RepoRepository {
    suspend fun getPagedRepos(page: Int): Either<Failure, List<Repo>>
}