package com.example.githupappuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listData = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<User>) {
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(this)
                    .load(user.avatar)
                    .placeholder(R.drawable.github)
                    .error(R.drawable.github)
                    .into(img_avatar)
                txt_name.text = user.username
                txt_link.text = user.url

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}