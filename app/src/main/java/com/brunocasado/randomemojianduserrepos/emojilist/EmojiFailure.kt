package com.brunocasado.randomemojianduserrepos.emojilist

import com.brunocasado.randomemojianduserrepos.core.exception.Failure

sealed class EmojiFailure : Failure.FeatureFailure() {
    object GetEmojiListPersistenceError : EmojiFailure()
    object SaveEmojiListPersistenceError : EmojiFailure()
}