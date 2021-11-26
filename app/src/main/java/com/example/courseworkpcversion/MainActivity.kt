package com.example.courseworkpcversion

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//this class allows the user to log in
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openCreateAccount(view: View) {
        //opens the create account page
        val intent = Intent(this, UserRegistration::class.java)
        startActivity(intent)
    }


    fun login(view: View) {
        val email = findViewById<TextView>(R.id.email)
        val password = findViewById<TextView>(R.id.password)
        val emailTxt: String = email.text.toString().trim { it <= ' ' }
        val passwordTxt: String = password.text.toString().trim { it <= ' ' }


        val snackEmptyPassword =
            Snackbar.make(view, "Please enter a password", Snackbar.LENGTH_LONG)
        val snackEmptyEmail =
            Snackbar.make(view, "Please enter an email", Snackbar.LENGTH_LONG)
        val snackSuccessLogin =
            Snackbar.make(view, "Logging in", Snackbar.LENGTH_LONG)

        when {
            //checks if the user has left any details blank and alerts them
            TextUtils.isEmpty(passwordTxt) -> {
                snackEmptyPassword.show()
            }

            TextUtils.isEmpty(emailTxt) -> {
                snackEmptyEmail.show()
            }
            else -> {
                //This will check to see if the log in details are correct and sign the user in
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {

                            snackSuccessLogin.show()

                            val intent =
                                Intent(this@MainActivity, HomePage::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                            intent.putExtra("email_id", emailTxt)
                            startActivity(intent)
                            finish()
                        } else {
                            Snackbar.make(
                                view,
                                task.exception!!.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }
}