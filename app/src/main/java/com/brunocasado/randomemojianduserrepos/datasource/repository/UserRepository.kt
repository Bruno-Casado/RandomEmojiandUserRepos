package com.brunocasado.randomemojianduserrepos.datasource.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.User

interface UserRepository {
    suspend fun getUser(): Either<Failure, User>
}