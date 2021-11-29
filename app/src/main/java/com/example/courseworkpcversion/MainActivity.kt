package com.example.courseworkpcversion

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

//this class allows the user to log in
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleRestaurant: TextView = findViewById(R.id.appTitleRestaurant)
        val titleReviewer: TextView = findViewById(R.id.appTitleReviewer)

        val font = Typeface.createFromAsset(assets, "Foodpacker.otf")

        titleRestaurant.typeface = font
        titleReviewer.typeface = font

    }

    fun openCreateAccount(view: View) {
        //opens the create account page
        val intent = Intent(this, UserRegistration::class.java)
        startActivity(intent)
    }


    fun login(view: View) {
        //this gets the data that the user inputted
        val email = findViewById<EditText>(R.id.emailText)
        val password = findViewById<EditText>(R.id.passwordText)
        val emailTxt = email.text.toString()
        val passwordTxt = password.text.toString()

        //this creates the error message snackbars
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

                            //this clears all instances of the log in screen and 'resets it'
                            //if you click the back button it will take you off the app
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