package com.example.courseworkpcversion

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MapsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myView = findViewById<View>(R.id.toolbar)
        when (item.itemId) {
            R.id.search -> {
                val snackbar = Snackbar.make(myView, "searching", Snackbar.LENGTH_LONG)
                snackbar.show()
                return true
            }
            R.id.logoutBtn -> {
                HomePageActivity().logout(myView)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}