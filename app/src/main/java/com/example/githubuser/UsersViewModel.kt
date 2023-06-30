package com.example.githubuser

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubuser.Repository.UsersRepository
import com.example.githubuser.database.Users

class UsersViewModel(application: Application): ViewModel() {
    private val mUsersRepository: UsersRepository = UsersRepository(application)

    fun insert(users: Users) {
        mUsersRepository.insert(users)
    }

    fun delete(users: Users) {
        mUsersRepository.delete(users)
    }

}