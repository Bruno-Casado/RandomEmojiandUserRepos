package com.brunocasado.randomemojianduserrepos.useravatar

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepository

class UserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(login: String): Either<Failure, User> {
        return userRepository.getUser(login)
    }
}