package com.brunocasado.randomemojianduserrepos

import com.brunocasado.randomemojianduserrepos.core.exception.Success

sealed class EmojiSuccess : Success.FeatureSuccess() {
    object SaveEmojiListSuccess : EmojiSuccess()
}