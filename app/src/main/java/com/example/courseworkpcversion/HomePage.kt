package com.example.courseworkpcversion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")
    }

    fun logout(view: View) {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this@HomePage, MainActivity::class.java))
        finish()
    }
}