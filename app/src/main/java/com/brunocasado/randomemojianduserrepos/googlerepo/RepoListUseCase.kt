package com.brunocasado.randomemojianduserrepos.googlerepo

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.datasource.repository.RepoRepository

class RepoListUseCase(private val repoRepository: RepoRepository) {
    suspend operator fun invoke(page: Int): Either<Failure, List<Repo>> {
        return repoRepository.getPagedRepos(page)
    }
}