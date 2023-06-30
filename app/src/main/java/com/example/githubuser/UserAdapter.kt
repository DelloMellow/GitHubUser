package com.example.githubuser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UserAdapter(private val userAdapter: List<String?>, private val listImage: List<String?>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false))


    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.tvUser.text = userAdapter[position]
        Picasso.get().load(listImage[position]).into(holder.ivUser)

        holder.cardView.setOnClickListener {
            val moveWithDataIntent = Intent(holder.context, UserDetailActivity::class.java)
            moveWithDataIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, userAdapter[position])
            moveWithDataIntent.putExtra(UserDetailActivity.EXTRA_AVATAR_URL, listImage[position])
            holder.context.startActivity(moveWithDataIntent)
        }
    }

    override fun getItemCount(): Int = userAdapter.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val cardView: CardView = view.findViewById(R.id.card_view)
        val ivUser: ImageView = view.findViewById(R.id.iv_user_photo)
        val tvUser: TextView = view.findViewById(R.id.tv_username)
    }

}