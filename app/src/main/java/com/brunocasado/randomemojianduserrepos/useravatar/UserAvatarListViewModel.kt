package com.brunocasado.randomemojianduserrepos.useravatar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserAvatarListViewModel @Inject constructor(
    private val userListUseCase: UserListUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    lateinit var showNetworkConnectionError: () -> Unit
    lateinit var showPersistenceError: () -> Unit
    lateinit var showServerError: () -> Unit
    lateinit var users: LiveData<List<User>>
    lateinit var showDeleteUserSuccess: () -> Unit

    init {
        loadUserList()
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            val deleteUserFuture = async {
                deleteUserUseCase.invoke(user)
            }
            val deleteUserFutureResult = deleteUserFuture.await()
            when (deleteUserFutureResult) {
                is Either.Left -> handleError(deleteUserFutureResult.a)
                is Either.Right -> showDeleteUserSuccess()
            }
        }
    }

    private fun loadUserList() {
        viewModelScope.launch {
            val userListFuture = async {
                userListUseCase.invoke()
            }
            val userListFutureResult = userListFuture.await()
            when (userListFutureResult) {
                is Either.Left -> handleError(userListFutureResult.a)
                is Either.Right -> handleUserListSuccess(userListFutureResult.b)
            }
        }
    }

    private fun handleError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> showNetworkConnectionError()
            Failure.ServerError -> showServerError()
            is UserFailure.GetUserListPersistenceError -> showPersistenceError()
            is UserFailure.DeleteUserPersistenceError -> showPersistenceError()
            else -> showServerError()
        }
    }

    private fun handleUserListSuccess(result: LiveData<List<User>>) {
        users = result
    }
}
