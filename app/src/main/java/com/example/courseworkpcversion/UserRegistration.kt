package com.example.courseworkpcversion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
        val email = findViewById<TextView>(R.id.inputEmail)
        val password = findViewById<TextView>(R.id.inputPassword)
        val emailTxt: String = email.text.toString().trim { it <= ' ' }
        val passwordTxt: String = password.text.toString().trim { it <= ' ' }
        val snackEmptyPassword =
            Snackbar.make(view, "Please enter a password", Snackbar.LENGTH_LONG)
        val snackEmptyEmail =
            Snackbar.make(view, "Please enter an email", Snackbar.LENGTH_LONG)
        val snackSuccessCreated =
            Snackbar.make(view, "Account Registered Successfully", Snackbar.LENGTH_LONG)

        when {
            //checks if the user has left any details blank and alerts them
            TextUtils.isEmpty(passwordTxt) -> {
                snackEmptyPassword.show()
            }

            TextUtils.isEmpty(emailTxt) -> {
                snackEmptyEmail.show()
            }
            else -> {
                //This will create the user and sign them in
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {

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