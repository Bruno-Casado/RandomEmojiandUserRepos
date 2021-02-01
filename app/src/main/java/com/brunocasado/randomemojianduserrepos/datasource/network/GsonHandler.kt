package com.brunocasado.randomemojianduserrepos.datasource.network

import com.brunocasado.randomemojianduserrepos.datasource.entity.EmojiResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonHandler {
    val gson: Gson
        get() = GsonBuilder()
            .registerTypeAdapter(EmojiResponse::class.java, EmojiConverterFactory())
            .create()
}