package com.brunocasado.randomemojianduserrepos

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiFailure
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiListUseCase
import com.brunocasado.randomemojianduserrepos.useravatar.UserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val emojiListUseCase: EmojiListUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    lateinit var showNetworkConnectionError: () -> Unit
    lateinit var showPersistenceError: () -> Unit
    lateinit var showServerError: () -> Unit
    lateinit var showSuccessMessage: () -> Unit
    lateinit var loadUrlIntoImageView: (Emoji) -> Unit
    lateinit var showLoadEmojiIntoImageViewError: () -> Unit
    lateinit var openEmojiListActivityAction: () -> Unit
    lateinit var displayAvatarOnEmojiImageHolder: (String) -> Unit
    lateinit var openAvatarListActivity: () -> Unit
    lateinit var openRepoListActivity: () -> Unit

    private val _emojis = MutableLiveData<List<Emoji>>().apply { value = emptyList() }
    val emojis: LiveData<List<Emoji>> = _emojis

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = _isLoading

    var userLogin: String = ""

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

    fun openEmojiListActivity() = View.OnClickListener {
        openEmojiListActivityAction()
    }

    fun removeEmojiFromList(emoji: Emoji) {
        _emojis.value?.let {
            _emojis.value = it.minusElement(emoji)
        }
    }

    fun getUser() {
        _isLoading.value = true
        viewModelScope.launch {
            val userFuture = async {
                userUseCase.invoke(userLogin)
            }

            val userFutureResult = userFuture.await()
            when (userFutureResult) {
                is Either.Left -> handleGetUserError(userFutureResult.a)
                is Either.Right -> handleGetUserSuccess(userFutureResult.b)
            }
        }
        _isLoading.value = false
    }

    fun openAvatarList() = View.OnClickListener {
        openAvatarListActivity()
    }

    fun openRepoList() = View.OnClickListener {
        openRepoListActivity()
    }

    private fun handleGetEmojiListSuccess(emojiList: List<Emoji>) {
        _emojis.value = emojiList
        showSuccessMessage()
    }

    private fun handleGetEmojiListError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> showNetworkConnectionError()
            is EmojiFailure.SaveEmojiListPersistenceError -> showPersistenceError()
            is EmojiFailure.GetEmojiListPersistenceError -> showPersistenceError()
            is Failure.ServerError -> showServerError()
            else -> showServerError()
        }
    }

    private fun handleGetUserError(failure: Failure) {
        when (failure) {
            Failure.NetworkConnection -> showNetworkConnectionError()
            Failure.ServerError -> showServerError()
            is Failure.FeatureFailure -> showPersistenceError()
        }
    }

    private fun handleGetUserSuccess(user: User) {
        displayAvatarOnEmojiImageHolder(user.avatarUrl)
    }
}