package com.example.courseworkpcversion

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

//this class allows the user to log in
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //sets the font of the title to be different to make it look like a logo
        val titleRestaurant: TextView = findViewById(R.id.appTitleRestaurant)
        val titleReviewer: TextView = findViewById(R.id.appTitleReviewer)

        val font = Typeface.createFromAsset(assets, "Foodpacker.otf")

        titleRestaurant.typeface = font
        titleReviewer.typeface = font

    }

    fun openCreateAccount(view: View) {
        //opens the create account page
        val intent = Intent(this, UserRegistrationActivity::class.java)
        startActivity(intent)
    }

    fun userLoggedInSuccess(user: User) {
        Log.i("Username: ", user.username)
        Log.i("Email: ", user.email)
        startActivity(Intent(this@LoginActivity, HomePageActivity::class.java))
        finish()
    }

    fun login(view: View) {
        //this gets the data that the user inputted and removes spaces
        val email = findViewById<EditText>(R.id.emailText)
        val password = findViewById<EditText>(R.id.passwordText)
        val emailTxt = email.text.toString().trim { it <= ' ' }
        val passwordTxt = password.text.toString().trim { it <= ' ' }

        //this creates the snackbars
        val snackEmptyPassword =
            Snackbar.make(view, getString(R.string.missingPassword), Snackbar.LENGTH_LONG)
        snackEmptyPassword.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackEmptyEmail =
            Snackbar.make(view, getString(R.string.missingEmail), Snackbar.LENGTH_LONG)
        snackEmptyEmail.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackSuccessLogin =
            Snackbar.make(view, getString(R.string.loggingIn), Snackbar.LENGTH_LONG)
        snackSuccessLogin.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarSuccess))

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


                            //this clears all instances of the log in screen and 'resets it'
                            //if you click the back button it will take you off the app

                            snackSuccessLogin.show()
                            FirestoreClass().getUserDetails(this@LoginActivity)

                        } else {
                            //creates a snackbar with the specific firebase error
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