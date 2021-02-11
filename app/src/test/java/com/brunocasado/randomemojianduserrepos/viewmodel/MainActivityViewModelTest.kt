package com.brunocasado.randomemojianduserrepos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.brunocasado.randomemojianduserrepos.EmojiFailure
import com.brunocasado.randomemojianduserrepos.EmojiListUseCase
import com.brunocasado.randomemojianduserrepos.MainActivityViewModel
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
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
class MainActivityViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var emojiListUseCase: EmojiListUseCase

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setup() {
        emojiListUseCase = mock()
        mainCoroutineRule.pauseDispatcher()
        viewModel = MainActivityViewModel(emojiListUseCase)
    }

    @Test
    fun `When getEmoji is invoked without network connection and has no cache then showNetworkConnectionError must be invoked`() {
        val emojis = mock<Observer<List<Emoji>>>()
        val isLoading = mock<Observer<Boolean>>()
        viewModel.emojis.observeForever(emojis)
        viewModel.isLoading.observeForever(isLoading)
        viewModel.showNetworkConnectionError = mock()


        runBlocking {
            whenever(emojiListUseCase.invoke()).thenReturn(Either.Left(Failure.NetworkConnection))
            assert(viewModel.isLoading.value == true)
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showNetworkConnectionError).invoke()
        assert(viewModel.isLoading.value == false)

        viewModel.emojis.removeObserver(emojis)
        viewModel.isLoading.removeObserver(isLoading)
    }

    @Test
    fun `When getEmoji is invoked and persistence error occurs then showPersistenceError must be invoked`() {
        val emojis = mock<Observer<List<Emoji>>>()
        val isLoading = mock<Observer<Boolean>>()
        viewModel.emojis.observeForever(emojis)
        viewModel.isLoading.observeForever(isLoading)
        viewModel.showPersistenceError = mock()

        runBlocking {
            whenever(emojiListUseCase.invoke()).thenReturn(Either.Left(EmojiFailure.SaveEmojiListPersistenceError))
            assert(viewModel.isLoading.value == true)
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showPersistenceError).invoke()
        assert(viewModel.isLoading.value == false)

        viewModel.emojis.removeObserver(emojis)
        viewModel.isLoading.removeObserver(isLoading)
    }

    @Test
    fun `When getEmoji is invoked and server error occurs then showServerError must be invoked`() {
        val emojis = mock<Observer<List<Emoji>>>()
        val isLoading = mock<Observer<Boolean>>()
        viewModel.emojis.observeForever(emojis)
        viewModel.isLoading.observeForever(isLoading)
        viewModel.showServerError = mock()

        runBlocking {
            whenever(emojiListUseCase.invoke()).thenReturn(Either.Left(Failure.ServerError))
            assert(viewModel.isLoading.value == true)
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showServerError).invoke()
        assert(viewModel.isLoading.value == false)

        viewModel.emojis.removeObserver(emojis)
        viewModel.isLoading.removeObserver(isLoading)
    }

    @Test
    fun `When getEmoji is invoked and returns data then emojis must have value and showSuccessMessage should be invoked`() {
        val emojis = mock<Observer<List<Emoji>>>()
        val isLoading = mock<Observer<Boolean>>()
        val mockEmojiList = listOf(mock<Emoji>(), mock())
        viewModel.emojis.observeForever(emojis)
        viewModel.isLoading.observeForever(isLoading)
        viewModel.showSuccessMessage = mock()

        runBlocking {
            whenever(emojiListUseCase.invoke()).thenReturn(Either.Right(mockEmojiList))
            assert(viewModel.isLoading.value == true)
        }

        mainCoroutineRule.resumeDispatcher()

        verify(viewModel.showSuccessMessage).invoke()
        assert(viewModel.isLoading.value == false)
        assert(viewModel.emojis.value == mockEmojiList)

        viewModel.emojis.removeObserver(emojis)
        viewModel.isLoading.removeObserver(isLoading)
    }

    @Test
    fun `When showRandomEmoji is invoked and emojis is not empty then loadUrlIntoImageView should be invoked`() {
        val emojis = mock<Observer<List<Emoji>>>()
        val mockEmojiList = listOf(mock<Emoji>())
        viewModel.emojis.observeForever(emojis)
        viewModel.loadUrlIntoImageView = mock()
        viewModel.showSuccessMessage = mock()

        runBlocking {
            whenever(emojiListUseCase.invoke()).thenReturn(Either.Right(mockEmojiList))
        }

        mainCoroutineRule.resumeDispatcher()

        viewModel.showRandomEmoji()

        verify(viewModel.loadUrlIntoImageView).invoke(mockEmojiList[0])
        verify(viewModel.showSuccessMessage).invoke()
        assert(viewModel.emojis.value == mockEmojiList)

        viewModel.emojis.removeObserver(emojis)
    }

    @Test
    fun `When showRandomEmoji is invoked and emojis is empty then showLoadEmojiIntoImageViewError should be invoked`() {
        val emojis = mock<Observer<List<Emoji>>>()
        viewModel.emojis.observeForever(emojis)
        viewModel.showLoadEmojiIntoImageViewError = mock()
        viewModel.showSuccessMessage = mock()

        runBlocking {
            whenever(emojiListUseCase.invoke()).thenReturn(Either.Right(listOf()))
        }

        mainCoroutineRule.resumeDispatcher()

        viewModel.showRandomEmoji()

        verify(viewModel.showLoadEmojiIntoImageViewError).invoke()
        verify(viewModel.showSuccessMessage).invoke()
        assert(viewModel.emojis.value?.isEmpty() ?: false)

        viewModel.emojis.removeObserver(emojis)
    }
}