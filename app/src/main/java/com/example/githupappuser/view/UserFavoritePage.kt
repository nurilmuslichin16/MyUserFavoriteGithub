package com.example.githupappuser.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githupappuser.R
import com.example.githupappuser.adapter.UserFavoriteAdapter
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githupappuser.helper.MappingHelper
import com.example.githupappuser.model.UserFavorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoritePage : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        supportActionBar?.title = "User Favorite"

        lay_list_favorite_users.layoutManager = LinearLayoutManager(this)
        lay_list_favorite_users.setHasFixedSize(true)
        adapter = UserFavoriteAdapter(this)
        lay_list_favorite_users.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listUsers = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUsers)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(lay_list_favorite_users, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progress_bar_favorite.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progress_bar_favorite.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                ll_image_favorite.visibility = View.GONE
                adapter.listUsers = notes
            } else {
                adapter.listUsers = ArrayList()
                ll_image_favorite.visibility = View.VISIBLE
                showSnackbarMessage(getString(R.string.snackbar_no_user_favorite))
            }
        }
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}