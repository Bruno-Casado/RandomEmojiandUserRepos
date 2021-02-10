package com.brunocasado.randomemojianduserrepos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunocasado.randomemojianduserrepos.datasource.entity.Repo

@Dao
abstract class RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repos: Repo)

    @Query("SELECT * FROM Repo LIMIT $PAGE_SIZE*:page, $PAGE_SIZE")
    abstract suspend fun getPagedRepos(page: Int): List<Repo>

    companion object {
        const val PAGE_SIZE = 15
    }
}