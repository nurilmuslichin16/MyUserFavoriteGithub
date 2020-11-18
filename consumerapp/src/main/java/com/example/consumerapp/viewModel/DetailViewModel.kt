package com.example.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.BuildConfig
import com.example.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel: ViewModel() {
    private val detailUser = MutableLiveData<User>()

    fun setDetailUser(username: String) {
        val apiKey = BuildConfig.API_KEY
        val url = "https://api.github.com/users/$username"

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
                try {
                    val responseObject = JSONObject(result)
                    val user = User()
                    user.name = responseObject.getString("name")
                    if (user.name == "null") user.name = "-" else user.name = user.name
                    user.repository = responseObject.getInt("public_repos")
                    user.followers = responseObject.getInt("followers")
                    user.following = responseObject.getInt("following")
                    user.company = responseObject.getString("company")
                    if (user.company == "null") user.company = "-" else user.company = user.company
                    user.location = responseObject.getString("location")
                    if (user.location == "null") user.location = "-" else user.location = user.location
                    detailUser.postValue(user)
                } catch (e: Exception) {
                    Log.d("DetailViewModel", "On Success: ${e.message.toString()}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("DetailViewModel", "On Failure: ${error?.message.toString()}")

                val message = when(statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : Check Your Connectivity"
                }

                Log.d("DetailViewModel", message)
            }
        })
    }

    fun getDetailUser(): LiveData<User> {
        return detailUser
    }
}