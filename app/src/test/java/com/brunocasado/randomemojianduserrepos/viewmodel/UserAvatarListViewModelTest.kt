package com.brunocasado.randomemojianduserrepos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.helper.MainCoroutineRule
import com.brunocasado.randomemojianduserrepos.helper.mock
import com.brunocasado.randomemojianduserrepos.helper.whenever
import com.brunocasado.randomemojianduserrepos.useravatar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserAvatarListViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userListUseCase: UserListUseCase

    @Mock
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    private lateinit var viewModel: UserAvatarListViewModel

    @Before
    fun setup() {
        userListUseCase = mock()
        deleteUserUseCase = mock()
        mainCoroutineRule.pauseDispatcher()
        viewModel = UserAvatarListViewModel(userListUseCase, deleteUserUseCase)
    }

    @Test
    fun `When loadUserList is invoked without network connection and has no cache then showNetworkConnectionError must be invoked`() {
        val users = mock<Observer<List<User>>>()
        viewModel.users = MutableLiveData()
        viewModel.users.observeForever(users)
        viewModel.showNetworkConnectionError = mock()


        runBlocking {
            whenever(userListUseCase.invoke()).thenReturn(Either.Left(Failure.NetworkConnection))
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showNetworkConnectionError).invoke()

        viewModel.users.removeObserver(users)
    }

    @Test
    fun `When loadUserList is invoked and persistence error occurs then showPersistenceError must be invoked`() {
        val users = mock<Observer<List<User>>>()
        viewModel.users = MutableLiveData()
        viewModel.users.observeForever(users)
        viewModel.showPersistenceError = mock()

        runBlocking {
            whenever(userListUseCase.invoke()).thenReturn(
                Either.Left(
                    UserFailure.GetUserListPersistenceError
                )
            )
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showPersistenceError).invoke()

        viewModel.users.removeObserver(users)
    }

    @Test
    fun `When loadUserList is invoked and server error occurs then showServerError must be invoked`() {
        val users = mock<Observer<List<User>>>()
        viewModel.users = MutableLiveData()
        viewModel.users.observeForever(users)
        viewModel.showServerError = mock()

        runBlocking {
            whenever(userListUseCase.invoke()).thenReturn(Either.Left(Failure.ServerError))
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showServerError).invoke()

        viewModel.users.removeObserver(users)
    }

    @Test
    fun `When loadUserList is invoked and returns data then users must have value`() {
        val users = mock<Observer<List<User>>>()
        val mockUserList = listOf(mock<User>(), mock())
        viewModel.users = MutableLiveData()
        viewModel.users.observeForever(users)

        runBlocking {
            whenever(userListUseCase.invoke()).thenReturn(Either.Right(MutableLiveData<List<User>>().apply {
                value = mockUserList
            }))
        }

        mainCoroutineRule.resumeDispatcher()

        assert(viewModel.users.value == mockUserList)

        viewModel.users.removeObserver(users)
    }

    @Test
    fun `When deleterUser is invoked and persistence error occurs must return DeleteUserPersistenceError`() {
        val user = User(
            "Bruno-Casado",
            8345519,
            "https://avatars.githubusercontent.com/u/8345519?v=4"
        )
        val users = mock<Observer<List<User>>>()
        val mockUserList = listOf(mock<User>(), mock())
        viewModel.users = MutableLiveData()
        viewModel.users.observeForever(users)
        viewModel.showPersistenceError = mock()

        runBlocking {
            whenever(userListUseCase.invoke()).thenReturn(Either.Right(MutableLiveData<List<User>>().apply {
                value = mockUserList
            }))
        }

        mainCoroutineRule.resumeDispatcher()

        mainCoroutineRule.pauseDispatcher()

        runBlocking {
            whenever(deleteUserUseCase.invoke(user)).thenReturn(Either.Left(UserFailure.DeleteUserPersistenceError))
            viewModel.deleteUser(user)
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showPersistenceError).invoke()

        viewModel.users.removeObserver(users)
    }

    @Test
    fun `When deleterUser is invoked then must invoke showDeleteUserSuccess`() {
        val user = User(
            "Bruno-Casado",
            8345519,
            "https://avatars.githubusercontent.com/u/8345519?v=4"
        )
        val users = mock<Observer<List<User>>>()
        val mockUserList = listOf(mock<User>(), mock())
        viewModel.users = MutableLiveData()
        viewModel.users.observeForever(users)
        viewModel.showDeleteUserSuccess = mock()

        runBlocking {
            whenever(userListUseCase.invoke()).thenReturn(Either.Right(MutableLiveData<List<User>>().apply {
                value = mockUserList
            }))
        }

        mainCoroutineRule.resumeDispatcher()

        mainCoroutineRule.pauseDispatcher()

        runBlocking {
            whenever(deleteUserUseCase.invoke(user)).thenReturn(Either.Right(UserSuccess.DeleteUserSuccess))
            viewModel.deleteUser(user)
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showDeleteUserSuccess).invoke()

        viewModel.users.removeObserver(users)
    }
}