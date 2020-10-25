package com.example.githupappuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githupappuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUsers(username: String) {
        val listItems = ArrayList<User>()

        val apiKey = "dec01f000ba2ef1fd72fdee45490e49f4b855e20"
        val url = "https://api.github.com/search/users?q=$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d("MainViewModel API", result)
                try {
                    val responseObject = JSONObject(result)
                    val listArray = responseObject.getJSONArray("items")
                    if (listArray.length() == 0) {
                        Log.d("MainViewModel", "onSuccess: Data Not Found")
                    } else {
                        for (i in 0 until listArray.length()) {
                            val item = listArray.getJSONObject(i)
                            val user = User()
                            user.avatar = item.getString("avatar_url")
                            user.username = item.getString("login")
                            user.url = item.getString("html_url")
                            listItems.add(user)
                        }
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("MainViewModel Exception", "onSuccess: ${e.message.toString()}")
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("On Failure", "Exception: ${error?.message.toString()}")

                val message = when(statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : Check Your Connectivity"
                }
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}