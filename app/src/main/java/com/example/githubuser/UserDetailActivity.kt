package com.example.githubuser

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.githubuser.database.Users
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String
    private lateinit var avatarUrl: String
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var users: Users = Users()
    private var isFavorite = false

    companion object {

        const val EXTRA_USERNAME = "username"
        const val EXTRA_AVATAR_URL = "avatar_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        username = intent.getStringExtra(EXTRA_USERNAME).toString()
        avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL).toString()
        supportActionBar?.title = username
        supportActionBar?.elevation = 0f

        SectionsPagerAdapter.username = username

        getUserDetail()

        usersViewModel = obtainViewModel(this)
        favoriteViewModel = obtainFavoriteViewModel(this)
        favoriteViewModel.getUsersByUsername(username).observe(this) { userList ->
            isFavorite = if (userList != null) {
                binding.btnAddFavorite.setText(R.string.remove_from_favorite)
                true
            } else {
                binding.btnAddFavorite.setText(R.string.add_to_favorite)
                false
            }
        }

        binding.btnAddFavorite.setOnClickListener(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getUserDetail() {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(userData: DetailUserResponse) {
        Picasso.get().load(userData.avatarUrl).into(binding.ivUserDetailAvatar)
        binding.tvUserDetailName.text = userData.name
        binding.tvUserDetailUsername.text = userData.login
        binding.tvUserDetailFollower.text = userData.followers.toString() + " Followers"
        binding.tvUserDetailFollowing.text = userData.following.toString() + " Following"
    }

    private fun obtainViewModel(activity: AppCompatActivity): UsersViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UsersViewModel::class.java)
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onClick(v: View?) {
        users.let {
            users.username = username
            users.avatarUrl = avatarUrl
        }

        if (isFavorite) {
            usersViewModel.delete(users)
            Toast.makeText(this, "Removed from My Favorite!", Toast.LENGTH_SHORT).show()
        } else {
            usersViewModel.insert(users)
            Toast.makeText(this, "Added to My Favorite!", Toast.LENGTH_SHORT).show()
        }


    }
}