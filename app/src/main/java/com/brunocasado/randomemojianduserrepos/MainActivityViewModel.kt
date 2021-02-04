package com.brunocasado.randomemojianduserrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val emojiListUseCase: EmojiListUseCase
) : ViewModel() {

    lateinit var showNetworkConnectionError: () -> Unit
    lateinit var showPersistenceError: () -> Unit
    lateinit var showServerError: () -> Unit
    lateinit var showSuccessMessage: () -> Unit
    lateinit var loadUrlIntoImageView: (Emoji) -> Unit
    lateinit var showLoadEmojiIntoImageViewError: () -> Unit

    private val _emojis = MutableLiveData<List<Emoji>>().apply { value = emptyList() }
    val emojis: LiveData<List<Emoji>> = _emojis

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getEmoji()
    }

    fun getEmoji() {
        _isLoading.value = true
        viewModelScope.launch {
            val emojiListFuture = async {
                emojiListUseCase.invoke()
            }

            val emojiListFutureResult: Either<Failure, List<Emoji>> = emojiListFuture.await()
            when (emojiListFutureResult) {
                is Either.Right -> {
                    handleGetEmojiListSuccess(emojiListFutureResult.b)
                }
                is Either.Left -> {
                    handleGetEmojiListError(emojiListFutureResult.a)
                }
            }
            _isLoading.value = false
        }
    }

    fun showRandomEmoji() {
        val randomEmoji = try {
            emojis.value?.random()
        } catch (ex: Exception) {
            null
        }
        randomEmoji?.let {
            loadUrlIntoImageView(it)
        } ?: showLoadEmojiIntoImageViewError()
    }

    private fun handleGetEmojiListSuccess(emojiList: List<Emoji>) {
        _emojis.value = emojiList
        showSuccessMessage()
    }

    private fun handleGetEmojiListError(failure: Failure) {
        when (failure) {
            is Failure.NetworkConnection -> showNetworkConnectionError()
            is EmojiFailure.SaveEmojiListPersistenceError -> showPersistenceError()
            is EmojiFailure.GetEmojiListPersistenceError -> showPersistenceError()
            is EmojiFailure.ServerError -> showServerError()
            else -> showServerError()
        }
    }
}