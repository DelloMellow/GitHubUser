package com.example.githubuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoriteViewModel = obtainFavoriteViewModel(this)
        favoriteViewModel.getAllUsers().observe(this) { userList ->
            val listUsername = ArrayList<String>()
            val listAvatarUrl = ArrayList<String?>()
            for (user in userList) {
                listUsername.add(user.username)
                listAvatarUrl.add(user.avatarUrl)
            }
            val userAdapter = UserAdapter(listUsername, listAvatarUrl)
            binding.rvFavoriteUsers.adapter = userAdapter

        }
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}