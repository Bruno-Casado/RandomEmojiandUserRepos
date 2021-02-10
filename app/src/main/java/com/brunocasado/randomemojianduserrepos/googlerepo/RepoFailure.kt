package com.brunocasado.randomemojianduserrepos.googlerepo

import com.brunocasado.randomemojianduserrepos.core.exception.Failure

sealed class RepoFailure : Failure.FeatureFailure() {
    object InsertRepoListPersistenceError : RepoFailure()
    object GetRepoListPersistenceError : RepoFailure()
    object LastPageReached : RepoFailure()
}