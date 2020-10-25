package com.example.githupappuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private val listFollowingUser = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listFollowingUser.clear()
        listFollowingUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FollowingViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return FollowingViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listFollowingUser[position])
    }

    override fun getItemCount(): Int = listFollowingUser.size

    inner class FollowingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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