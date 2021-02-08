package com.brunocasado.randomemojianduserrepos.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey
    val login: String,
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String
)