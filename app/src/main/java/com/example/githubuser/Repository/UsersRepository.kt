package com.example.githubuser.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.database.Users
import com.example.githubuser.database.UsersDao
import com.example.githubuser.database.UsersRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UsersRepository(application: Application) {
    private val mUsersDao: UsersDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UsersRoomDatabase.getDatabase(application)
        mUsersDao = db.usersDao()
    }

    fun getAllUsers(): LiveData<List<Users>> = mUsersDao.getAllUsers()

    fun insert(users: Users) {
        executorService.execute { mUsersDao.insert(users) }
    }

    fun delete(users: Users) {
        executorService.execute { mUsersDao.delete(users) }
    }

    fun getUsersByUsername(users: String): LiveData<Users> {
        return mUsersDao.getUsersByUsername(users)
    }
}