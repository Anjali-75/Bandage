package com.example.bandage.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bandage.Fragments.LoginFragment
import com.example.bandage.R

class Auth : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.authFrame, LoginFragment())
            .commit()
    }
}