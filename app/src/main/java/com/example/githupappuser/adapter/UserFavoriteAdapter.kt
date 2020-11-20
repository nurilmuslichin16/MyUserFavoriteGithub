package com.example.githupappuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.listener.CustomOnItemClickListener
import com.example.githupappuser.model.User
import com.example.githupappuser.model.UserFavorite
import com.example.githupappuser.view.UserDetail
import com.example.githupappuser.view.UserDetailFavorite
import kotlinx.android.synthetic.main.item_user_favorite.view.*

class UserFavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<UserFavoriteAdapter.UserFavoriteViewHolder>() {

    var listUsers = ArrayList<UserFavorite>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)

            notifyDataSetChanged()
        }

    fun addItem(user: UserFavorite) {
        this.listUsers.add(user)
        notifyItemInserted(this.listUsers.size - 1)
    }

    fun updateItem(position: Int, user: UserFavorite) {
        this.listUsers[position] = user
        notifyItemChanged(position, user)
    }

    fun removeItem(position: Int) {
        this.listUsers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUsers.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFavoriteAdapter.UserFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_favorite, parent, false)
        return UserFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserFavoriteAdapter.UserFavoriteViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = this.listUsers.size

    inner class UserFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserFavorite) {
            with (itemView) {
                Glide.with(this)
                    .load(user.avatar)
                    .placeholder(R.drawable.github)
                    .error(R.drawable.github)
                    .into(img_avatar_favorite)
                txt_name_favorite.text = user.username
                txt_link_favorite.text = user.url

                val extraUser = User()
                extraUser.avatar = user.avatar
                extraUser.username = user.username.toString()
                extraUser.url = user.url

                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, UserDetailFavorite::class.java)
                        intent.putExtra(UserDetailFavorite.EXTRA_USER, extraUser)
                        intent.putExtra(UserDetailFavorite.EXTRA_POSITION, user.id)
                        activity.startActivity(intent)
                    }
                }))
            }
        }
    }

}