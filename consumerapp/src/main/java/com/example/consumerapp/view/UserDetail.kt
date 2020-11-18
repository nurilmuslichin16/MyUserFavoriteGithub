package com.example.consumerapp.view

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.adapter.SectionsPagerAdapter
import com.example.consumerapp.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.example.consumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.consumerapp.db.DatabaseContract.UserColumns.Companion.ID
import com.example.consumerapp.db.DatabaseContract.UserColumns.Companion.URL
import com.example.consumerapp.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.example.consumerapp.model.User
import com.example.consumerapp.viewModel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.activity_user_favorite.*

class UserDetail : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri
    private lateinit var userDetail: User

    private var statusFavorite = false

    private fun setActionBarTitle(username: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = username
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        showLoading(true)

        val queryId = intent.getIntExtra(EXTRA_POSITION, 0)
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + queryId)

        val cursor = contentResolver?.query(uriWithId, null, null, null, null)

        userDetail = intent.getParcelableExtra<User>(EXTRA_USER) as User

        checkStatusFavorite(cursor)

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

        fab_add_favorite.setOnClickListener {
            checkActionButton(queryId)

            setStatusFavorite(statusFavorite)
        }

    }

    private fun checkStatusFavorite(cursor: Cursor?) {
        if (cursor?.count != 0) {
            setStatusFavorite(true)
        } else {
            setStatusFavorite(false)
        }
    }

    private fun checkActionButton(queryId: Int) {
        val cursor = contentResolver?.query(uriWithId, null, null, null, null)
        if (cursor?.count != 0) {
            statusFavorite = false

            contentResolver.delete(uriWithId, null, null)

            showSnackbarMessage(getString(R.string.success_remove_favorite))
        } else {
            statusFavorite = true

            val values = ContentValues()
            values.put(ID, queryId)
            values.put(AVATAR, userDetail.avatar)
            values.put(USERNAME, userDetail.username)
            values.put(URL, userDetail.url)

            contentResolver.insert(CONTENT_URI, values)

            showSnackbarMessage(getString(R.string.success_add_favorite))
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fab_add_favorite.setImageResource(R.drawable.ic_favorite_enable)
        } else {
            fab_add_favorite.setImageResource(R.drawable.ic_favorite_disable)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar_detail.visibility = View.VISIBLE
        } else {
            progress_bar_detail.visibility = View.GONE
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(scroll_detail, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
    }
}