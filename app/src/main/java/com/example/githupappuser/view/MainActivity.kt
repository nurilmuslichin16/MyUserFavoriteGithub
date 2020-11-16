package com.example.githupappuser.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githupappuser.viewModel.MainViewModel
import com.example.githupappuser.R
import com.example.githupappuser.adapter.UserAdapter
import com.example.githupappuser.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_favorite.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        showRecyclerList()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems.size > 0) {
                adapter.setData(userItems)
                ll_image_search.visibility = View.GONE
                showLoading(false)
            } else {
                Snackbar.make(lay_list_users, R.string.username_not_found, Snackbar.LENGTH_SHORT).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        lay_list_users.layoutManager = LinearLayoutManager(this)
        lay_list_users.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getDataUserFromApi(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.localization -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.favorite -> {
                val mIntent = Intent(this, UserFavoritePage::class.java)
                startActivity(mIntent)
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSelectedUser(user: User) {
        val toDetail = Intent(this@MainActivity, UserDetail::class.java)
        toDetail.putExtra(UserDetail.EXTRA_USER, user)
        toDetail.putExtra(UserDetail.EXTRA_POSITION, user.id)
        startActivity(toDetail)
    }

    private fun getDataUserFromApi(username: String) {
        if (username.isEmpty()) return
        showLoading(true)
        mainViewModel.setUsers(username)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}