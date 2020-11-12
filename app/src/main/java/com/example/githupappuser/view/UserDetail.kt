package com.example.githupappuser.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githupappuser.R
import com.example.githupappuser.adapter.SectionsPagerAdapter
import com.example.githupappuser.model.User
import com.example.githupappuser.viewModel.DetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetail : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private fun setActionBarTitle(username: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = username
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        showLoading(true)
        val userDetail = intent.getParcelableExtra<User>(EXTRA_USER) as User

        setActionBarTitle(userDetail.username)

        Glide.with(this)
            .load(userDetail.avatar)
            .into(img_avatar_detail)
        tv_username_detail.text = userDetail.username

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.setDetailUser(userDetail.username)
        detailViewModel.getDetailUser().observe(this, Observer { user ->
            if (user != null) {
                tv_nama_detail.text = user.name
                tv_repository_detail.text = user.repository.toString()
                tv_followers_detail.text = user.followers.toString()
                tv_following_detail.text = user.following.toString()
                tv_company_detail.text = user.company
                tv_location_detail.text = user.location
            }
            showLoading(false)
        })

        val sectionPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionPagerAdapter.setData(userDetail.username)
        vp_detail.adapter = sectionPagerAdapter
        tab_layout.setupWithViewPager(vp_detail)
        supportActionBar?.elevation = 0f

//        val bundle = Bundle()
//        val followersFragment = FollowersFragment()
//        bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, userDetail.username)
//        followersFragment.arguments = bundle
//
//        val followingFragment = FollowingFragment()
//        bundle.putString(FollowingFragment.EXTRA_FOLLOWING, userDetail.username)
//        followingFragment.arguments = bundle
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar_detail.visibility = View.VISIBLE
        } else {
            progress_bar_detail.visibility = View.GONE
        }
    }
}