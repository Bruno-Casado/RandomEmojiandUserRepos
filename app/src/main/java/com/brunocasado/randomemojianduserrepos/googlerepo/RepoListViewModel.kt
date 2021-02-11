package com.brunocasado.randomemojianduserrepos.googlerepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
    private val repoListUseCase: RepoListUseCase
) : ViewModel() {

    lateinit var showNetworkConnectionError: () -> Unit
    lateinit var showPersistenceError: () -> Unit
    lateinit var showServerError: () -> Unit

    private val _repos = MutableLiveData<List<Repo>>().apply { value = emptyList() }
    val repos: LiveData<List<Repo>> = _repos

    private var currentPage = FIRST_PAGE

    var dataLoadingError: Failure? = null
    var isLoading: Boolean = false

    init {
        getRepos()
    }

    fun loadNextPage() {
        currentPage++
        getRepos()
    }

    private fun getRepos() {
        isLoading = true
        viewModelScope.launch {
            val repoListFuture = async {
                repoListUseCase.invoke(currentPage)
            }

            val repoListFutureResult = repoListFuture.await()
            when (repoListFutureResult) {
                is Either.Left -> handleError(repoListFutureResult.a)
                is Either.Right -> handleSuccess(repoListFutureResult.b)
            }
            isLoading = false
        }
    }

    private fun handleError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> showNetworkConnectionError()
            Failure.ServerError -> showServerError()
            is RepoFailure.LastPageReached -> dataLoadingError = failure
            is RepoFailure.GetRepoListPersistenceError -> showPersistenceError()
            is RepoFailure.InsertRepoListPersistenceError -> showPersistenceError()
            else -> showServerError()
        }
    }

    private fun handleSuccess(repoList: List<Repo>) {
        val arrayList = ArrayList<Repo>()
        arrayList.addAll(_repos.value ?: listOf())
        arrayList.addAll(repoList)
        _repos.value = arrayList
    }

    companion object {
        const val FIRST_PAGE = 0
    }
}