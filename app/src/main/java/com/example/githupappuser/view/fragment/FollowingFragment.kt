package com.example.githupappuser.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githupappuser.R
import com.example.githupappuser.adapter.FollowingAdapter
import com.example.githupappuser.viewModel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        showRecyclerView()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        if (arguments != null) {
            val username = arguments?.getString(FollowingFragment.EXTRA_FOLLOWING)
            followingViewModel.setFollowingUser(username.toString())
        }

        followingViewModel.getFollowingUser().observe(viewLifecycleOwner, Observer { followers ->
            if (followers.size > 0) {
                adapter.setData(followers)
            } else {
                ll_image_following.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecyclerView() {
        lay_list_following.layoutManager = LinearLayoutManager(context)
        lay_list_following.adapter = adapter
    }

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }
}