package com.brunocasado.randomemojianduserrepos.useravatar

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Either<Failure, Success> {
        return userRepository.deleteUser(user)
    }
}