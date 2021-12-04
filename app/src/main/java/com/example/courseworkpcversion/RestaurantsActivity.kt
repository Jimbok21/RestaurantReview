package com.example.courseworkpcversion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class RestaurantsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurants)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.map -> {
                    startActivity(Intent(this@RestaurantsActivity, MapsActivity::class.java))
                }
                R.id.home -> {
                    startActivity(Intent(this@RestaurantsActivity, HomePageActivity::class.java))
                }
            }
            true
        }
    }
}