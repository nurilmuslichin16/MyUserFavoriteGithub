package com.example.githupappuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FollowersAdapter: RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    private val listFollowersUser = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listFollowersUser.clear()
        listFollowersUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FollowersViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_follow, viewGroup, false)
        return FollowersViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowersUser[position])
    }

    override fun getItemCount(): Int = listFollowersUser.size

    inner class FollowersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(this)
                    .load(user.avatar)
                    .into(img_avatar)
                txt_name.text = user.username
                txt_link.text = user.url
            }
        }
    }
}