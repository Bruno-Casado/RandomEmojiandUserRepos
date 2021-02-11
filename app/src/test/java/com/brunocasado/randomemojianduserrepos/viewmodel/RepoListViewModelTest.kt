package com.brunocasado.randomemojianduserrepos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoFailure
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoListUseCase
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoListViewModel
import com.brunocasado.randomemojianduserrepos.helper.MainCoroutineRule
import com.brunocasado.randomemojianduserrepos.helper.mock
import com.brunocasado.randomemojianduserrepos.helper.whenever
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
class RepoListViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repoListUseCase: RepoListUseCase

    private lateinit var viewModel: RepoListViewModel

    @Before
    fun setup() {
        repoListUseCase = mock()
        mainCoroutineRule.pauseDispatcher()
        viewModel = RepoListViewModel(repoListUseCase)
    }

    @Test
    fun `When getRepos is invoked without network connection and has no cache then showNetworkConnectionError must be invoked`() {
        val repos = mock<Observer<List<Repo>>>()
        viewModel.repos.observeForever(repos)
        viewModel.showNetworkConnectionError = mock()


        runBlocking {
            whenever(repoListUseCase.invoke(RepoListViewModel.FIRST_PAGE)).thenReturn(
                Either.Left(
                    Failure.NetworkConnection
                )
            )
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showNetworkConnectionError).invoke()

        viewModel.repos.removeObserver(repos)
    }

    @Test
    fun `When getRepos is invoked and persistence error occurs then showPersistenceError must be invoked`() {
        val repos = mock<Observer<List<Repo>>>()
        viewModel.repos.observeForever(repos)
        viewModel.showPersistenceError = mock()

        runBlocking {
            whenever(repoListUseCase.invoke(RepoListViewModel.FIRST_PAGE)).thenReturn(
                Either.Left(
                    RepoFailure.InsertRepoListPersistenceError
                )
            )
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showPersistenceError).invoke()

        viewModel.repos.removeObserver(repos)
    }

    @Test
    fun `When getRepos is invoked and server error occurs then showServerError must be invoked`() {
        val repos = mock<Observer<List<Repo>>>()
        viewModel.repos.observeForever(repos)
        viewModel.showServerError = mock()

        runBlocking {
            whenever(repoListUseCase.invoke(RepoListViewModel.FIRST_PAGE)).thenReturn(
                Either.Left(
                    Failure.ServerError
                )
            )
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showServerError).invoke()

        viewModel.repos.removeObserver(repos)
    }

    @Test
    fun `When getRepos is invoked and returns data then repos must have value`() {
        val repos = mock<Observer<List<Repo>>>()
        val mockRepoList = listOf(mock<Repo>(), mock())
        viewModel.repos.observeForever(repos)

        runBlocking {
            whenever(repoListUseCase.invoke(RepoListViewModel.FIRST_PAGE)).thenReturn(Either.Right(mockRepoList))
        }

        mainCoroutineRule.resumeDispatcher()

        assert(viewModel.repos.value == mockRepoList)

        viewModel.repos.removeObserver(repos)
    }

    @Test
    fun `When loadNextPage is invoked and there is no more page then should return LasPageReached failure`() {
        val repos = mock<Observer<List<Repo>>>()
        val mockRepoList = listOf(mock<Repo>(), mock())
        viewModel.repos.observeForever(repos)

        runBlocking {
            whenever(repoListUseCase.invoke(RepoListViewModel.FIRST_PAGE)).thenReturn(Either.Right(mockRepoList))
        }

        mainCoroutineRule.resumeDispatcher()

        mainCoroutineRule.pauseDispatcher()

        runBlocking {
            whenever(repoListUseCase.invoke(RepoListViewModel.FIRST_PAGE + 1)).thenReturn(Either.Left(RepoFailure.LastPageReached))
            viewModel.loadNextPage()
        }

        mainCoroutineRule.resumeDispatcher()

        assert(viewModel.repos.value == mockRepoList)
        assert(viewModel.dataLoadingError == RepoFailure.LastPageReached)

        viewModel.repos.removeObserver(repos)
    }
}