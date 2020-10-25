package com.example.githupappuser

import android.content.Context
import android.content.res.TypedArray
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {

    private lateinit var dataUsername: Array<String>
    private lateinit var dataName: Array<String>
    private lateinit var dataLokasi: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataAvatar: TypedArray

    private var users = arrayListOf<User>()

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun firstItem(context: Context): ArrayList<User> {
        dataUsername = context.resources.getStringArray(R.array.username)
        dataName = context.resources.getStringArray(R.array.name)
        dataLokasi = context.resources.getStringArray(R.array.location)
        dataRepository = context.resources.getStringArray(R.array.repository)
        dataCompany = context.resources.getStringArray(R.array.company)
        dataFollowers = context.resources.getStringArray(R.array.followers)
        dataFollowing = context.resources.getStringArray(R.array.following)
        dataAvatar = context.resources.obtainTypedArray(R.array.avatar)
        for (position in dataName.indices) {
            val user = User(
                dataUsername[position],
                dataName[position],
                dataLokasi[position],
                dataRepository[position].toInt(),
                dataCompany[position],
                dataFollowers[position].toInt(),
                dataFollowing[position].toInt(),
                dataAvatar.getResourceId(position, -1)
            )
            users.add(user)
        }
        return users
    }

    fun setUsers(username: String) {
        val listItems = ArrayList<User>()

        val apiKey = "dec01f000ba2ef1fd72fdee45490e49f4b855e20"
        val url = "https://api.github.com/search/users?q=$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d("MainViewModel API", result)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    var username: String
                    var avatar: String

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        username = user.getString("login")
                        avatar = user.getString("avatar_url")
                    }

                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("MainViewModel Exception", "onSuccess: ${e.message.toString()}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("Exception", "onFailure: ${error?.message.toString()}")
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}