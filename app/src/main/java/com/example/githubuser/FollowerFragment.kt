package com.example.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.githubuser.databinding.FragmentFollowerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private var username = ""

    companion object{
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME).toString()
        }

        getFollower()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getFollower() {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowers(username)

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
            }
           })
        }


    private fun setUserData(userData: List<ItemsItem?>?) {
        val listUsername = ArrayList<String?>()
        val listImage = ArrayList<String?>()
        if (userData != null) {
            for (user in userData) {
                listUsername.add(user?.login)
                listImage.add(user?.avatarUrl)
            }
        }
        val adapter = UserAdapter(listUsername, listImage)
        binding.rvFollower.adapter = adapter

    }

}