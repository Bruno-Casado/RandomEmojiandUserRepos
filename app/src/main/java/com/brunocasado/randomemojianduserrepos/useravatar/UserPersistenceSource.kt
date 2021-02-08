package com.brunocasado.randomemojianduserrepos.useravatar

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.User

interface UserPersistenceSource {
    suspend fun getUserByLogin(login: String): Either<Failure, User>
    suspend fun saveUser(user: User): Either<Failure, Success>
}