package com.example.bandage.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bandage.Fragments.LoginFragment
import com.example.bandage.Fragments.ProductList
import com.example.bandage.R

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, ProductList())
            .commit()
    }
}