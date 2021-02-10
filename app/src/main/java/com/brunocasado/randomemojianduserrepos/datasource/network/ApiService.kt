package com.brunocasado.randomemojianduserrepos.datasource.network

import com.brunocasado.randomemojianduserrepos.datasource.entity.EmojiResponse
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    companion object {
        private const val EMOJIS = "emojis"
        private const val USER = "user"
        private const val USERS = "users/{$USER}"
        private const val GOOGLE_REPOS = "users/google/repos"
        const val API_URL = "https://api.github.com/"
    }

    @GET(EMOJIS)
    suspend fun getEmojiList(): EmojiResponse

    @GET(USERS)
    suspend fun getUser(
        @Path(USER) user: String
    ): User

    @GET(GOOGLE_REPOS)
    suspend fun getGoogleRepos(): List<Repo>
}