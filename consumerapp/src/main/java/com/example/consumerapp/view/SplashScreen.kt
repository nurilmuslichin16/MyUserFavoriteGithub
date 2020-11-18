package com.example.consumerapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.consumerapp.R


class SplashScreen : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed(Runnable {
            val pindahFavorite = Intent(this@SplashScreen, UserFavoritePage::class.java)
            startActivity(pindahFavorite)
            finish()
        }, 2000)
    }
}