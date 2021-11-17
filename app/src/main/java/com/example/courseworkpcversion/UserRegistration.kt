package com.example.courseworkpcversion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

//This whole class is no longer implemented.
//I merged the functionality of creating a new user into the home page.
//I am keeping this file in case I change my mind.
class UserRegistration : AppCompatActivity() {

    lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_registration)


        handler = DatabaseHelper(this)
    }
}