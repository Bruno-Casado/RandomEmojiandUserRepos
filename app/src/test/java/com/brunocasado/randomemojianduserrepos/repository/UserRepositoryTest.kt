package com.brunocasado.randomemojianduserrepos.repository

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfo
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.UserRepositoryImpl
import com.brunocasado.randomemojianduserrepos.helper.mock
import com.brunocasado.randomemojianduserrepos.helper.whenever
import com.brunocasado.randomemojianduserrepos.useravatar.UserFailure
import com.brunocasado.randomemojianduserrepos.useravatar.UserPersistenceSource
import com.brunocasado.randomemojianduserrepos.useravatar.UserSuccess
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UserRepositoryTest {

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var persistenceSource: UserPersistenceSource

    @Mock
    private lateinit var networkInfo: NetworkInfo

    private lateinit var repository: UserRepository
    private lateinit var user: User

    @Before
    fun setup() {
        apiService = mock()
        persistenceSource = mock()
        networkInfo = mock()
        repository = UserRepositoryImpl(apiService, persistenceSource, networkInfo)
        user = User("Bruno-Casado", 8345519, "https://avatars.githubusercontent.com/u/8345519?v=4")
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when has no internet connection and database is empty then should return NetworkConnection Failure`() {
        whenever(networkInfo.isNetworkAvailable()).thenReturn(false)

        runBlocking {
            whenever(persistenceSource.getUserByLogin("")).thenReturn(Either.Right(User("", 0, "")))

            launch(Dispatchers.Main) {
                val result = repository.getUser("")

                Assert.assertThat(result.isLeft, CoreMatchers.`is`(true))
                Assert.assertThat(
                    (result as Either.Left).a,
                    CoreMatchers.instanceOf(Failure.NetworkConnection.javaClass)
                )

                Mockito.verify(persistenceSource).getUserByLogin("")
                Mockito.verifyNoMoreInteractions(persistenceSource)
                Mockito.verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when has no internet connection and database has data then should return User`() {
        whenever(networkInfo.isNetworkAvailable()).thenReturn(false)

        runBlocking {
            whenever(persistenceSource.getUserByLogin("Bruno-Casado")).thenReturn(Either.Right(user))

            launch(Dispatchers.Main) {
                val result = repository.getUser("Bruno-Casado")

                Assert.assertThat(result.isRight, CoreMatchers.`is`(true))
                Assert.assertThat((result as Either.Right).b, CoreMatchers.`is`(user))

                Mockito.verify(persistenceSource).getUserByLogin("Bruno-Casado")
                Mockito.verifyNoMoreInteractions(persistenceSource)
                Mockito.verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when occurs some error getUser database request then should return GetUserPersistenceError Failure`() {
        runBlocking {
            whenever(persistenceSource.getUserByLogin("Bruno-Casado")).thenReturn(Either.Left(UserFailure.GetUserPersistenceError))

            launch(Dispatchers.Main) {
                val result = repository.getUser("Bruno-Casado")

                Assert.assertThat(result.isLeft, CoreMatchers.`is`(true))
                Assert.assertThat(
                    (result as Either.Left).a,
                    CoreMatchers.instanceOf(UserFailure.GetUserPersistenceError.javaClass)
                )

                Mockito.verify(persistenceSource).getUserByLogin("Bruno-Casado")
                Mockito.verifyNoMoreInteractions(persistenceSource)
                Mockito.verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when has internet connection and database is empty then should fetch User from Network and save on database`() {
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getUserByLogin("Bruno-Casado")).thenReturn(
                Either.Right(User("", 0, "")),
                Either.Right(user)
            )
            whenever(apiService.getUser("Bruno-Casado")).thenReturn(user)
            whenever(persistenceSource.saveUser(user)).thenReturn(Either.Right(UserSuccess.SaveUserSuccess))

            launch(Dispatchers.Main) {
                val result = repository.getUser("Bruno-Casado")

                Assert.assertThat(result.isRight, CoreMatchers.`is`(true))
                Assert.assertThat((result as Either.Right).b, CoreMatchers.`is`(user))

                Mockito.verify(persistenceSource, Mockito.times(2)).getUserByLogin("Bruno-Casado")
                Mockito.verify(persistenceSource).saveUser(user)
                Mockito.verify(apiService).getUser("Bruno-Casado")
            }
        }
    }

    @Test
    fun `when occurs some error on saveUser database request then should return SaveUserPersistenceError Failure`() {
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getUserByLogin("Bruno-Casado")).thenReturn(Either.Right(User("", 0, "")))
            whenever(apiService.getUser("Bruno-Casado")).thenReturn(user)
            whenever(persistenceSource.saveUser(user)).thenReturn(Either.Left(UserFailure.SaveUserPersistenceError))

            launch(Dispatchers.Main) {
                val result = repository.getUser("Bruno-Casado")

                Assert.assertThat(result.isLeft, CoreMatchers.`is`(true))
                Assert.assertThat(
                    (result as Either.Left).a,
                    CoreMatchers.instanceOf(UserFailure.SaveUserPersistenceError.javaClass)
                )

                Mockito.verify(persistenceSource).getUserByLogin("Bruno-Casado")
                Mockito.verify(persistenceSource).saveUser(user)
                Mockito.verifyNoMoreInteractions(persistenceSource)
                Mockito.verify(apiService).getUser("Bruno-Casado")
            }
        }
    }
}