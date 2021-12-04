package com.example.courseworkpcversion

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    startActivity(Intent(this@MapsActivity, HomePageActivity::class.java))
                }
                R.id.restaurant -> {
                    startActivity(Intent(this@MapsActivity, RestaurantsActivity::class.java))
                }
            }
            true
        }

    }
}