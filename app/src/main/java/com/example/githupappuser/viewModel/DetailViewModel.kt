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

class DetailViewModel: ViewModel() {
    private val detailUser = MutableLiveData<User>()

    fun setDetailUser(username: String) {
        val apiKey = "dec01f000ba2ef1fd72fdee45490e49f4b855e20"
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
                Log.d("MainViewModel", "onSuccess: $result")
                try {
                    val responseObject = JSONObject(result)
                    val user = User()
                    user.name = responseObject.getString("name")
                    user.repository = responseObject.getInt("public_repos")
                    user.followers = responseObject.getInt("followers")
                    user.following = responseObject.getInt("following")
                    user.company = responseObject.getString("company")
                    user.location = responseObject.getString("location")
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