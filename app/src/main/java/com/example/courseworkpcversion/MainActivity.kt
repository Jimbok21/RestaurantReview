package com.example.courseworkpcversion

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = DatabaseHelper(this)
    }

    //Originally I had a sepearate create account screen but it saved data to a separate database so
    //I scrapped it.
    fun openCreateAccount(view: View) {
        val intent = Intent(this, UserRegistration::class.java)
        startActivity(intent)
    }


    fun saveData (view: View) {
        val inputUsername = findViewById<TextView>(R.id.username)
        val inputPassword = findViewById<TextView>(R.id.password)
        val username = inputUsername.text.toString()
        val password = inputPassword.text.toString()

        val snackSave = Snackbar.make(view,"Profile $username has been added to the database", Snackbar.LENGTH_LONG)
        snackSave.show()
        Log.d("Trying", "Inserting Data")
        handler.insertData(username, password)
        snackSave.show()
    }


    fun login(view: android.view.View) {
        //checks if inputted data is in the database
        val userNameTxtBox = findViewById<TextView>(R.id.username)
        val passwordTxtBox = findViewById<TextView>(R.id.password)
        val userName = userNameTxtBox.text.toString()
        val password = passwordTxtBox.text.toString()
        val snackLogin = Snackbar.make(view,"Logging in", Snackbar.LENGTH_LONG)
        val snackIncorrect = Snackbar.make(view,"Incorrect log in details $userName and $password", Snackbar.LENGTH_SHORT)
        if(handler.validUserCheck(userName, password)) {
            snackLogin.show()
        } else {
            snackIncorrect.show()
        }
    }


}