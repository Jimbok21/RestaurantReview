package com.example.courseworkpcversion

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//This class allows the user to create an account and logs them in
class UserRegistration : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_registration)

    }

    fun openLogin(view: View) {
        //takes the user back to the main login page
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun createAccount(view: View) {
        val email = findViewById<TextView>(R.id.inputEmailText)
        val password = findViewById<TextView>(R.id.inputPasswordText)
        val username = findViewById<TextView>(R.id.inputUsernameText)
        val emailTxt: String = email.text.toString().trim { it <= ' ' }
        val passwordTxt: String = password.text.toString().trim { it <= ' ' }
        val usernameTxt: String = username.text.toString().trim { it <= ' ' }
        val snackEmptyPassword =
            Snackbar.make(view, "Please enter a password", Snackbar.LENGTH_LONG)
        snackEmptyPassword.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackEmptyEmail =
            Snackbar.make(view, "Please enter an email", Snackbar.LENGTH_LONG)
        snackEmptyEmail.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackEmptyUsername =
            Snackbar.make(view, "Please enter a username", Snackbar.LENGTH_LONG)
        snackEmptyUsername.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackSuccessCreated =
            Snackbar.make(view, "Account Registered Successfully", Snackbar.LENGTH_LONG)
        snackSuccessCreated.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarSuccess))

        when {
            //checks if the user has left any details blank and alerts them
            TextUtils.isEmpty(passwordTxt) -> {
                snackEmptyPassword.show()
            }

            TextUtils.isEmpty(emailTxt) -> {
                snackEmptyEmail.show()
            }

            TextUtils.isEmpty(usernameTxt) -> {
                snackEmptyUsername.show()
            }
            else -> {
                //This will create the user and sign them in
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            val user = UserData(usernameTxt, emailTxt)

                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            snackSuccessCreated.show()

                            val intent =
                                Intent(this@UserRegistration, HomePage::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", emailTxt)
                            startActivity(intent)
                            finish()
                        } else {
                            val snackError = Snackbar.make(
                                view,
                                task.exception!!.message.toString(),
                                Snackbar.LENGTH_LONG
                            )
                                snackError.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
                            snackError.show()
                        }
                    }
            }
        }
    }
}