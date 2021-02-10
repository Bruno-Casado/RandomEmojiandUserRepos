package com.brunocasado.randomemojianduserrepos.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.brunocasado.randomemojianduserrepos.datasource.entity.User

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: User)

    @Query("SELECT * FROM User WHERE login = :login")
    abstract suspend fun getUser(login: String): User

    @Query("SELECT * FROM User")
    abstract fun getUserList(): LiveData<List<User>>

    @Delete
    abstract suspend fun deleteUser(user: User)
}