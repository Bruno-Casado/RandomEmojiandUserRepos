package com.brunocasado.randomemojianduserrepos.datasource.network

import com.brunocasado.randomemojianduserrepos.datasource.entity.EmojiResponse
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import retrofit2.http.GET

interface ApiService {

    companion object {
        private const val EMOJIS = "emojis"
        const val API_URL = "https://api.github.com/"
    }

    @GET(EMOJIS)
    suspend fun getEmojiList(): EmojiResponse

    @GET("users/{user}")
    suspend fun getUser(): User
}