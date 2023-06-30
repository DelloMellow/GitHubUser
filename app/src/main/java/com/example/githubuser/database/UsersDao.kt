package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(users: Users)

    @Delete
    fun delete(users: Users)

    @Query("SELECT * from Users")
    fun getAllUsers(): LiveData<List<Users>>

    @Query("SELECT * from Users WHERE username =:username ")
    fun getUsersByUsername(username: String): LiveData<Users>
}