package com.brunocasado.randomemojianduserrepos.datasource.entity

data class EmojiResponse(
    val result: Map<String, String>,
    val emojiList: List<Emoji>
)