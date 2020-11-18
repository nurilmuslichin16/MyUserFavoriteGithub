package com.example.githupappuser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githupappuser.R
import com.example.githupappuser.view.fragment.PreferenceFragment

class SettingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_page)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_page, PreferenceFragment())
            .commit()
    }
}