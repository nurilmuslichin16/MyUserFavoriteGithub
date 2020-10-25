package com.example.githupappuser.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.model.User
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetail : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar!!.title = "Detail User"

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        Glide.with(this)
            .load(user.avatar)
            .into(img_avatar_detail)
        tv_nama_detail.text = user.name
        tv_username_detail.text = user.username
        tv_repository_detail.text = user.repository.toString()
        tv_followers_detail.text = user.followers.toString()
        tv_following_detail.text = user.following.toString()
        tv_company_detail.text = user.company
        tv_location_detail.text = user.location
    }
}