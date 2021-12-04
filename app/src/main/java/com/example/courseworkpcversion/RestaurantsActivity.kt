package com.example.courseworkpcversion

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RestaurantsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurants)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        //because I am using activities and not fragments, the naviagtion bar
        //resets to home unless I have this code to stop it
        val currentActivity: View = bottomNav.findViewById(R.id.restaurant)
        currentActivity.performClick()

        bottomNav.setOnItemSelectedListener {
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
        //setting up the toolbar
        val mToolbar = findViewById<Toolbar>(R.id.toolbarRestaurant)
        setSupportActionBar(mToolbar)
    }
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar_layout, menu)
    return true
}
    fun logout() {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@RestaurantsActivity, LoginActivity::class.java))
        finish()
    }
    override fun onOptionsItemSelected(itemRestaurant: MenuItem): Boolean {
        val myView = findViewById<View>(R.id.toolbarRestaurant)
        when (itemRestaurant.itemId) {
            R.id.search -> {
                val snackbar = Snackbar.make(myView, "searching", Snackbar.LENGTH_LONG)
                snackbar.show()
                return true
            }
            R.id.logoutToolbar -> {
                logout()
            }

        }
        return super.onOptionsItemSelected(itemRestaurant)
    }
}