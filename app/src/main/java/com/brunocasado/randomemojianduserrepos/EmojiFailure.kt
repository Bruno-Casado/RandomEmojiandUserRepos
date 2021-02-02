package com.brunocasado.randomemojianduserrepos

import com.brunocasado.randomemojianduserrepos.core.exception.Failure

sealed class EmojiFailure : Failure.FeatureFailure() {
    object ListNotAvailable: EmojiFailure()
}