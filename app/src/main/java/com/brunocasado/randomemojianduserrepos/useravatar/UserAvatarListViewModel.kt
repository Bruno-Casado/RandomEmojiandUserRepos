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
                is Either.Left -> showDeleteUserError(deleteUserFutureResult.a)
                is Either.Right -> showDeleteUserSuccess()
            }
        }
    }

    private fun showDeleteUserError(failure: Failure) {
        handleUserListError(failure)
    }

    private fun loadUserList() {
        viewModelScope.launch {
            val userListFuture = async {
                userListUseCase.invoke()
            }
            val userListFutureResult = userListFuture.await()
            when (userListFutureResult) {
                is Either.Left -> handleUserListError(userListFutureResult.a)
                is Either.Right -> handleUserListSuccess(userListFutureResult.b)
            }
        }
    }

    private fun handleUserListError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> TODO()
            Failure.ServerError -> TODO()
            is Failure.FeatureFailure -> TODO()
        }
    }

    private fun handleUserListSuccess(result: LiveData<List<User>>) {
        users = result
    }
}
