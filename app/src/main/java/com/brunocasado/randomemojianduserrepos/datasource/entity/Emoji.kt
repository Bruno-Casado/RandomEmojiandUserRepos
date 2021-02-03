package com.brunocasado.randomemojianduserrepos.datasource.entity

import androidx.room.Entity

@Entity(
    primaryKeys = ["name"]
)
data class Emoji(
    val name: String,
    val url: String
)