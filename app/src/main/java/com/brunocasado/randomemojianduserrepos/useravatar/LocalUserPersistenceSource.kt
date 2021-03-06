package com.brunocasado.randomemojianduserrepos.useravatar

import androidx.lifecycle.LiveData
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.exception.Success
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.db.Database
import com.brunocasado.randomemojianduserrepos.db.UserDao
import javax.inject.Inject

class LocalUserPersistenceSource @Inject constructor(
    private val database: Database,
    private val userDao: UserDao
) : UserPersistenceSource {
    override suspend fun getUserByLogin(login: String): Either<Failure, User> {
        return try {
            Either.Right(userDao.getUser(login))
        } catch (ex: Exception) {
            Either.Left(UserFailure.GetUserPersistenceError)
        }
    }

    override suspend fun saveUser(user: User): Either<Failure, Success> {
        return try {
            database.runInTransaction {
                userDao.insert(user)
            }
            Either.Right(UserSuccess.SaveUserSuccess)
        } catch (ex: Exception) {
            Either.Left(UserFailure.SaveUserPersistenceError)
        }
    }

    override fun getUserList(): Either<Failure, LiveData<List<User>>> {
        return try {
            Either.Right(userDao.getUserList())
        } catch (ex: Exception) {
            Either.Left(UserFailure.GetUserListPersistenceError)
        }
    }

    override suspend fun deleteUser(user: User): Either<Failure, Success> {
        return try {
            userDao.deleteUser(user)
            Either.Right(UserSuccess.DeleteUserSuccess)
        } catch (ex: Exception) {
            Either.Left(UserFailure.DeleteUserPersistenceError)
        }
    }
}