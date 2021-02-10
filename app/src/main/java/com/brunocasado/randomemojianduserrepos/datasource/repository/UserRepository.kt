package com.brunocasado.randomemojianduserrepos.datasource.repository

import androidx.lifecycle.LiveData
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.User

interface UserRepository {
    suspend fun getUser(login: String): Either<Failure, User>
    suspend fun getUserList(): Either<Failure, LiveData<List<User>>>
    suspend fun deleteUser(user: User): Either<Failure, Success>
}