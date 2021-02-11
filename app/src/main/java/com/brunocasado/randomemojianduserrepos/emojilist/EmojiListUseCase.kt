package com.brunocasado.randomemojianduserrepos.emojilist

import com.brunocasado.randomemojianduserrepos.core.Either
import com.brunocasado.randomemojianduserrepos.core.exception.Failure
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.repository.EmojiRepository

class EmojiListUseCase(private val emojiRepository: EmojiRepository) {
    suspend operator fun invoke(): Either<Failure, List<Emoji>> {
        return emojiRepository.getEmojiList()
    }
}