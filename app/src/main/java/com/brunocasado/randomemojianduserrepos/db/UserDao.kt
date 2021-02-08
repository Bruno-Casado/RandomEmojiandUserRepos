package com.brunocasado.randomemojianduserrepos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunocasado.randomemojianduserrepos.datasource.entity.User

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: User)

    @Query("SELECT * FROM User WHERE login = :login")
    abstract suspend fun getUser(login: String): User
}