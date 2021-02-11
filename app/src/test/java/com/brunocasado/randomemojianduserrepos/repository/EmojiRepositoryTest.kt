package com.brunocasado.randomemojianduserrepos.repository

import com.brunocasado.randomemojianduserrepos.emojilist.EmojiFailure
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiPersistenceSource
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiSuccess
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.core.network.NetworkInfo
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.entity.EmojiResponse
import com.brunocasado.randomemojianduserrepos.datasource.network.ApiService
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepositoryImpl
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
class EmojiRepositoryTest {

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var persistenceSource: EmojiPersistenceSource

    @Mock
    private lateinit var networkInfo: NetworkInfo

    private lateinit var repository: EmojiRepository

    @Before
    fun setup() {
        apiService = mock()
        persistenceSource = mock()
        networkInfo = mock()
        repository = EmojiRepositoryImpl(apiService, persistenceSource, networkInfo)

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
            whenever(persistenceSource.getEmojiList()).thenReturn(Either.Right(listOf()))

            launch(Dispatchers.Main) {
                val result = repository.getEmojiList()

                assertThat(result.isLeft, `is`(true))
                assertThat(
                    (result as Either.Left).a,
                    instanceOf(Failure.NetworkConnection.javaClass)
                )

                verify(persistenceSource).getEmojiList()
                verifyNoMoreInteractions(persistenceSource)
                verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when has no internet connection and database has data then should return Emoji List`() {
        val emojis = listOf(mock<Emoji>(), mock())
        whenever(networkInfo.isNetworkAvailable()).thenReturn(false)

        runBlocking {
            whenever(persistenceSource.getEmojiList()).thenReturn(Either.Right(emojis))

            launch(Dispatchers.Main) {
                val result = repository.getEmojiList()

                assertThat(result.isRight, `is`(true))
                assertThat((result as Either.Right).b.size, `is`(2))

                verify(persistenceSource).getEmojiList()
                verifyNoMoreInteractions(persistenceSource)
                verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when occurs some error getEmojiList database request then should return GetEmojiListPersistenceError Failure`() {
        runBlocking {
            whenever(persistenceSource.getEmojiList()).thenReturn(Either.Left(EmojiFailure.GetEmojiListPersistenceError))

            launch(Dispatchers.Main) {
                val result = repository.getEmojiList()

                assertThat(result.isLeft, `is`(true))
                assertThat(
                    (result as Either.Left).a,
                    instanceOf(EmojiFailure.GetEmojiListPersistenceError.javaClass)
                )

                verify(persistenceSource).getEmojiList()
                verifyNoMoreInteractions(persistenceSource)
                verifyNoInteractions(apiService)
            }
        }
    }

    @Test
    fun `when has internet connection and database is empty then should fetch Emoji List from Network and save on database`() {
        val emojis = listOf(mock<Emoji>(), mock())
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getEmojiList()).thenReturn(
                Either.Right(listOf()),
                Either.Right(emojis)
            )
            whenever(apiService.getEmojiList()).thenReturn(EmojiResponse(mapOf(), emojis))
            whenever(persistenceSource.saveEmojiList(emojis)).thenReturn(Either.Right(EmojiSuccess.SaveEmojiListSuccess))

            launch(Dispatchers.Main) {
                val result = repository.getEmojiList()

                assertThat(result.isRight, `is`(true))
                assertThat((result as Either.Right).b.size, `is`(2))

                verify(persistenceSource, times(2)).getEmojiList()
                verify(persistenceSource).saveEmojiList(emojis)
                verify(apiService).getEmojiList()
            }
        }
    }

    @Test
    fun `when occurs some error on saveEmojiList database request then should return SaveEmojiListPersistenceError Failure`() {
        val emojis = listOf(mock<Emoji>(), mock())
        whenever(networkInfo.isNetworkAvailable()).thenReturn(true)

        runBlocking {
            whenever(persistenceSource.getEmojiList()).thenReturn(Either.Right(listOf()))
            whenever(apiService.getEmojiList()).thenReturn(EmojiResponse(mapOf(), emojis))
            whenever(persistenceSource.saveEmojiList(emojis)).thenReturn(Either.Left(EmojiFailure.SaveEmojiListPersistenceError))

            launch(Dispatchers.Main) {
                val result = repository.getEmojiList()

                assertThat(result.isLeft, `is`(true))
                assertThat(
                    (result as Either.Left).a,
                    instanceOf(EmojiFailure.SaveEmojiListPersistenceError.javaClass)
                )

                verify(persistenceSource).getEmojiList()
                verify(persistenceSource).saveEmojiList(emojis)
                verifyNoMoreInteractions(persistenceSource)
                verify(apiService).getEmojiList()
            }
        }
    }
}