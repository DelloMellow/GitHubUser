package com.example.githubuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.Repository.UsersRepository
import com.example.githubuser.database.Users

class FavoriteViewModel(application: Application): ViewModel() {
    private val mUsersRepository: UsersRepository = UsersRepository(application)

    fun getAllUsers(): LiveData<List<Users>> = mUsersRepository.getAllUsers()

    fun getUsersByUsername(username: String): LiveData<Users> = mUsersRepository.getUsersByUsername(username)
}