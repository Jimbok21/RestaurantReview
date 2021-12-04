package com.example.courseworkpcversion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

//this class is the main page of the app that will show the reviews and allow the user to navigate
//to other parts of the app
class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        val sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "guest")!!

        val lemon: TextView = findViewById(R.id.lemon)
        lemon.text = username

        //setting up the toolbar
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.map -> {
                    startActivity(Intent(this@HomePageActivity, MapsActivity::class.java))
                }
                R.id.restaurant -> {
                    startActivity(Intent(this@HomePageActivity, RestaurantsActivity::class.java))
                }
            }
            true
        }

    }

    fun logout(view: View) {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this@HomePageActivity, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
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
                logout(myView)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}