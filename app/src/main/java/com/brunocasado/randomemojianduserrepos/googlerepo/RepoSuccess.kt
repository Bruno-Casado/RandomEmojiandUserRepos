package com.brunocasado.randomemojianduserrepos.googlerepo

import com.brunocasado.randomemojianduserrepos.core.exception.Success

sealed class RepoSuccess : Success.FeatureSuccess() {
    object SaveRepoListSuccess : RepoSuccess()
}