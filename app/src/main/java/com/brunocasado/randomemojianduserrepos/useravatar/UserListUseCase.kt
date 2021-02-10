package com.brunocasado.randomemojianduserrepos.useravatar

import androidx.lifecycle.LiveData
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepository

class UserListUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Either<Failure, LiveData<List<User>>> {
        return userRepository.getUserList()
    }
}