package com.brunocasado.randomemojianduserrepos.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Repos(
    @PrimaryKey
    val id: Int,
    @SerializedName("full_name")
    val fullName: String
)