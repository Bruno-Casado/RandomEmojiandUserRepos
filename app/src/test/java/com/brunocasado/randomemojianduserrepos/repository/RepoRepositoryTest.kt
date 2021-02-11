package com.brunocasado.randomemojianduserrepos.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfo
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.RepoRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.RepoRepositoryImpl
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoFailure
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoPersistenceSource
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoSuccess
import com.brunocasado.randomemojianduserrepos.helper.mock
import com.brunocasado.randomemojianduserrepos.helper.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RepoRepositoryTest {
    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var persistenceSource: RepoPersistenceSource

    @Mock
    private lateinit var networkInfo: NetworkInfo

    private lateinit var repository: RepoRepository

    @Before
    fun setup() {
        apiService = mock()
        persistenceSource = mock()
        networkInfo = mock()
        repository = RepoRepositoryImpl(apiService, persistenceSource, networkInfo)

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when get first page and has no internet connection then must return NetworkConnection Failure`() {
        whenever(networkInfo.isNetworkAvailable()).thenReturn(false)

        runBlocking {
            whenever(persistenceSource.getPagedRepos(FIRST_PAGE)).thenReturn(Either.Right(listOf()))

            launch(Dispatchers.Main) {
                val result = repository.getPagedRepos(FIRST_PAGE)

                assertThat(result.isLeft, `is`(true))
                assertThat(
                    (result as Either.Left).a,
                    instanceOf(Failure.NetworkConnection.javaClass)
                )

                verify(persistenceSource).getPagedRepos(FIRST_PAGE)
                verifyNoMoreInteractions(persistenceSource)
                verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when get first page and some error occur on persistenceSource then should return GetRepoListPersistenceError`() {
        runBlocking {
            whenever(persistenceSource.getPagedRepos(FIRST_PAGE)).thenReturn(Either.Left(RepoFailure.GetRepoListPersistenceError))

            launch(Dispatchers.Main) {
                val result = repository.getPagedRepos(FIRST_PAGE)

                assertThat(result.isLeft, `is`(true))
                assertThat(
                    (result as Either.Left).a,
                    instanceOf(RepoFailure.GetRepoListPersistenceError.javaClass)
                )

                verify(persistenceSource).getPagedRepos(FIRST_PAGE)
                verifyNoMoreInteractions(persistenceSource)
                verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when get first page and database is empty and has Internet Connection should return Repo list`() {
        val repos = listOf<Repo>(mock(), mock())
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getPagedRepos(FIRST_PAGE)).thenReturn(
                Either.Right(listOf()),
                Either.Right(repos)
            )
            whenever(apiService.getGoogleRepos()).thenReturn(repos)
            whenever(persistenceSource.insertRepos(repos)).thenReturn(Either.Right(RepoSuccess.SaveRepoListSuccess))

            launch(Dispatchers.Main) {
                val result = repository.getPagedRepos(FIRST_PAGE)

                assertThat(result.isRight, `is`(true))
                assertThat((result as Either.Right).b.size, `is`(2))

                verify(persistenceSource, times(2)).getPagedRepos(FIRST_PAGE)
                verify(persistenceSource).insertRepos(repos)
                verify(apiService).getGoogleRepos()
            }
        }
    }

    @Test
    fun `when lastPage is reached should return LastPageReached Failure`() {
        val repos = listOf<Repo>(mock(), mock())
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getPagedRepos(FIRST_PAGE)).thenReturn(
                Either.Right(listOf()),
                Either.Right(repos)
            )
            whenever(persistenceSource.getPagedRepos(SECOND_PAGE)).thenReturn(Either.Right(listOf()))
            whenever(apiService.getGoogleRepos()).thenReturn(repos)
            whenever(persistenceSource.insertRepos(repos)).thenReturn(Either.Right(RepoSuccess.SaveRepoListSuccess))

            launch(Dispatchers.Main) {
                val firstPageResult = repository.getPagedRepos(FIRST_PAGE)

                assertThat(firstPageResult.isRight, `is`(true))
                assertThat((firstPageResult as Either.Right).b.size, `is`(2))

                val secondPageResult = repository.getPagedRepos(SECOND_PAGE)
                assertThat(secondPageResult.isLeft, `is`(true))
                assertThat(
                    (secondPageResult as Either.Left).a,
                    instanceOf(RepoFailure.LastPageReached.javaClass)
                )

                verify(persistenceSource, times(2)).getPagedRepos(FIRST_PAGE)
                verify(persistenceSource).getPagedRepos(SECOND_PAGE)
                verify(persistenceSource).insertRepos(repos)
                verify(apiService).getGoogleRepos()
            }
        }
    }

    @Test
    fun `when get second page and has data on database should return Repo List`() {
        val repos = listOf<Repo>(mock(), mock())
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getPagedRepos(FIRST_PAGE)).thenReturn(
                Either.Right(listOf()),
                Either.Right(repos)
            )
            whenever(persistenceSource.getPagedRepos(SECOND_PAGE)).thenReturn(Either.Right(repos))
            whenever(apiService.getGoogleRepos()).thenReturn(repos)
            whenever(persistenceSource.insertRepos(repos)).thenReturn(Either.Right(RepoSuccess.SaveRepoListSuccess))

            launch(Dispatchers.Main) {
                val firstPageResult = repository.getPagedRepos(FIRST_PAGE)

                assertThat(firstPageResult.isRight, `is`(true))
                assertThat((firstPageResult as Either.Right).b.size, `is`(2))

                val secondPageResult = repository.getPagedRepos(SECOND_PAGE)
                assertThat(secondPageResult.isRight, `is`(true))
                assertThat((secondPageResult as Either.Right).b.size, `is`(2))

                verify(persistenceSource, times(2)).getPagedRepos(FIRST_PAGE)
                verify(persistenceSource).getPagedRepos(SECOND_PAGE)
                verify(persistenceSource).insertRepos(repos)
                verify(apiService).getGoogleRepos()
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 0
        private const val SECOND_PAGE = 1
    }
}