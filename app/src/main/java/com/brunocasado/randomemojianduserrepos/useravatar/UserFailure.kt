package com.brunocasado.randomemojianduserrepos.useravatar

import com.brunocasado.randomemojianduserrepos.core.exception.Failure

sealed class UserFailure : Failure.FeatureFailure() {
    object GetUserPersistenceError : UserFailure()
    object SaveUserPersistenceError : UserFailure()
}