package com.example.courseworkpcversion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.utils.Constants
import com.google.firebase.auth.FirebaseAuth

//this class is the main page of the app that will show the reviews and allow the user to navigate
//to other parts of the app
class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        /*val sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "guest")!!*/

        val username = FirestoreClass().getUsername()
        val lemon: TextView = findViewById(R.id.lemon)
        lemon.text = username
    }

    fun logout(view: View) {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this@HomePageActivity, LoginActivity::class.java))
        finish()
    }
}